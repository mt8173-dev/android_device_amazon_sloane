/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.tv.settings.about;

import android.app.ActivityManagerNative;
import android.app.Fragment;
import android.app.IActivityManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.SELinux;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.support.annotation.NonNull;
import android.support.v17.leanback.app.GuidedStepFragment;
import android.support.v17.leanback.widget.GuidanceStylist;
import android.support.v17.leanback.widget.GuidedAction;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.tv.settings.PreferenceUtils;
import com.android.tv.settings.R;
import com.android.tv.settings.dialog.Layout;
import com.android.tv.settings.dialog.SettingsLayoutActivity;
import com.android.tv.settings.name.DeviceManager;

import cyanogenmod.providers.CMSettings;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Activity which shows the build / model / legal info / etc.
 */
public class AboutActivity extends SettingsLayoutActivity {

    private static final String TAG = "AboutActivity";

    /**
     * Action keys for switching over in onActionClicked.
     */
    private static final int KEY_BUILD = 0;
    private static final int KEY_VERSION = 1;
    private static final int KEY_REBOOT = 2;
    private static final int KEY_MOD_VERSION = 3;
    private static final String FILENAME_PROC_VERSION = "/proc/version";
    private static final String LOG_TAG = "AboutSettings";
    private static final String PROPERTY_SELINUX_STATUS = "ro.build.selinux";
    private static String mRebootReason;

    /**
     * Intent action of SettingsLicenseActivity (for displaying open source licenses.)
     */
    private static final String SETTINGS_LEGAL_LICENSE_INTENT_ACTION = "android.settings.LICENSE";

    /**
     * Intent action of SettingsTosActivity (for displaying terms of service.)
     */
    private static final String SETTINGS_LEGAL_TERMS_OF_SERVICE = "android.settings.TERMS";

    /**
     * Intent action of device name activity.
     */
    private static final String SETTINGS_DEVICE_NAME_INTENT_ACTION = "android.settings.DEVICE_NAME";

    /**
     * Intent action of CyanogenMod updater activity.
     */
    private static final ComponentName mCmupdaterActivity =
            new ComponentName("com.cyanogenmod.updater",
                    "com.cyanogenmod.updater.UpdatesSettingsTv");
    private static final String SETTINGS_CM_UPDATER_ACTION = "android.intent.action.MAIN";

    /**
     * Intent to launch ads activity.
     */
    private static final String SETTINGS_ADS_ACTIVITY_PACKAGE = "com.google.android.gms";
    private static final String SETTINGS_ADS_ACTIVITY_ACTION =
            "com.google.android.gms.settings.ADS_PRIVACY";

    /**
     * Get the CyanogenMod version.
     */
    public static String getDisplayVersion() {
        return SystemProperties.get("ro.cm.display.version");
    }

    /**
     * Get the build date.
     */
    public static String getBuildDate() {
        return SystemProperties.get("ro.build.date");
    }

