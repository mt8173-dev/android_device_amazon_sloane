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

package com.android.tv.settings.system;

import android.annotation.StringRes;
import android.app.ActivityManagerNative;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageInfo;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.net.NetworkUtils;
import android.net.wifi.IWifiManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.PowerManager;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.StrictMode;
import android.os.SystemProperties;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.HardwareRenderer;
import android.view.IWindowManager;
import android.view.View;

import com.android.tv.settings.R;
import com.android.tv.settings.dialog.Layout;
import com.android.tv.settings.dialog.SettingsLayoutActivity;
import com.android.tv.settings.util.SettingsHelper;

import cyanogenmod.providers.CMSettings;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DeveloperOptionsActivity extends SettingsLayoutActivity {

    private static final String TAG = "DeveloperOptions";
    private static final boolean DEBUG = false;

    private static final int INDEX_WINDOW_ANIMATION_SCALE = 0;
    private static final int INDEX_TRANSITION_ANIMATION_SCALE = 1;
    private static final int INDEX_ANIMATOR_DURATION_SCALE = 2;
    private static final String OPENGL_TRACES_PROPERTY = "debug.egl.trace";
    private static final String HDCP_CHECKING_PROPERTY = "persist.sys.hdcp_checking";

    private static final String HDMI_OPTIMIZATION_PROPERTY = "persist.sys.hdmi.resolution";

    private static final String ADB_ROOT_PROPERTY = "cm.service.adb.root";
    private static final String ROOT_ACCESS_PROPERTY = "persist.sys.root_access";
    private static String setting = SystemProperties.get(ROOT_ACCESS_PROPERTY, "0");
    private static final String TERMINAL_APP_PACKAGE = "com.android.terminal";

    private static final int ACTION_BASE_MASK = -1 << 20;

    // General actions
    private static final int ACTION_GENERAL_BASE = 1 << 20;
    private static final int ACTION_STAY_AWAKE_ON = ACTION_GENERAL_BASE;
    private static final int ACTION_STAY_AWAKE_OFF = ACTION_GENERAL_BASE + 1;
    private static final int ACTION_HDCP_ALWAYS = ACTION_GENERAL_BASE + 2;
    private static final int ACTION_HDCP_DRM = ACTION_GENERAL_BASE + 3;
    private static final int ACTION_HDCP_NEVER = ACTION_GENERAL_BASE + 4;
    private static final int ACTION_HDMI_OPT_BEST_RES = ACTION_GENERAL_BASE + 5;
    private static final int ACTION_HDMI_OPT_BEST_FRAME = ACTION_GENERAL_BASE + 6;
    private static final int ACTION_HCI_LOGGING_ON = ACTION_GENERAL_BASE + 7;
    private static final int ACTION_HCI_LOGGING_OFF = ACTION_GENERAL_BASE + 8;

    // Debugging actions
    private static final int ACTION_DEBUGGING_BASE = 2 << 20;
    private static final int ACTION_USB_DEBUGGING_ON = ACTION_DEBUGGING_BASE;
    private static final int ACTION_USB_DEBUGGING_OFF = ACTION_DEBUGGING_BASE + 1;
    private static final int ACTION_MOCK_LOCATIONS_ON = ACTION_DEBUGGING_BASE + 2;
    private static final int ACTION_MOCK_LOCATIONS_OFF = ACTION_DEBUGGING_BASE + 3;
    private static final int ACTION_WAIT_FOR_DEBUGGER_ON = ACTION_DEBUGGING_BASE + 4;
    private static final int ACTION_WAIT_FOR_DEBUGGER_OFF = ACTION_DEBUGGING_BASE + 5;
    private static final int ACTION_VERIFY_APPS_ON = ACTION_DEBUGGING_BASE + 6;
    private static final int ACTION_VERIFY_APPS_OFF = ACTION_DEBUGGING_BASE + 7;
    private static final int ACTION_WIFI_VERBOSE_ON = ACTION_DEBUGGING_BASE + 8;
    private static final int ACTION_WIFI_VERBOSE_OFF = ACTION_DEBUGGING_BASE + 9;

    // Debug app
    private static final int ACTION_DEBUG_APP_BASE = 3 << 20;

    // Input actions
    private static final int ACTION_INPUT_BASE = 4 << 20;
    private static final int ACTION_SHOW_TOUCHES_ON = ACTION_INPUT_BASE;
    private static final int ACTION_SHOW_TOUCHES_OFF = ACTION_INPUT_BASE + 1;
    private static final int ACTION_POINTER_LOCATION_ON = ACTION_INPUT_BASE + 2;
    private static final int ACTION_POINTER_LOCATION_OFF = ACTION_INPUT_BASE + 3;

    // Drawing actions
    private static final int ACTION_DRAWING_BASE = 5 << 20;
    private static final int ACTION_LAYOUT_BOUNDS_ON = ACTION_DRAWING_BASE;
    private static final int ACTION_LAYOUT_BOUNDS_OFF = ACTION_DRAWING_BASE + 1;
    private static final int ACTION_GPU_VIEW_UPDATES_ON = ACTION_DRAWING_BASE + 2;
    private static final int ACTION_GPU_VIEW_UPDATES_OFF = ACTION_DRAWING_BASE + 3;
    private static final int ACTION_GPU_OVERDRAW_OFF = ACTION_DRAWING_BASE + 4;
    private static final int ACTION_GPU_OVERDRAW_AREAS = ACTION_DRAWING_BASE + 5;
    private static final int ACTION_GPU_OVERDRAW_COUNTER = ACTION_DRAWING_BASE + 6;
    private static final int ACTION_HARDWARE_LAYER_ON = ACTION_DRAWING_BASE + 7;
    private static final int ACTION_HARDWARE_LAYER_OFF = ACTION_DRAWING_BASE + 8;
    private static final int ACTION_SURFACE_UPDATES_ON = ACTION_DRAWING_BASE + 9;
    private static final int ACTION_SURFACE_UPDATES_OFF = ACTION_DRAWING_BASE + 10;

    // Drawing scale actions
    private static final int ACTION_WINDOW_ANIMATION_SCALE_BASE = 6 << 20;
    private static final int ACTION_TRANSITION_ANIMATION_SCALE_BASE = 7 << 20;
    private static final int ACTION_ANIMATOR_DURATION_BASE = 8 << 20;

    // Monitoring actions
    private static final int ACTION_MONITORING_BASE = 9 << 20;
    private static final int ACTION_STRICT_MODE_ON = ACTION_MONITORING_BASE;
    private static final int ACTION_STRICT_MODE_OFF = ACTION_MONITORING_BASE + 1;
    private static final int ACTION_CPU_USAGE_ON = ACTION_MONITORING_BASE + 2;
    private static final int ACTION_CPU_USAGE_OFF = ACTION_MONITORING_BASE + 3;
    private static final int ACTION_FRAME_TIME_OFF = ACTION_MONITORING_BASE + 4;
    private static final int ACTION_FRAME_TIME_BARS = ACTION_MONITORING_BASE + 5;
    private static final int ACTION_FRAME_TIME_GFXINFO = ACTION_MONITORING_BASE + 6;
    private static final int ACTION_OPENGL_TRACES_NONE = ACTION_MONITORING_BASE + 7;
    private static final int ACTION_OPENGL_TRACES_LOGCAT = ACTION_MONITORING_BASE + 8;
    private static final int ACTION_OPENGL_TRACES_SYSTRACE = ACTION_MONITORING_BASE + 9;
    private static final int ACTION_OPENGL_TRACES_ERROR = ACTION_MONITORING_BASE + 10;


    private static final int ACTION_APPS_BASE = 10 << 20;
    private static final int ACTION_DONT_KEEP_ACTIVITIES_ON = ACTION_APPS_BASE;
    private static final int ACTION_DONT_KEEP_ACTIVITIES_OFF = ACTION_APPS_BASE + 1;
    private static final int ACTION_APP_PROCESS_LIMIT_STANDARD = ACTION_APPS_BASE + 2;
    private static final int ACTION_APP_PROCESS_LIMIT_ZERO = ACTION_APPS_BASE + 3;
    private static final int ACTION_APP_PROCESS_LIMIT_ONE = ACTION_APPS_BASE + 4;
    private static final int ACTION_APP_PROCESS_LIMIT_TWO = ACTION_APPS_BASE + 5;
    private static final int ACTION_APP_PROCESS_LIMIT_THREE = ACTION_APPS_BASE + 6;
    private static final int ACTION_APP_PROCESS_LIMIT_FOUR = ACTION_APPS_BASE + 7;
    private static final int ACTION_ALL_ANRS_ON = ACTION_APPS_BASE + 8;
    private static final int ACTION_ALL_ANRS_OFF = ACTION_APPS_BASE + 9;

    // CM Actions
    private static final int ACTION_CM_BASE = 11 << 20;
    private static final int ACTION_ROOT_ACCESS_NONE = ACTION_CM_BASE;
    private static final int ACTION_ROOT_ACCESS_APPS = ACTION_CM_BASE + 1;
    private static final int ACTION_ROOT_ACCESS_ADB = ACTION_CM_BASE + 2;
    private static final int ACTION_ROOT_ACCESS_ALL = ACTION_CM_BASE + 3;
    private static final int ACTION_ENABLE_TERMINAL_ON = ACTION_CM_BASE + 4;
    private static final int ACTION_ENABLE_TERMINAL_OFF = ACTION_CM_BASE + 5;
    private static final int ACTION_ADVANCED_REBOOT_ON = ACTION_CM_BASE + 6;
    private static final int ACTION_ADVANCED_REBOOT_OFF = ACTION_CM_BASE + 7;
    private static final int ACTION_ADB_OVER_NETWORK_ON = ACTION_CM_BASE + 8;
    private static final int ACTION_ADB_OVER_NETWORK_OFF = ACTION_CM_BASE + 9;

    private IWindowManager mWindowManager;
    private WifiManager mWifiManager;

    private static class MyApplicationInfo {
        final ApplicationInfo info;
        final CharSequence label;
        int id;

        public MyApplicationInfo(ApplicationInfo info, CharSequence label) {
            this.info = info;
            this.label = label;
        }
    }

    private final static Comparator<MyApplicationInfo> sDisplayNameComparator
            = new Comparator<MyApplicationInfo>() {
        @Override
        public final int compare(MyApplicationInfo a, MyApplicationInfo b) {
            return collator.compare(a.label, b.label);
        }

        private final Collator collator = Collator.getInstance();
    };

    private final List<MyApplicationInfo> mPackageInfoList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mWindowManager = IWindowManager.Stub.asInterface(ServiceManager.getService("window"));
        mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        super.onCreate(savedInstanceState);
    }

    @Override
    public Layout createLayout() {
        return new Layout().breadcrumb(getString(R.string.header_category_preferences))
                .add(new Layout.Header.Builder(getResources())
                        .title(R.string.system_developer_options)
                        .icon(R.drawable.ic_settings_developeroptions)
                        .build()
                        .add(getGeneralHeader())
                        .add(getDebuggingHeader())
                        .add(getCmHeader())
                        .add(getInputHeader())
                        .add(getDrawingHeader())
                        .add(getMonitoringHeader())
                        .add(getAppsHeader()));
    }

    private Layout.Header getGeneralHeader() {
        final Resources res = getResources();

        final String onTitle = getString(R.string.action_on_description);
        final String offTitle = getString(R.string.action_off_description);

        final Layout.Header header = new Layout.Header.Builder(res)
                .title(R.string.system_general)
                .build();

        header.add(new Layout.Header.Builder(res)
                .title(R.string.system_stay_awake)
                .detailedDescription(R.string.system_desc_stay_awake)
                .build()
                .setSelectionGroup(new Layout.SelectionGroup.Builder(2)
                        .add(onTitle, null, ACTION_STAY_AWAKE_ON)
                        .add(offTitle, null, ACTION_STAY_AWAKE_OFF)
                        .select(getStayAwakeActionId())
                        .build()));

        if (!"user".equals(Build.TYPE)) {
            header.add(new Layout.Header.Builder(res)
                    .title(R.string.system_hdcp_checking)
                    .build()
                    .setSelectionGroup(new Layout.SelectionGroup.Builder(3)
                            .add(getString(R.string.system_hdcp_checking_always), null,
                                    ACTION_HDCP_ALWAYS)
                            .add(getString(R.string.system_hdcp_checking_drm), null,
                                    ACTION_HDCP_DRM)
                            .add(getString(R.string.system_hdcp_checking_never), null,
                                    ACTION_HDCP_NEVER)
                            .select(getHdcpCheckingActionId())
                            .build()));
        }

        header.add(new Layout.Header.Builder(res)
                .title(R.string.system_hdmi_optimization)
                .detailedDescription(R.string.system_desc_hdmi_optimization)
                .build()
                .setSelectionGroup(new Layout.SelectionGroup.Builder(2)
                        .add(getString(R.string.system_hdmi_optimization_best_resolution), null,
                                ACTION_HDMI_OPT_BEST_RES)
                        .add(getString(R.string.system_hdmi_optimization_best_framerate), null,
                                ACTION_HDMI_OPT_BEST_FRAME)
                        .select(getHdmiOptimizationActionId())
                        .build()));

        header.add(new Layout.Header.Builder(res)
                .title(R.string.system_bt_hci_log)
                .detailedDescription(R.string.system_desc_bt_hci_log)
                .build()
                .setSelectionGroup(new Layout.SelectionGroup.Builder(2)
                        .add(onTitle, null, ACTION_HCI_LOGGING_ON)
                        .add(offTitle, null, ACTION_HCI_LOGGING_OFF)
                        .select(getHciLoggingActionId())
                        .build()));
        return header;
    }

    private Layout.Header getDebuggingHeader() {
        final Resources res = getResources();

        final String onTitle = getString(R.string.action_on_description);
        final String offTitle = getString(R.string.action_off_description);

        return new Layout.Header.Builder(res)
                .title(R.string.system_debugging)
                .build()
                .add(new Layout.Header.Builder(res)
                        .title(R.string.system_usb_debugging)
                        .detailedDescription(R.string.system_desc_usb_debugging)
                        .build()
                        .setSelectionGroup(new Layout.SelectionGroup.Builder(2)
                                .add(onTitle, null, ACTION_USB_DEBUGGING_ON)
                                .add(offTitle, null, ACTION_USB_DEBUGGING_OFF)
                                .select(getUsbDebuggingActionId())
                                .build()))
                .add(new Layout.Header.Builder(res)
                        .title(R.string.system_allow_mock_locations)
                        .build()
                        .setSelectionGroup(new Layout.SelectionGroup.Builder(2)
                                .add(onTitle, null, ACTION_MOCK_LOCATIONS_ON)
                                .add(offTitle, null, ACTION_MOCK_LOCATIONS_OFF)
                                .select(getMockLocationsActionId())
                                .build()))
                .add(getDebugAppHeader())
                .add(new Layout.Header.Builder(res)
                        .title(R.string.system_wait_for_debugger)
                        .detailedDescription(R.string.system_desc_wait_for_debugger)
                        .build()
                        .setSelectionGroup(new Layout.SelectionGroup.Builder(2)
                                .add(onTitle, null, ACTION_WAIT_FOR_DEBUGGER_ON)
                                .add(offTitle, null, ACTION_WAIT_FOR_DEBUGGER_OFF)
                                .select(getWaitForDebuggerActionId())
                                .build()))
                .add(new Layout.Header.Builder(res)
                        .title(R.string.system_verify_apps_over_usb)
                        .detailedDescription(R.string.system_desc_verify_apps_over_usb)
                        .build()
                        .setSelectionGroup(new Layout.SelectionGroup.Builder(2)
                                .add(onTitle, null, ACTION_VERIFY_APPS_ON)
                                .add(offTitle, null, ACTION_VERIFY_APPS_OFF)
                                .select(getVerifyAppsActionId())
                                .build()))
                .add(new Layout.Header.Builder(res)
                        .title(R.string.system_wifi_verbose_logging)
                        .detailedDescription(R.string.system_desc_wifi_verbose_logging)
                        .build()
                        .setSelectionGroup(new Layout.SelectionGroup.Builder(2)
                                .add(onTitle, null, ACTION_WIFI_VERBOSE_ON)
                                .add(offTitle, null, ACTION_WIFI_VERBOSE_OFF)
                                .select(getWifiVerboseActionId())
                                .build()));
    }

    private Layout.Header getDebugAppHeader() {
        final List<ApplicationInfo> pkgs = getPackageManager().getInstalledApplications(0);
        mPackageInfoList.clear();
        for (final ApplicationInfo ai : pkgs) {
            if (ai.uid == Process.SYSTEM_UID) {
                continue;
            }
            // On a user build, we only allow debugging of apps that
            // are marked as debuggable. Otherwise (for platform development)
            // we allow all apps.
            if ((ai.flags & ApplicationInfo.FLAG_DEBUGGABLE) == 0
                    && "user".equals(Build.TYPE)) {
                continue;
            }
            MyApplicationInfo info = new MyApplicationInfo(ai,
                    ai.loadLabel(getPackageManager()).toString());
            mPackageInfoList.add(info);
        }
        Collections.sort(mPackageInfoList, sDisplayNameComparator);
        mPackageInfoList.add(0, new MyApplicationInfo(null, getString(R.string.no_application)));

        final Layout.SelectionGroup.Builder selectionGroupBuilder =
                new Layout.SelectionGroup.Builder();

        final String debugApp = getDebugApp();
        int selection = ACTION_DEBUG_APP_BASE;
        int id = ACTION_DEBUG_APP_BASE;
        for (final MyApplicationInfo app : mPackageInfoList) {
            if (app.info != null) {
                selectionGroupBuilder.add(app.label.toString(), app.info.packageName, id);
                if (TextUtils.equals(debugApp, app.info.packageName)) {
                    selection = id;
                }
            } else {
                selectionGroupBuilder.add(app.label.toString(), null, id);
            }
            app.id = id;
            id++;
        }

        selectionGroupBuilder.select(selection);

        return new Layout.Header.Builder(getResources())
                .title(R.string.system_select_debug_app)
                .build()
                .setSelectionGroup(selectionGroupBuilder.build());
    }

    private Layout.Header getInputHeader() {
        final Resources res = getResources();

        final String onTitle = getString(R.string.action_on_description);
        final String offTitle = getString(R.string.action_off_description);

        return new Layout.Header.Builder(res)
                .title(R.string.system_input)
                .build()
                .add(new Layout.Header.Builder(res)
                        .title(R.string.system_show_touches)
                        .build()
                        .setSelectionGroup(new Layout.SelectionGroup.Builder(2)
                                .add(onTitle, null, ACTION_SHOW_TOUCHES_ON)
                                .add(offTitle, null, ACTION_SHOW_TOUCHES_OFF)
                                .select(getShowTouchesActionId())
                                .build()))
                .add(new Layout.Header.Builder(res)
                        .title(R.string.system_pointer_location)
                        .build()
                        .setSelectionGroup(new Layout.SelectionGroup.Builder(2)
                                .add(onTitle, null, ACTION_POINTER_LOCATION_ON)
                                .add(offTitle, null, ACTION_POINTER_LOCATION_OFF)
                                .select(getPointerLocationActionId())
                                .build()));
    }

    private Layout.Header getDrawingHeader() {
        final Resources res = getResources();

        final String onTitle = getString(R.string.action_on_description);
        final String offTitle = getString(R.string.action_off_description);

        return new Layout.Header.Builder(res)
                .title(R.string.system_drawing)
                .build()
                .add(new Layout.Header.Builder(res)
                        .title(R.string.system_show_layout_bounds)
                        .detailedDescription(R.string.system_desc_show_layout_bounds)
                        .build()
                        .setSelectionGroup(new Layout.SelectionGroup.Builder(2)
                                .add(onTitle, null, ACTION_LAYOUT_BOUNDS_ON)
                                .add(offTitle, null, ACTION_LAYOUT_BOUNDS_OFF)
                                .select(getLayoutBoundsActionId())
                                .build()))
                .add(new Layout.Header.Builder(res)
                        .title(R.string.system_show_gpu_view_updates)
                        .detailedDescription(R.string.system_desc_show_gpu_view_updates)
                        .build()
                        .setSelectionGroup(new Layout.SelectionGroup.Builder(2)
                                .add(onTitle, null, ACTION_GPU_VIEW_UPDATES_ON)
                                .add(offTitle, null, ACTION_GPU_VIEW_UPDATES_OFF)
                                .select(getGpuViewUpdatesActionId())
                                .build()))
                .add(new Layout.Header.Builder(res)
                        .title(R.string.system_show_gpu_overdraw)
                        .detailedDescription(R.string.system_desc_show_gpu_overdraw)
                        .build()
                        .setSelectionGroup(new Layout.SelectionGroup.Builder(2)
                                .add(getString(R.string.system_hw_overdraw_off), null,
                                        ACTION_GPU_OVERDRAW_OFF)
                                .add(getString(R.string.system_hw_overdraw_areas), null,
                                        ACTION_GPU_OVERDRAW_AREAS)
                                .add(getString(R.string.system_hw_overdraw_counter), null,
                                        ACTION_GPU_OVERDRAW_COUNTER)
                                .select(getGpuOverdrawActionId())
                                .build()))
                .add(new Layout.Header.Builder(res)
                        .title(R.string.system_show_hardware_layer)
                        .detailedDescription(R.string.system_desc_show_hardware_layer)
                        .build()
                        .setSelectionGroup(new Layout.SelectionGroup.Builder(2)
                                .add(onTitle, null, ACTION_HARDWARE_LAYER_ON)
                                .add(offTitle, null, ACTION_HARDWARE_LAYER_OFF)
                                .select(getHardwareLayerActionId())
                                .build()))
                .add(new Layout.Header.Builder(res)
                        .title(R.string.system_show_surface_updates)
                        .detailedDescription(R.string.system_desc_show_surface_updates)
                        .build()
                        .setSelectionGroup(new Layout.SelectionGroup.Builder(2)
                                .add(onTitle, null, ACTION_SURFACE_UPDATES_ON)
                                .add(offTitle, null, ACTION_SURFACE_UPDATES_OFF)
                                .select(getSurfaceUpdatesActionId())
                                .build()))
                .add(getAnimationScaleHeader(R.string.system_window_animation_scale,
                        ACTION_WINDOW_ANIMATION_SCALE_BASE,
                        getAnimationScaleValueIndex(INDEX_WINDOW_ANIMATION_SCALE)))
                .add(getAnimationScaleHeader(R.string.system_transition_animation_scale,
                        ACTION_TRANSITION_ANIMATION_SCALE_BASE,
                        getAnimationScaleValueIndex(INDEX_TRANSITION_ANIMATION_SCALE)))
                .add(getAnimationScaleHeader(R.string.system_animator_duration_scale,
                        ACTION_ANIMATOR_DURATION_BASE,
                        getAnimationScaleValueIndex(INDEX_ANIMATOR_DURATION_SCALE)));
    }

    private Layout.Header getAnimationScaleHeader(@StringRes int headerTitle, int baseId,
            int selectionIndex) {
        final Resources res = getResources();
        final String[] titles = res.getStringArray(R.array.animation_scale_entries);
        final Layout.SelectionGroup.Builder builder =
                new Layout.SelectionGroup.Builder(titles.length);
        int id = baseId;
        for (final String title : titles) {
            builder.add(title, null, id++);
        }
        builder.select(selectionIndex + baseId);
        return new Layout.Header.Builder(res)
                .title(headerTitle)
                .build()
                .setSelectionGroup(builder.build());
    }

    private Layout.Header getMonitoringHeader() {
        final Resources res = getResources();

        final String onTitle = getString(R.string.action_on_description);
        final String offTitle = getString(R.string.action_off_description);

        return new Layout.Header.Builder(res)
                .title(R.string.system_monitoring)
                .build()
                .add(new Layout.Header.Builder(res)
                        .title(R.string.system_strict_mode_enabled)
                        .detailedDescription(R.string.system_desc_strict_mode_enabled)
                        .build()
                        .setSelectionGroup(new Layout.SelectionGroup.Builder(2)
                                .add(onTitle, null, ACTION_STRICT_MODE_ON)
                                .add(offTitle, null, ACTION_STRICT_MODE_OFF)
                                .select(getStrictModeActionId())
                                .build()))
                .add(new Layout.Header.Builder(res)
                        .title(R.string.system_show_cpu_usage)
                        .detailedDescription(R.string.system_desc_show_cpu_usage)
                        .build()
                        .setSelectionGroup(new Layout.SelectionGroup.Builder(2)
                                .add(onTitle, null, ACTION_CPU_USAGE_ON)
                                .add(offTitle, null, ACTION_CPU_USAGE_OFF)
                                .select(getCpuUsageActionId())
                                .build()))
                .add(new Layout.Header.Builder(res)
                        .title(R.string.system_profile_gpu_rendering)
                        .detailedDescription(R.string.system_desc_profile_gpu_rendering)
                        .build()
                        .setSelectionGroup(new Layout.SelectionGroup.Builder(3)
                                .add(getString(R.string.track_frame_time_off), null,
                                        ACTION_FRAME_TIME_OFF)
                                .add(getString(R.string.track_frame_time_bars), null,
                                        ACTION_FRAME_TIME_BARS)
                                .add(getString(R.string.track_frame_time_gfxinfo), null,
                                        ACTION_FRAME_TIME_GFXINFO)
                                .select(getFrameTimeActionId())
                                .build()))
                .add(new Layout.Header.Builder(res)
                        .title(R.string.system_enable_traces)
                        .build()
                        .setSelectionGroup(new Layout.SelectionGroup.Builder(4)
                                .add(getString(R.string.enable_opengl_traces_none), null,
                                        ACTION_OPENGL_TRACES_NONE)
                                .add(getString(R.string.enable_opengl_traces_logcat), null,
                                        ACTION_OPENGL_TRACES_LOGCAT)
                                .add(getString(R.string.enable_opengl_traces_systrace), null,
                                        ACTION_OPENGL_TRACES_SYSTRACE)
                                .add(getString(R.string.enable_opengl_traces_error), null,
                                        ACTION_OPENGL_TRACES_ERROR)
                                .select(getOpenglTracesActionId())
                                .build()));
    }

    private Layout.Header getAppsHeader() {
        final Resources res = getResources();

        final String onTitle = getString(R.string.action_on_description);
        final String offTitle = getString(R.string.action_off_description);

        return new Layout.Header.Builder(res)
                .title(R.string.system_apps)
                .build()
                .add(new Layout.Header.Builder(res)
                        .title(R.string.system_dont_keep_activities)
                        .build()
                        .setSelectionGroup(new Layout.SelectionGroup.Builder(2)
                                .add(onTitle, null, ACTION_DONT_KEEP_ACTIVITIES_ON)
                                .add(offTitle, null, ACTION_DONT_KEEP_ACTIVITIES_OFF)
                                .select(getDontKeepActivitiesOnActionId())
                                .build()))
                .add(new Layout.Header.Builder(res)
                        .title(R.string.system_background_process_limit)
                        .build()
                        .setSelectionGroup(new Layout.SelectionGroup.Builder(6)
                                .add(getString(R.string.app_process_limit_standard), null,
                                        ACTION_APP_PROCESS_LIMIT_STANDARD)
                                .add(getString(R.string.app_process_limit_zero), null,
                                        ACTION_APP_PROCESS_LIMIT_ZERO)
                                .add(getString(R.string.app_process_limit_one), null,
                                        ACTION_APP_PROCESS_LIMIT_ONE)
                                .add(getString(R.string.app_process_limit_two), null,
                                        ACTION_APP_PROCESS_LIMIT_TWO)
                                .add(getString(R.string.app_process_limit_three), null,
                                        ACTION_APP_PROCESS_LIMIT_THREE)
                                .add(getString(R.string.app_process_limit_four), null,
                                        ACTION_APP_PROCESS_LIMIT_FOUR)
                                .select(getAppProcessLimitActionId())
                                .build()))
                .add(new Layout.Header.Builder(res)
                        .title(R.string.system_show_all_anrs)
                        .build()
                        .setSelectionGroup(new Layout.SelectionGroup.Builder(2)
                                .add(onTitle, null, ACTION_ALL_ANRS_ON)
                                .add(offTitle, null, ACTION_ALL_ANRS_OFF)
                                .select(getAllAnrsActionId())
                                .build()));
    }

    private Layout.Header getCmHeader() {
        final Resources res = getResources();

        final String onTitle = getString(R.string.action_on_description);
        final String offTitle = getString(R.string.action_off_description);

        final Layout.Header header = new Layout.Header.Builder(res)
                .title(R.string.system_cm)
                .build();

                if (getGlobalSettingBoolean(Settings.Global.ADB_ENABLED) == true) {
                    header.add(new Layout.Header.Builder(res)
                            .title(R.string.adb_over_network)
                            .detailedDescription(R.string.adb_over_network_summary)
                            .build()
                            .setSelectionGroup(new Layout.SelectionGroup.Builder(2)
                                    .add(onTitle, null, ACTION_ADB_OVER_NETWORK_ON)
                                    .add(offTitle, null, ACTION_ADB_OVER_NETWORK_OFF)
                                    .select(getAdbOverNetworkId())
                                    .build()));
                }
                header.add(new Layout.Header.Builder(res)
                        .title(R.string.advanced_reboot_title)
                        .detailedDescription(R.string.advanced_reboot_summary)
                        .build()
                        .setSelectionGroup(new Layout.SelectionGroup.Builder(2)
                                .add(onTitle, null, ACTION_ADVANCED_REBOOT_ON)
                                .add(offTitle, null, ACTION_ADVANCED_REBOOT_OFF)
                                .select(getAdvancedRebootId())
                                .build()));
                if (isPackageInstalled(this, TERMINAL_APP_PACKAGE)) {
                    header.add(new Layout.Header.Builder(res)
                            .title(R.string.enable_terminal_title)
                            .detailedDescription(R.string.enable_terminal_summary)
                            .build()
                            .setSelectionGroup(new Layout.SelectionGroup.Builder(2)
                                    .add(onTitle, null, ACTION_ENABLE_TERMINAL_ON)
                                    .add(offTitle, null, ACTION_ENABLE_TERMINAL_OFF)
                                    .select(getEnableTerminalId())
                                    .build()));
                }
                if (!"user".equals(Build.TYPE)) {
                    header.add(new Layout.Header.Builder(res)
                            .title(R.string.root_access)
                            .detailedDescription(R.string.root_access_warning_title)
                            .build()
                            .setSelectionGroup(new Layout.SelectionGroup.Builder(4)
                                    .add(getString(R.string.root_access_none), null,
                                            ACTION_ROOT_ACCESS_NONE)
                                    .add(getString(R.string.root_access_apps), null,
                                            ACTION_ROOT_ACCESS_APPS)
                                    .add(getString(R.string.root_access_adb), null,
                                            ACTION_ROOT_ACCESS_ADB)
                                    .add(getString(R.string.root_access_all), null,
                                            ACTION_ROOT_ACCESS_ALL)
                                    .select(getRootAccessId())
                                    .build()));
                }
        return header;
    }

    @Override
    public void onActionClicked(Layout.Action action) {
        switch (action.getId() & ACTION_BASE_MASK) {
            case ACTION_GENERAL_BASE:
                onGeneralActionClicked(action);
                break;
            case ACTION_DEBUGGING_BASE:
                onDebuggingActionClicked(action);
                break;
            case ACTION_DEBUG_APP_BASE:
                onDebugAppActionClicked(action);
                break;
            case ACTION_INPUT_BASE:
                onInputActionClicked(action);
                break;
            case ACTION_DRAWING_BASE:
                onDrawingActionClicked(action);
                break;
            case ACTION_WINDOW_ANIMATION_SCALE_BASE:
                onWindowAnimationScaleActionClicked(action);
                break;
            case ACTION_TRANSITION_ANIMATION_SCALE_BASE:
                onTransitionAnimationScaleActionClicked(action);
                break;
            case ACTION_ANIMATOR_DURATION_BASE:
                onAnimatorDurationActionClicked(action);
                break;
            case ACTION_MONITORING_BASE:
                onMonitoringActionClicked(action);
                break;
            case ACTION_APPS_BASE:
                onAppsActionClicked(action);
                break;
            case ACTION_CM_BASE:
                onCmActionClicked(action);
                break;
        }
    }

    private void onGeneralActionClicked(Layout.Action action) {
        final int id = action.getId();
        switch (id) {
            case ACTION_STAY_AWAKE_ON:
                setStayAwake(true);
                break;
            case ACTION_STAY_AWAKE_OFF:
                setStayAwake(false);
                break;
            case ACTION_HDCP_ALWAYS:
            case ACTION_HDCP_DRM:
            case ACTION_HDCP_NEVER:
                setHdcpCheckingByAction(id);
                break;
            case ACTION_HDMI_OPT_BEST_FRAME:
            case ACTION_HDMI_OPT_BEST_RES:
                setHdmiOptimizationByAction(id);
                tryReboot();
                break;
            case ACTION_HCI_LOGGING_ON:
                setHciLogging(true);
                break;
            case ACTION_HCI_LOGGING_OFF:
                setHciLogging(false);
                break;
        }
    }

    private void onDebuggingActionClicked(Layout.Action action) {
        switch (action.getId()) {
            case ACTION_USB_DEBUGGING_ON:
                setUsbDebugging(true);
                break;
            case ACTION_USB_DEBUGGING_OFF:
                setUsbDebugging(false);
                break;
            case ACTION_MOCK_LOCATIONS_ON:
                setMockLocations(true);
                break;
            case ACTION_MOCK_LOCATIONS_OFF:
                setMockLocations(false);
                break;
            case ACTION_WAIT_FOR_DEBUGGER_ON:
                setWaitForDebugger(true);
                break;
            case ACTION_WAIT_FOR_DEBUGGER_OFF:
                setWaitForDebugger(false);
                break;
            case ACTION_VERIFY_APPS_ON:
                setVerifyApps(true);
                break;
            case ACTION_VERIFY_APPS_OFF:
                setVerifyApps(false);
                break;
            case ACTION_WIFI_VERBOSE_ON:
                setWifiVerbose(true);
                break;
            case ACTION_WIFI_VERBOSE_OFF:
                setWifiVerbose(false);
                break;
        }
    }

    private void onDebugAppActionClicked(Layout.Action action) {
        final int id = action.getId();
        for (MyApplicationInfo myInfo : mPackageInfoList) {
            if (myInfo.id == id) {
                if (myInfo.info != null) {
                    setDebugApp(myInfo.info.packageName);
                } else {
                    setDebugApp(null);
                }
                return;
            }
        }
        setDebugApp(null);
    }

    private void onInputActionClicked(Layout.Action action) {
        switch (action.getId()) {
            case ACTION_SHOW_TOUCHES_ON:
                setShowTouches(true);
                break;
            case ACTION_SHOW_TOUCHES_OFF:
                setShowTouches(false);
                break;
            case ACTION_POINTER_LOCATION_ON:
                setPointerLocation(true);
                break;
            case ACTION_POINTER_LOCATION_OFF:
                setPointerLocation(false);
                break;
        }
    }

    private void onDrawingActionClicked(Layout.Action action) {
        final int id = action.getId();
        switch (id) {
            case ACTION_LAYOUT_BOUNDS_ON:
                setLayoutBounds(true);
                break;
            case ACTION_LAYOUT_BOUNDS_OFF:
                setLayoutBounds(false);
                break;
            case ACTION_GPU_VIEW_UPDATES_ON:
                setGpuViewUpdates(true);
                break;
            case ACTION_GPU_VIEW_UPDATES_OFF:
                setGpuViewUpdates(false);
                break;
            case ACTION_GPU_OVERDRAW_OFF:
            case ACTION_GPU_OVERDRAW_AREAS:
            case ACTION_GPU_OVERDRAW_COUNTER:
                setGpuOverdrawByAction(id);
                break;
            case ACTION_HARDWARE_LAYER_ON:
                setHardwareLayer(true);
                break;
            case ACTION_HARDWARE_LAYER_OFF:
                setHardwareLayer(false);
                break;
            case ACTION_SURFACE_UPDATES_ON:
                setSurfaceUpdates(true);
                break;
            case ACTION_SURFACE_UPDATES_OFF:
                setSurfaceUpdates(false);
                break;
        }
    }

    private void onWindowAnimationScaleActionClicked(Layout.Action action) {
        setAnimationScale(INDEX_WINDOW_ANIMATION_SCALE, action.getId() & ~ACTION_BASE_MASK);
    }

    private void onTransitionAnimationScaleActionClicked(Layout.Action action) {
        setAnimationScale(INDEX_TRANSITION_ANIMATION_SCALE, action.getId() & ~ACTION_BASE_MASK);
    }

    private void onAnimatorDurationActionClicked(Layout.Action action) {
        setAnimationScale(INDEX_ANIMATOR_DURATION_SCALE, action.getId() & ~ACTION_BASE_MASK);
    }

    private void onMonitoringActionClicked(Layout.Action action) {
        final int id = action.getId();
        switch (id) {
            case ACTION_STRICT_MODE_ON:
                setStrictMode(true);
                break;
            case ACTION_STRICT_MODE_OFF:
                setStrictMode(false);
                break;
            case ACTION_CPU_USAGE_ON:
                setCpuUsage(true);
                break;
            case ACTION_CPU_USAGE_OFF:
                setCpuUsage(false);
                break;
            case ACTION_FRAME_TIME_OFF:
            case ACTION_FRAME_TIME_BARS:
            case ACTION_FRAME_TIME_GFXINFO:
                setFrameTimeByAction(id);
                break;
            case ACTION_OPENGL_TRACES_NONE:
            case ACTION_OPENGL_TRACES_LOGCAT:
            case ACTION_OPENGL_TRACES_SYSTRACE:
            case ACTION_OPENGL_TRACES_ERROR:
                setOpenglTracesByAction(id);
                break;
        }
    }

    private void onAppsActionClicked(Layout.Action action) {
        final int id = action.getId();
        switch (id) {
            case ACTION_DONT_KEEP_ACTIVITIES_ON:
                setDontKeepActivities(true);
                break;
            case ACTION_DONT_KEEP_ACTIVITIES_OFF:
                setDontKeepActivities(false);
                break;
            case ACTION_APP_PROCESS_LIMIT_STANDARD:
            case ACTION_APP_PROCESS_LIMIT_ZERO:
            case ACTION_APP_PROCESS_LIMIT_ONE:
            case ACTION_APP_PROCESS_LIMIT_TWO:
            case ACTION_APP_PROCESS_LIMIT_THREE:
            case ACTION_APP_PROCESS_LIMIT_FOUR:
                setAppProcessLimitByAction(id);
                break;
            case ACTION_ALL_ANRS_ON:
                setAllAnrs(true);
                break;
            case ACTION_ALL_ANRS_OFF:
                setAllAnrs(false);
                break;
        }
    }

    private void onCmActionClicked(Layout.Action action) {
        final int id = action.getId();
        switch (id) {
            case ACTION_ROOT_ACCESS_NONE:
            case ACTION_ROOT_ACCESS_APPS:
            case ACTION_ROOT_ACCESS_ADB:
            case ACTION_ROOT_ACCESS_ALL:
                setRootAccessByAction(id);
                break;
            case ACTION_ENABLE_TERMINAL_ON:
                setEnableTerminal(true);
                break;
            case ACTION_ENABLE_TERMINAL_OFF:
                setEnableTerminal(false);
                break;
            case ACTION_ADVANCED_REBOOT_ON:
                setAdvancedReboot(true);
                break;
            case ACTION_ADVANCED_REBOOT_OFF:
                setAdvancedReboot(false);
                break;
            case ACTION_ADB_OVER_NETWORK_ON:
            case ACTION_ADB_OVER_NETWORK_OFF:
                setAdbOverNetwork(id);
                break;
        }
    }

    private String getDebugApp() {
        return Settings.Global.getString(getContentResolver(), Settings.Global.DEBUG_APP);
    }

    private void setDebugApp(String debugApp){
        boolean waitForDebugger = getGlobalSettingBoolean(Settings.Global.WAIT_FOR_DEBUGGER);
        try {
            ActivityManagerNative.getDefault().setDebugApp(debugApp, waitForDebugger, true);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void setShowUpdatesOption(boolean enable) {
        try {
            IBinder flinger = ServiceManager.getService("SurfaceFlinger");
            if (flinger != null) {
                Parcel data = Parcel.obtain();
                data.writeInterfaceToken("android.ui.ISurfaceComposer");
                final int showUpdates = enable ? 1 : 0;
                data.writeInt(showUpdates);
                flinger.transact(1002, data, null, 0);
                data.recycle();
            }
        } catch (RemoteException ex) {
            if (DEBUG) {
                Log.d(TAG, "setShowUpdatesOption", ex);
            }
        }
    }

    private static boolean getShowUpdatesOption() {
        // magic communication with surface flinger.
        int showUpdates = 0;
        try {
            IBinder flinger = ServiceManager.getService("SurfaceFlinger");
            if (flinger != null) {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                data.writeInterfaceToken("android.ui.ISurfaceComposer");
                flinger.transact(1010, data, reply, 0);
                @SuppressWarnings("unused")
                int showCpu = reply.readInt();
                @SuppressWarnings("unused")
                int enableGL = reply.readInt();
                showUpdates = reply.readInt();
                reply.recycle();
                data.recycle();
            }
        } catch (RemoteException ex) {
            if (DEBUG) {
                Log.d(TAG, "getShowUpdatesOption", ex);
            }
        }
        return showUpdates > 0;
    }

    private void setAnimationScale(int which, int index) {
        final String[] values = getResources().getStringArray(R.array.animation_scale_values);
        float scale;
        try {
            scale = Float.parseFloat(values[index]);
        } catch (NumberFormatException e) {
            scale = 1.0f;
        }
        try {
            mWindowManager.setAnimationScale(which, scale);
        } catch (RemoteException e) {
            if (DEBUG) {
                Log.d(TAG, "setAnimationScaleOption", e);
            }
        }
    }

    private float getAnimationScaleValue(int which) {
        float scale = 0;
        try {
            scale = mWindowManager.getAnimationScale(which);
        } catch (RemoteException e) {
            if (DEBUG) {
                Log.d(TAG, "getAnimationScaleValue", e);
            }
        }
        return scale;
    }

    private int getAnimationScaleValueIndex(int which) {
        final String currentScale = Float.toString(getAnimationScaleValue(which));
        final String[] scales = getResources().getStringArray(R.array.animation_scale_values);
        for (int i = 0; i < scales.length; i++) {
            if (TextUtils.equals(currentScale, scales[i])) {
                return i;
            }
        }
        return 2;
    }

    private int getAppProcessLimit() {
        try {
            return ActivityManagerNative.getDefault().getProcessLimit();
        } catch (RemoteException e) {
            if (DEBUG) {
                Log.d(TAG, "getAppProcessLimit", e);
            }
        }
        return 0;
    }

    private void setAppProcessLimit(int limit) {
        try {
            ActivityManagerNative.getDefault().setProcessLimit(limit);
        } catch (RemoteException e) {
            if (DEBUG) {
                Log.d(TAG, "setAppProcessLimit", e);
            }
        }
    }

    private boolean getGlobalSettingBoolean(String setting) {
        return Settings.Global.getInt(getContentResolver(), setting, 0) != 0;
    }

    private void setGlobalSettingBoolean(String setting, boolean value) {
        Settings.Global.putInt(getContentResolver(), setting, value ? 1 : 0);
    }

    private boolean getSecureSettingBoolean(String setting) {
        return Settings.Secure.getInt(getContentResolver(), setting, 0) != 0;
    }

    private void setSecureSettingBoolean(String setting, boolean value) {
        Settings.Secure.putInt(getContentResolver(), setting, value ? 1 : 0);
    }

    private boolean getSecureCMSettingBoolean(String setting) {
        return CMSettings.Secure.getInt(getContentResolver(), setting, 0) != 0;
    }

    private void setSecureCMSettingBoolean(String setting, boolean value) {
        CMSettings.Secure.putInt(getContentResolver(), setting, value ? 1 : 0);
    }

    private int getStayAwakeActionId() {
        return getGlobalSettingBoolean(Settings.Global.STAY_ON_WHILE_PLUGGED_IN) ?
                ACTION_STAY_AWAKE_ON : ACTION_STAY_AWAKE_OFF;
    }

    private void setStayAwake(boolean value) {
        setGlobalSettingBoolean(Settings.Global.STAY_ON_WHILE_PLUGGED_IN, value);
    }

    private int getHdcpCheckingActionId() {
        switch (SystemProperties.get(HDCP_CHECKING_PROPERTY, "drm-only")) {
            case "never":
                return ACTION_HDCP_NEVER;
            case "always":
                return ACTION_HDCP_ALWAYS;
            case "drm-only":
            default:
                return ACTION_HDCP_DRM;
        }
    }

    private void setHdcpCheckingByAction(int action) {
        switch (action) {
            case ACTION_HDCP_DRM:
                SettingsHelper.setSystemProperties(HDCP_CHECKING_PROPERTY, "drm-only");
                break;
            case ACTION_HDCP_ALWAYS:
                SettingsHelper.setSystemProperties(HDCP_CHECKING_PROPERTY, "always");
                break;
            case ACTION_HDCP_NEVER:
                SettingsHelper.setSystemProperties(HDCP_CHECKING_PROPERTY, "never");
                break;
        }
    }

    private int getHdmiOptimizationActionId() {
        switch (SettingsHelper.getSystemProperties(HDMI_OPTIMIZATION_PROPERTY)) {
            case "1080p":
                return ACTION_HDMI_OPT_BEST_FRAME;
            case "Max":
            default:
                return ACTION_HDMI_OPT_BEST_RES;
        }
    }

    private void setHdmiOptimizationByAction(int action) {
        switch (action) {
            case ACTION_HDMI_OPT_BEST_FRAME:
                SettingsHelper.setSystemProperties(HDMI_OPTIMIZATION_PROPERTY, "1080p");
                break;
            case ACTION_HDMI_OPT_BEST_RES:
            default:
                SettingsHelper.setSystemProperties(HDMI_OPTIMIZATION_PROPERTY, "Max");
                break;
        }
    }

    private int getHciLoggingActionId() {
        return getSecureSettingBoolean(Settings.Secure.BLUETOOTH_HCI_LOG) ?
                ACTION_HCI_LOGGING_ON : ACTION_HCI_LOGGING_OFF;
    }

    private void setHciLogging(boolean value) {
        setSecureSettingBoolean(Settings.Secure.BLUETOOTH_HCI_LOG, value);
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        adapter.configHciSnoopLog(value);
    }

    private int getUsbDebuggingActionId() {
        return getGlobalSettingBoolean(Settings.Global.ADB_ENABLED) ?
                ACTION_USB_DEBUGGING_ON : ACTION_USB_DEBUGGING_OFF;
    }

    private void setUsbDebugging(boolean value) {
        setGlobalSettingBoolean(Settings.Global.ADB_ENABLED, value);
    }

    private int getMockLocationsActionId() {
        return getSecureSettingBoolean(Settings.Secure.ALLOW_MOCK_LOCATION) ?
                ACTION_MOCK_LOCATIONS_ON : ACTION_MOCK_LOCATIONS_OFF;
    }

    private void setMockLocations(boolean value) {
        setSecureSettingBoolean(Settings.Secure.ALLOW_MOCK_LOCATION, value);
    }

    private int getWaitForDebuggerActionId() {
        return getGlobalSettingBoolean(Settings.Global.WAIT_FOR_DEBUGGER) ?
                ACTION_WAIT_FOR_DEBUGGER_ON : ACTION_WAIT_FOR_DEBUGGER_OFF;
    }

    private void setWaitForDebugger(boolean value) {
        final String debugApp = getDebugApp();
        try {
            ActivityManagerNative.getDefault().setDebugApp(debugApp, value, true);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private int getVerifyAppsActionId() {
        return getGlobalSettingBoolean(Settings.Global.PACKAGE_VERIFIER_INCLUDE_ADB) ?
                ACTION_VERIFY_APPS_ON : ACTION_VERIFY_APPS_OFF;
    }

    private void setVerifyApps(boolean value) {
        setGlobalSettingBoolean(Settings.Global.PACKAGE_VERIFIER_INCLUDE_ADB, value);
    }

    private int getWifiVerboseActionId() {
        return mWifiManager.getVerboseLoggingLevel() > 0 ?
                ACTION_WIFI_VERBOSE_ON : ACTION_WIFI_VERBOSE_OFF;
    }

    private void setWifiVerbose(boolean value) {
        mWifiManager.enableVerboseLogging(value ? 1 : 0);
    }

    private int getShowTouchesActionId() {
        return getGlobalSettingBoolean(Settings.System.SHOW_TOUCHES) ?
                ACTION_SHOW_TOUCHES_ON : ACTION_SHOW_TOUCHES_OFF;
    }

    private void setShowTouches(boolean value) {
        setGlobalSettingBoolean(Settings.System.SHOW_TOUCHES, value);
    }

    private int getPointerLocationActionId() {
        return getGlobalSettingBoolean(Settings.System.POINTER_LOCATION) ?
                ACTION_POINTER_LOCATION_ON : ACTION_POINTER_LOCATION_OFF;
    }

    private void setPointerLocation(boolean value) {
        setGlobalSettingBoolean(Settings.System.POINTER_LOCATION, value);
    }

    private int getLayoutBoundsActionId() {
        return SystemProperties.getBoolean(View.DEBUG_LAYOUT_PROPERTY, false) ?
                ACTION_LAYOUT_BOUNDS_ON : ACTION_LAYOUT_BOUNDS_OFF;
    }

    private void setLayoutBounds(boolean value) {
        SettingsHelper.setSystemProperties(View.DEBUG_LAYOUT_PROPERTY, value);
    }

    private int getGpuViewUpdatesActionId() {
        return SystemProperties.getBoolean(HardwareRenderer.DEBUG_DIRTY_REGIONS_PROPERTY, false) ?
                ACTION_GPU_VIEW_UPDATES_ON : ACTION_GPU_VIEW_UPDATES_OFF;
    }

    private void setGpuViewUpdates(boolean value) {
        SettingsHelper.setSystemProperties(HardwareRenderer.DEBUG_DIRTY_REGIONS_PROPERTY, value);
    }

    private int getGpuOverdrawActionId() {
        switch (SystemProperties.get(HardwareRenderer.DEBUG_OVERDRAW_PROPERTY, "false")) {
            case "show":
                return ACTION_GPU_OVERDRAW_AREAS;
            case "count":
                return ACTION_GPU_OVERDRAW_COUNTER;
            case "false":
            default:
                return ACTION_GPU_OVERDRAW_OFF;
        }
    }

    private void setGpuOverdrawByAction(int action) {
        switch (action) {
            case ACTION_GPU_OVERDRAW_AREAS:
                SettingsHelper.setSystemProperties(HardwareRenderer.DEBUG_OVERDRAW_PROPERTY,
                        "show");
                break;
            case ACTION_GPU_OVERDRAW_COUNTER:
                SettingsHelper.setSystemProperties(HardwareRenderer.DEBUG_OVERDRAW_PROPERTY,
                        "count");
                break;
            case ACTION_GPU_OVERDRAW_OFF:
                SettingsHelper.setSystemProperties(HardwareRenderer.DEBUG_OVERDRAW_PROPERTY,
                        "false");
                break;

        }
    }

    private int getHardwareLayerActionId() {
        return SystemProperties.getBoolean(
                HardwareRenderer.DEBUG_SHOW_LAYERS_UPDATES_PROPERTY, false) ?
                ACTION_HARDWARE_LAYER_ON : ACTION_HARDWARE_LAYER_OFF;
    }

    private void setHardwareLayer(boolean value) {
        SettingsHelper.setSystemProperties(HardwareRenderer.DEBUG_SHOW_LAYERS_UPDATES_PROPERTY,
                value);
    }

    private int getSurfaceUpdatesActionId() {
        return getShowUpdatesOption() ? ACTION_SURFACE_UPDATES_ON : ACTION_SURFACE_UPDATES_OFF;
    }

    private void setSurfaceUpdates(boolean value) {
        setShowUpdatesOption(value);
    }

    private int getStrictModeActionId() {
        return SystemProperties.getBoolean(StrictMode.VISUAL_PROPERTY, false) ?
                ACTION_STRICT_MODE_ON : ACTION_STRICT_MODE_OFF;
    }

    private void setStrictMode(boolean value) {
        try {
            mWindowManager.setStrictModeVisualIndicatorPreference(value ? "1" : "");
        } catch (RemoteException e) {
            if (DEBUG) {
                Log.d(TAG, "setStrictModeVisualOptions", e);
            }
        }
    }

    private int getCpuUsageActionId() {
        return getGlobalSettingBoolean(Settings.Global.SHOW_PROCESSES) ?
                ACTION_CPU_USAGE_ON : ACTION_CPU_USAGE_OFF;
    }

    private void setCpuUsage(boolean value) {
        setGlobalSettingBoolean(Settings.Global.SHOW_PROCESSES, value);
        Intent service = (new Intent()).setClassName("com.android.systemui",
                "com.android.systemui.LoadAverageService");
        if (value) {
            startService(service);
        } else {
            stopService(service);
        }
    }

    private int getFrameTimeActionId() {
        switch (SystemProperties.get(HardwareRenderer.PROFILE_PROPERTY, "false")) {
            case "visual_bars":
                return ACTION_FRAME_TIME_BARS;
            case "true":
                return ACTION_FRAME_TIME_GFXINFO;
            case "false":
            default:
                return ACTION_FRAME_TIME_OFF;
        }
    }

    private void setFrameTimeByAction(int action) {
        switch (action) {
            case ACTION_FRAME_TIME_BARS:
                SettingsHelper.setSystemProperties(HardwareRenderer.PROFILE_PROPERTY,
                        "visual_bars");
                break;
            case ACTION_FRAME_TIME_GFXINFO:
                SettingsHelper.setSystemProperties(HardwareRenderer.PROFILE_PROPERTY, "true");
                break;
            case ACTION_FRAME_TIME_OFF:
                SettingsHelper.setSystemProperties(HardwareRenderer.PROFILE_PROPERTY, "false");
                break;
        }
    }

    private int getOpenglTracesActionId() {
        switch (SystemProperties.get(OPENGL_TRACES_PROPERTY, "0")) {
            case "1":
                return ACTION_OPENGL_TRACES_LOGCAT;
            case "systrace":
                return ACTION_OPENGL_TRACES_SYSTRACE;
            case "error":
                return ACTION_OPENGL_TRACES_ERROR;
            case "0":
            default:
                return ACTION_OPENGL_TRACES_NONE;
        }
    }

    private void setOpenglTracesByAction(int action) {
        switch (action) {
            case ACTION_OPENGL_TRACES_LOGCAT:
                SettingsHelper.setSystemProperties(OPENGL_TRACES_PROPERTY, "1");
                break;
            case ACTION_OPENGL_TRACES_SYSTRACE:
                SettingsHelper.setSystemProperties(OPENGL_TRACES_PROPERTY, "systrace");
                break;
            case ACTION_OPENGL_TRACES_ERROR:
                SettingsHelper.setSystemProperties(OPENGL_TRACES_PROPERTY, "error");
                break;
            case ACTION_OPENGL_TRACES_NONE:
                SettingsHelper.setSystemProperties(OPENGL_TRACES_PROPERTY, "0");
                break;
        }
    }

    private int getDontKeepActivitiesOnActionId () {
        return getGlobalSettingBoolean(Settings.Global.ALWAYS_FINISH_ACTIVITIES) ?
             ACTION_DONT_KEEP_ACTIVITIES_ON : ACTION_DONT_KEEP_ACTIVITIES_OFF;
    }

    private void setDontKeepActivities(boolean value) {
        try {
            ActivityManagerNative.getDefault().setAlwaysFinish(value);
        } catch (RemoteException ex) {
            if (DEBUG) {
                Log.d(TAG, "DEVELOPER_APPS_DONT_KEEP_ACTIVITIES", ex);
            }
        }
    }

    private int getAppProcessLimitActionId() {
        switch (getAppProcessLimit()) {
            case 0:
                return ACTION_APP_PROCESS_LIMIT_ZERO;
            case 1:
                return ACTION_APP_PROCESS_LIMIT_ONE;
            case 2:
                return ACTION_APP_PROCESS_LIMIT_TWO;
            case 3:
                return ACTION_APP_PROCESS_LIMIT_THREE;
            case 4:
                return ACTION_APP_PROCESS_LIMIT_FOUR;
            case -1:
            default:
                return ACTION_APP_PROCESS_LIMIT_STANDARD;
        }
    }

    private void setAppProcessLimitByAction(int action) {
        switch (action) {
            case ACTION_APP_PROCESS_LIMIT_ZERO:
                setAppProcessLimit(0);
                break;
            case ACTION_APP_PROCESS_LIMIT_ONE:
                setAppProcessLimit(1);
                break;
            case ACTION_APP_PROCESS_LIMIT_TWO:
                setAppProcessLimit(2);
                break;
            case ACTION_APP_PROCESS_LIMIT_THREE:
                setAppProcessLimit(3);
                break;
            case ACTION_APP_PROCESS_LIMIT_FOUR:
                setAppProcessLimit(4);
                break;
            case ACTION_APP_PROCESS_LIMIT_STANDARD:
                setAppProcessLimit(-1);
                break;
        }
    }

    private int getAllAnrsActionId() {
        return getSecureSettingBoolean(Settings.Secure.ANR_SHOW_BACKGROUND) ?
                ACTION_ALL_ANRS_ON : ACTION_ALL_ANRS_OFF;
    }

    private void setAllAnrs(boolean value) {
        setSecureSettingBoolean(Settings.Secure.ANR_SHOW_BACKGROUND, value);
    }

    private int getRootAccessId() {
        switch (SystemProperties.get(ROOT_ACCESS_PROPERTY, "0")) {
            case "1":
                return ACTION_ROOT_ACCESS_APPS;
            case "2":
                return ACTION_ROOT_ACCESS_ADB;
            case "3":
                return ACTION_ROOT_ACCESS_ALL;
            case "0":
            default:
                return ACTION_ROOT_ACCESS_NONE;
        }
    }

    private void setRootAccessByAction(int action) {
        switch (action) {
            case ACTION_ROOT_ACCESS_APPS:
                setting = "1";
                SettingsHelper.setSystemProperties(ADB_ROOT_PROPERTY, "0");
                setGlobalSettingBoolean(Settings.Global.ADB_ENABLED, false);
                setGlobalSettingBoolean(Settings.Global.ADB_ENABLED, true);
                rootAccessWarning();
                break;
            case ACTION_ROOT_ACCESS_ADB:
                setting = "2";
                rootAccessWarning();
                break;
            case ACTION_ROOT_ACCESS_ALL:
                setting = "3";
                rootAccessWarning();
                break;
            case ACTION_ROOT_ACCESS_NONE:
                setting = "0";
                SettingsHelper.setSystemProperties(ROOT_ACCESS_PROPERTY, "0");
                SettingsHelper.setSystemProperties(ADB_ROOT_PROPERTY, "0");
                setGlobalSettingBoolean(Settings.Global.ADB_ENABLED, false);
                setGlobalSettingBoolean(Settings.Global.ADB_ENABLED, true);
                break;
        }
    }

    private void rootAccessWarning() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.root_access_warning_title)
                .setMessage(R.string.root_access_warning_message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // We are OK to enable root acess, trigger it
                 SettingsHelper.setSystemProperties(ROOT_ACCESS_PROPERTY, setting);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Enabling root access canceled, go back
                    }
                })
                .show();
    }

    /**
     * Enable Terminal app.
     */
    private int getEnableTerminalId() {
		final PackageManager pm = this.getPackageManager();
        return pm.getApplicationEnabledSetting(TERMINAL_APP_PACKAGE)
                == PackageManager.COMPONENT_ENABLED_STATE_ENABLED ?
                ACTION_ENABLE_TERMINAL_ON : ACTION_ENABLE_TERMINAL_OFF;
    }

    private void setEnableTerminal(boolean value) {
        final PackageManager pm = this.getPackageManager();
            pm.setApplicationEnabledSetting(TERMINAL_APP_PACKAGE,
                    value ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                            : PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, 0);
    }

    public static boolean isPackageInstalled(Context context, String pkg, boolean ignoreState) {
        if (pkg != null) {
            try {
                PackageInfo pi = context.getPackageManager().getPackageInfo(pkg, 0);
                if (!pi.applicationInfo.enabled && !ignoreState) {
                    return false;
                }
            } catch (NameNotFoundException e) {
                return false;
            }
        }

        return true;
    }

    public static boolean isPackageInstalled(Context context, String pkg) {
        return isPackageInstalled(context, pkg, true);
    }

    /**
     * Adb over TCP/IP.
     */
    private void updateAdbOverNetwork() {
        int port = CMSettings.Secure.getInt(getContentResolver(), CMSettings.Secure.ADB_PORT,  0);

        WifiInfo wifiInfo = null;

        if (port > 0) {
            IWifiManager wifiManager = IWifiManager.Stub.asInterface(
                    ServiceManager.getService(Context.WIFI_SERVICE));
            try {
                wifiInfo = wifiManager.getConnectionInfo();
            } catch (RemoteException e) {
                Log.e(TAG, "wifiManager, getConnectionInfo()", e);
            }
        }

        if (wifiInfo != null) {
            String hostAddress = NetworkUtils.intToInetAddress(
                    wifiInfo.getIpAddress()).getHostAddress();
            String summary = getString(R.string.adb_over_network_info) +hostAddress + ":" + String.valueOf(port);
            Log.i(TAG, "Adb Over Network is: " + summary);
            new AlertDialog.Builder(this)
                    .setTitle(R.string.adb_over_network)
                    .setMessage(summary)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Just need to display the adb over tcp/ip connect info
                        }
                    })
                    .show();
        }
    }

    private void adbNetworkWarning() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.adb_over_network)
                .setMessage(R.string.adb_over_network_warning)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // We are OK to enable adb over network, trigger it
                        CMSettings.Secure.putInt(getContentResolver(),
                                CMSettings.Secure.ADB_PORT, 5555);
                        updateAdbOverNetwork();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Enabling adb over network canceled
                    }
                })
                .show();
    }

    private int getAdbOverNetworkId() {
		int value = CMSettings.Secure.getInt(getContentResolver(),
                CMSettings.Secure.ADB_PORT,  0);
        switch (value) {
            case 5555:
                return ACTION_ADB_OVER_NETWORK_ON;
            case -1:
            default:
                return ACTION_ADB_OVER_NETWORK_OFF;
        }
    }

    private void setAdbOverNetwork(int action) {
        switch (action) {
            case ACTION_ADB_OVER_NETWORK_ON:
                adbNetworkWarning();
                break;
            case ACTION_ADB_OVER_NETWORK_OFF:
                CMSettings.Secure.putInt(getContentResolver(), CMSettings.Secure.ADB_PORT, -1);
                break;
        }
    }

    private void tryReboot() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.system_reboot_confirm)
                .setMessage(R.string.system_desc_reboot_confirm)
                .setPositiveButton(R.string.settings_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PowerManager manager = (PowerManager) getSystemService(POWER_SERVICE);
                        manager.reboot(null);
                    }
                })
                .setNegativeButton(R.string.settings_cancel, null).show();
    }

    private int getAdvancedRebootId() {
        return getSecureCMSettingBoolean(CMSettings.Secure.ADVANCED_REBOOT) ?
                ACTION_ADVANCED_REBOOT_ON : ACTION_ADVANCED_REBOOT_OFF;
    }

    private void setAdvancedReboot(boolean value) {
        setSecureCMSettingBoolean(CMSettings.Secure.ADVANCED_REBOOT, value);
    }
}