    /**
     * Reads a line from the specified file.
     * @param filename the file to read from
     * @return the first line, if any.
     * @throws IOException if the file couldn't be read
     */
    private static String readLine(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename), 256);
        try {
            return reader.readLine();
        } finally {
            reader.close();
        }
    }

    /**
     * Get the kernel version.
     */
    public static String getFormattedKernelVersion() {
        try {
            return formatKernelVersion(readLine(FILENAME_PROC_VERSION));

        } catch (IOException e) {
            Log.e(LOG_TAG,
                "IO Exception when getting kernel version for Device Info screen",
                e);

            return "Unavailable";
        }
    }

    public static String formatKernelVersion(String rawKernelVersion) {
        // Example (see tests for more):
        // Linux version 3.0.31-g6fb96c9 (android-build@xxx.xxx.xxx.xxx.com) \
        //     (gcc version 4.6.x-xxx 20120106 (prerelease) (GCC) ) #1 SMP PREEMPT \
        //     Thu Jun 28 11:02:39 PDT 2012

        final String PROC_VERSION_REGEX =
            "Linux version (\\S+) " + /* group 1: "3.0.31-g6fb96c9" */
            "\\((\\S+?)\\) " +        /* group 2: "x@y.com" (kernel builder) */
            "(?:\\(gcc.+? \\)) " +    /* ignore: GCC version information */
            "(#\\d+) " +              /* group 3: "#1" */
            "(?:.*?)?" +              /* ignore: optional SMP, PREEMPT, and any CONFIG_FLAGS */
            "((Sun|Mon|Tue|Wed|Thu|Fri|Sat).+)"; /* group 4: "Thu Jun 28 11:02:39 PDT 2012" */

        Matcher m = Pattern.compile(PROC_VERSION_REGEX).matcher(rawKernelVersion);
        if (!m.matches()) {
            Log.e(LOG_TAG, "Regex did not match on /proc/version: " + rawKernelVersion);
            return "Unavailable";
        } else if (m.groupCount() < 4) {
            Log.e(LOG_TAG, "Regex match on /proc/version only returned " + m.groupCount()
                    + " groups");
            return "Unavailable";
        }
        return m.group(1) + "\n" +                 // 3.0.31-g6fb96c9
            m.group(2) + " " + m.group(3) + "\n" + // x@y.com #1
            m.group(4);                            // Thu Jun 28 11:02:39 PDT 2012
    }

    /**
     * Get the SELinux status.
     */
    public String getSelinuxStatus() {
        String status = getString(R.string.selinux_status_enforcing);
        if (!SELinux.isSELinuxEnabled()) {
            status = getString(R.string.selinux_status_disabled);
        } else if (!SELinux.isSELinuxEnforced()) {
            status = getString(R.string.selinux_status_permissive);
        }
        return status;
    }

    /**
     * Intent component to launch PlatLogo Easter egg.
     */
    private static final ComponentName mPlatLogoActivity = new ComponentName("android",
            "com.android.internal.app.PlatLogoActivity");

    /**
     * Number of clicks it takes to be a developer.
     */
    private static final int NUM_DEVELOPER_CLICKS = 7;

    private int mDeveloperClickCount;
    private PreferenceUtils mPreferenceUtils;
    private Toast mToast;
    private final long[] mHits = new long[3];
    private int mHitsIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreferenceUtils = new PreferenceUtils(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mDeveloperClickCount = 0;
        mDeviceNameLayoutGetter.refreshView();
    }

    @Override
    public void onActionClicked(Layout.Action action) {
        final int key = action.getId();
        if (key == KEY_BUILD) {
            mDeveloperClickCount++;
            if (!mPreferenceUtils.isDeveloperEnabled()) {
                int numLeft = NUM_DEVELOPER_CLICKS - mDeveloperClickCount;
                if (numLeft < 3 && numLeft > 0) {
                    showToast(getResources().getQuantityString(
                            R.plurals.show_dev_countdown_cm, numLeft, numLeft));
                }
                if (numLeft == 0) {
                    mPreferenceUtils.setDeveloperEnabled(true);
                    showToast(getString(R.string.show_dev_on_cm));
                    mDeveloperClickCount = 0;
                }
            } else {
                if (mDeveloperClickCount > 3) {
                    showToast(getString(R.string.show_dev_already_cm));
                }
            }
        } else if (key == KEY_MOD_VERSION) {
            mHits[mHitsIndex] = SystemClock.uptimeMillis();
            mHitsIndex = (mHitsIndex + 1) % mHits.length;
            if (mHits[mHitsIndex] >= SystemClock.uptimeMillis() - 500) {
                Intent intent = new Intent();
                intent.putExtra("is_cm", true);
                intent.setComponent(mPlatLogoActivity);
                startActivity(intent);
            }
        } else if (key == KEY_VERSION) {
            mHits[mHitsIndex] = SystemClock.uptimeMillis();
            mHitsIndex = (mHitsIndex + 1) % mHits.length;
            if (mHits[mHitsIndex] >= SystemClock.uptimeMillis() - 500) {
                Intent intent = new Intent();
                intent.setComponent(mPlatLogoActivity);
                startActivity(intent);
            }
        } else if (key == KEY_REBOOT) {
            final Fragment f = new RebootConfirmFragment();
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, f)
                    .addToBackStack(null)
                    .commit();
        } else {
            Intent intent = action.getIntent();
            if (intent != null) {
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Log.e(TAG, "intent for (" + action.getTitle() + ") not found:", e);
                }
            } else {
                Log.e(TAG, "null intent for: " + action.getTitle());
            }
        }
    }

    private final Layout.LayoutGetter mDeviceNameLayoutGetter = new Layout.LayoutGetter() {
        @Override
        public Layout get() {
            return new Layout().add(new Layout.Action.Builder(getResources(),
                    new Intent(SETTINGS_DEVICE_NAME_INTENT_ACTION))
                    .title(R.string.device_name)
                    .description(DeviceManager.getDeviceName(AboutActivity.this))
                    .build());
        }
    };

    @Override
    public Layout createLayout() {
        final Resources res = getResources();

        final Layout.Header header = new Layout.Header.Builder(res)
                .icon(R.drawable.ic_settings_about)
                .title(R.string.about_preference)
                .build();

        final Intent cmupdaterIntent = new Intent();
        cmupdaterIntent.setComponent(mCmupdaterActivity);
        cmupdaterIntent.setAction(SETTINGS_CM_UPDATER_ACTION);
        cmupdaterIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        header.add(new Layout.Action.Builder(res, cmupdaterIntent)
                .title(R.string.about_cmupdate_settings_title)
                .build());
        header.add(mDeviceNameLayoutGetter);
        header.add(new Layout.Action.Builder(res, KEY_REBOOT)
                .title(R.string.restart_button_label)
                .build());
        header.add(new Layout.Header.Builder(res)
                .title(R.string.about_legal_info)
                .build()
                .add(new Layout.Action.Builder(res,
                        systemIntent(SETTINGS_LEGAL_LICENSE_INTENT_ACTION))
                        .title(R.string.about_legal_license)
                        .build())
                .add(new Layout.Action.Builder(res, systemIntent(SETTINGS_LEGAL_TERMS_OF_SERVICE))
                        .title(R.string.about_terms_of_service)
                        .build()));

        final Intent adsIntent = new Intent();
        adsIntent.setPackage(SETTINGS_ADS_ACTIVITY_PACKAGE);
        adsIntent.setAction(SETTINGS_ADS_ACTIVITY_ACTION);
        adsIntent.addCategory(Intent.CATEGORY_DEFAULT);
        final List<ResolveInfo> resolveInfos = getPackageManager().queryIntentActivities(adsIntent,
                PackageManager.MATCH_DEFAULT_ONLY);
        if (!resolveInfos.isEmpty()) {
            header.add(new Layout.Action.Builder(res, adsIntent)
                    .title(R.string.about_ads)
                    .build());
        }

        header.add(new Layout.Status.Builder(res)
                .title(R.string.about_model)
                .description(Build.MODEL)
                .build());

        String patch = Build.VERSION.SECURITY_PATCH;
        if (!TextUtils.isEmpty(patch)) {
            try {
                SimpleDateFormat template = new SimpleDateFormat("yyyy-MM-dd");
                Date patchDate = template.parse(patch);
                String format = DateFormat.getBestDateTimePattern(Locale.getDefault(), "dMMMMyyyy");
                patch = DateFormat.format(format, patchDate).toString();
            } catch (ParseException e) {
                // broken parse; fall through and use the raw string
            }
            header.add(new Layout.Status.Builder(res)
                    .title(R.string.security_patch)
                    .description(patch)
                    .build());
        }

        header.add(new Layout.Action.Builder(res, KEY_MOD_VERSION)
                .title(R.string.about_mod_version)
                .description(getDisplayVersion())
                .build());
        header.add(new Layout.Action.Builder(res, KEY_VERSION)
                .title(R.string.about_version)
                .description(Build.VERSION.RELEASE)
                .build());
        header.add(new Layout.Status.Builder(res)
                .title(R.string.about_serial)
                .description(Build.SERIAL)
                .build());
        header.add(new Layout.Status.Builder(res)
                .title(getString(R.string.about_kernel_version))
                .description(getFormattedKernelVersion())
                .build());
        header.add(new Layout.Status.Builder(res)
                .title(getString(R.string.about_build_date))
                .description(getBuildDate())
                .build());
        header.add(new Layout.Action.Builder(res, KEY_BUILD)
                .title(R.string.about_build)
                .description(Build.DISPLAY)
                .build());
        if (!SystemProperties.get(PROPERTY_SELINUX_STATUS).equals("")) {
            header.add(new Layout.Status.Builder(res)
                    .title(getString(R.string.about_selinux_status))
                    .description(getSelinuxStatus())
                    .build());
        }
        return new Layout().breadcrumb(getString(R.string.header_category_device)).add(header);
    }

    private void showToast(String toastString) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(this, toastString, Toast.LENGTH_SHORT);
        mToast.show();
    }

    // Returns an Intent for the given action if a system app can handle it, otherwise null.
    private Intent systemIntent(String action) {
        final Intent intent = new Intent(action);

        // Limit the intent to an activity that is in the system image.
        final PackageManager pm = getPackageManager();
        final List<ResolveInfo> activities = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo info : activities) {
            if ((info.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                if (info.activityInfo.isEnabled()) {
                    intent.setPackage(info.activityInfo.packageName);
                    return intent;
                }
            }
        }
        return null;  // No system image package found.
    }

    private boolean isAdvancedRebootPossible() {
        boolean advancedRebootEnabled = CMSettings.Secure.getInt(this.getContentResolver(),
                CMSettings.Secure.ADVANCED_REBOOT, 0) != 0;

        return advancedRebootEnabled;
    }

    private static void doSoftReboot() {
        try {
            final IActivityManager am =
                  ActivityManagerNative.asInterface(ServiceManager.checkService("activity"));
            if (am != null) {
                am.restart();
            }
        } catch (RemoteException e) {
            Log.e(TAG, "failure trying to perform soft reboot", e);
        }
    }

    public class RebootConfirmFragment extends GuidedStepFragment {

        private static final int ACTION_RESTART = 1;
        private static final int ACTION_REBOOT_SOFT = 2;
        private static final int ACTION_REBOOT_RECOVERY = 3;
        private static final int ACTION_REBOOT_BOOTLOADER = 4;

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            setSelectedActionPosition(1);
        }

        @Override
        public @NonNull GuidanceStylist.Guidance onCreateGuidance(Bundle savedInstanceState) {
            return new GuidanceStylist.Guidance(
                    getString(R.string.system_reboot_confirm),
                    "",
                    getString(R.string.about_preference),
                    getActivity().getDrawable(R.drawable.ic_settings_warning)
                    );
        }

        @Override
        public void onCreateActions(@NonNull List<GuidedAction> actions,
                Bundle savedInstanceState) {
            actions.add(new GuidedAction.Builder()
                    .title(getString(R.string.restart_button_label))
                    .id(ACTION_RESTART)
                    .build());
            if (isAdvancedRebootPossible()) {
                actions.add(new GuidedAction.Builder()
                        .title(getString(R.string.reboot_soft_button_label))
                        .id(ACTION_REBOOT_SOFT)
                        .build());
                actions.add(new GuidedAction.Builder()
                        .title(getString(R.string.reboot_recovery_button_label))
                        .id(ACTION_REBOOT_RECOVERY)
                        .build());
                actions.add(new GuidedAction.Builder()
                        .title(getString(R.string.reboot_bootloader_button_label))
                        .id(ACTION_REBOOT_BOOTLOADER)
                        .build());
            }
            actions.add(new GuidedAction.Builder()
                    .title(getString(android.R.string.cancel))
                    .build());
        }

        @Override
        public void onGuidedActionClicked(GuidedAction action) {
            PowerManager pm =
                    (PowerManager) getActivity().getSystemService(Context.POWER_SERVICE);
            if (action.getId() == ACTION_RESTART) {
                pm.reboot(null);
            } else if (action.getId() == ACTION_REBOOT_SOFT)  {
                doSoftReboot();
            } else if (action.getId() == ACTION_REBOOT_RECOVERY)  {
                pm.reboot(PowerManager.REBOOT_RECOVERY);
            } else if (action.getId() == ACTION_REBOOT_BOOTLOADER)  {
                pm.reboot(getString(R.string.reboot_bootloader_reason));
            } else {
                getFragmentManager().popBackStack();
            }
        }
    }
}
