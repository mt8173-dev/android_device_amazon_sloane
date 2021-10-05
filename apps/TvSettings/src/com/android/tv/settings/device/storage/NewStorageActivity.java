/*
 * Copyright (C) 2015 The Android Open Source Project
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
 * limitations under the License
 */

package com.android.tv.settings.device.storage;

import android.annotation.Nullable;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.UserInfo;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.UserManager;
import android.os.storage.DiskInfo;
import android.os.storage.StorageManager;
import android.os.storage.VolumeInfo;
import android.os.storage.VolumeRecord;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v17.leanback.widget.GuidanceStylist;
import android.support.v17.leanback.widget.GuidedAction;
import android.text.TextUtils;
import android.util.Log;

import com.android.tv.settings.R;
import com.android.tv.settings.device.StorageResetActivity;

import java.util.List;

public class NewStorageActivity extends Activity {

    private static final String TAG = "NewStorageActivity";

    private static final String ACTION_NEW_STORAGE =
            "com.android.tv.settings.device.storage.NewStorageActivity.NEW_STORAGE";
    private static final String ACTION_MISSING_STORAGE =
            "com.android.tv.settings.device.storage.NewStorageActivity.MISSING_STORAGE";

    public static Intent getNewStorageLaunchIntent(Context context, String volumeId,
            String diskId) {
        final Intent i = new Intent(context, NewStorageActivity.class);
        i.setAction(ACTION_NEW_STORAGE);
        i.putExtra(VolumeInfo.EXTRA_VOLUME_ID, volumeId);
        i.putExtra(DiskInfo.EXTRA_DISK_ID, diskId);
        return i;
    }

    public static Intent getMissingStorageLaunchIntent(Context context, String fsUuid) {
        final Intent i = new Intent(context, NewStorageActivity.class);
        i.setAction(ACTION_MISSING_STORAGE);
        i.putExtra(VolumeRecord.EXTRA_FS_UUID, fsUuid);
        return i;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            final String action = getIntent().getAction();

            if (TextUtils.equals(action, ACTION_NEW_STORAGE)) {
                final String volumeId = getIntent().getStringExtra(VolumeInfo.EXTRA_VOLUME_ID);
                final String diskId = getIntent().getStringExtra(DiskInfo.EXTRA_DISK_ID);
                if (TextUtils.isEmpty(volumeId) && TextUtils.isEmpty(diskId)) {
                    throw new IllegalStateException(
                            "NewStorageActivity launched without specifying new storage");
                }

                getFragmentManager().beginTransaction()
                        .add(android.R.id.content, NewStorageFragment.newInstance(volumeId, diskId))
                        .commit();
            } else if (TextUtils.equals(action, ACTION_MISSING_STORAGE)) {
                final String fsUuid = getIntent().getStringExtra(VolumeRecord.EXTRA_FS_UUID);
                if (TextUtils.isEmpty(fsUuid)) {
                    throw new IllegalStateException(
                            "NewStorageActivity launched without specifying missing storage");
                }

                getFragmentManager().beginTransaction()
                        .add(android.R.id.content, MissingStorageFragment.newInstance(fsUuid))
                        .commit();
            }
        }
    }

    public static class NewStorageFragment extends StorageGuidedStepFragment {

        private static final int ACTION_BROWSE = 1;
        private static final int ACTION_FORMAT_AS_PRIVATE = 2;
        private static final int ACTION_UNMOUNT = 3;
        private static final int ACTION_FORMAT_AS_PUBLIC = 4;

        private String mVolumeId;
        private String mDiskId;
        private String mDescription;

        public static NewStorageFragment newInstance(String volumeId, String diskId) {
            final Bundle b = new Bundle(1);
            b.putString(VolumeInfo.EXTRA_VOLUME_ID, volumeId);
            b.putString(DiskInfo.EXTRA_DISK_ID, diskId);
            final NewStorageFragment fragment = new NewStorageFragment();
            fragment.setArguments(b);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            StorageManager storageManager = getActivity().getSystemService(StorageManager.class);
            mVolumeId = getArguments().getString(VolumeInfo.EXTRA_VOLUME_ID);
            mDiskId = getArguments().getString(DiskInfo.EXTRA_DISK_ID);
            if (TextUtils.isEmpty(mVolumeId) && TextUtils.isEmpty(mDiskId)) {
                throw new IllegalStateException(
                        "NewStorageFragment launched without specifying new storage");
            }
            if (!TextUtils.isEmpty(mVolumeId)) {
                final VolumeInfo info = storageManager.findVolumeById(mVolumeId);
                mDescription = storageManager.getBestVolumeDescription(info);
            } else {
                final DiskInfo info = storageManager.findDiskById(mDiskId);
                mDescription = info.getDescription();
            }

            super.onCreate(savedInstanceState);
        }

        @Override
        public @NonNull GuidanceStylist.Guidance onCreateGuidance(Bundle savedInstanceState) {
            return new GuidanceStylist.Guidance(
                    getString(R.string.storage_new_title),
                    mDescription,
                    null,
                    getActivity().getDrawable(R.drawable.ic_settings_storage));
        }

        @Override
        public void onCreateActions(@NonNull List<GuidedAction> actions,
                Bundle savedInstanceState) {
            if (TextUtils.isEmpty(mVolumeId)) {
                actions.add(new GuidedAction.Builder()
                        .title(getString(R.string.storage_new_action_format_public))
                        .id(ACTION_FORMAT_AS_PUBLIC)
                        .build());
            } else {
                actions.add(new GuidedAction.Builder()
                        .title(getString(R.string.storage_new_action_browse))
                        .id(ACTION_BROWSE)
                        .build());
            }
            actions.add(new GuidedAction.Builder()
                    .title(getString(R.string.storage_new_action_adopt))
                    .id(ACTION_FORMAT_AS_PRIVATE)
                    .build());
            actions.add(new GuidedAction.Builder()
                    .title(getString(R.string.storage_new_action_eject))
                    .id(ACTION_UNMOUNT)
                    .build());
        }

        @Override
        public void onGuidedActionClicked(GuidedAction action) {
            switch ((int) action.getId()) {
                case ACTION_FORMAT_AS_PUBLIC:
                    startActivity(FormatActivity.getFormatAsPublicIntent(getActivity(), mDiskId));
                    break;
                case ACTION_BROWSE:
                    startActivity(new Intent(getActivity(), StorageResetActivity.class));
                    break;
                case ACTION_FORMAT_AS_PRIVATE:
                    startActivity(FormatActivity.getFormatAsPrivateIntent(getActivity(), mDiskId));
                    break;
                case ACTION_UNMOUNT:
                    // If we've mounted a volume, eject it. Otherwise just treat eject as cancel
                    if (!TextUtils.isEmpty(mVolumeId)) {
                        startActivity(
                                UnmountActivity.getIntent(getActivity(), mVolumeId, mDescription));
                    }
                    break;
            }
            getActivity().finish();
        }
    }

    public static class MissingStorageFragment extends StorageGuidedStepFragment {

        private static final int ACTION_OK = 0;

        private String mFsUuid;
        private String mDescription;

        public static MissingStorageFragment newInstance(String fsUuid) {
            final MissingStorageFragment fragment = new MissingStorageFragment();
            final Bundle b = new Bundle(1);
            b.putString(VolumeRecord.EXTRA_FS_UUID, fsUuid);
            fragment.setArguments(b);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            StorageManager storageManager = getActivity().getSystemService(StorageManager.class);
            mFsUuid = getArguments().getString(VolumeRecord.EXTRA_FS_UUID);
            if (TextUtils.isEmpty(mFsUuid)) {
                throw new IllegalStateException(
                        "MissingStorageFragment launched without specifying missing storage");
            }
            final VolumeRecord volumeRecord = storageManager.findRecordByUuid(mFsUuid);
            mDescription = volumeRecord.getNickname();

            super.onCreate(savedInstanceState);
        }

        @Override
        public @NonNull GuidanceStylist.Guidance onCreateGuidance(Bundle savedInstanceState) {
            return new GuidanceStylist.Guidance(
                    getString(R.string.storage_missing_title, mDescription),
                    getString(R.string.storage_missing_description),
                    null,
                    getActivity().getDrawable(R.drawable.ic_settings_error));
        }

        @Override
        public void onCreateActions(@NonNull List<GuidedAction> actions,
                Bundle savedInstanceState) {
            actions.add(new GuidedAction.Builder()
                    .title(getString(android.R.string.ok))
                    .id(ACTION_OK)
                    .build());
        }

        @Override
        public void onGuidedActionClicked(GuidedAction action) {
            getActivity().finish();
        }

    }

    public static class DiskReceiver extends BroadcastReceiver {

        private StorageManager mStorageManager;
        @Override
        public void onReceive(Context context, Intent intent) {
            final UserManager userManager =
                    (UserManager) context.getSystemService(Context.USER_SERVICE);
            final UserInfo userInfo = userManager.getUserInfo(UserHandle.myUserId());

            if (userInfo.isRestricted() ||
                    ActivityManager.getCurrentUser() != UserHandle.myUserId()) {
                Log.d(TAG, "Ignoring storage notification: wrong user");
                return;
            }

            if (Settings.Secure.getInt(context.getContentResolver(),
                    Settings.Secure.USER_SETUP_COMPLETE, 0) == 0) {
                Log.d(TAG, "Ignoring storage notification: setup not complete");
                return;
            }

            mStorageManager = context.getSystemService(StorageManager.class);

            if (TextUtils.equals(intent.getAction(), VolumeInfo.ACTION_VOLUME_STATE_CHANGED)) {
                final int state = intent.getIntExtra(VolumeInfo.EXTRA_VOLUME_STATE, -1);
                if (state == VolumeInfo.STATE_MOUNTED ||
                        state == VolumeInfo.STATE_MOUNTED_READ_ONLY) {
                    handleMount(context, intent);
                } else if (state == VolumeInfo.STATE_UNMOUNTED ||
                        state == VolumeInfo.STATE_BAD_REMOVAL) {
                    handleUnmount(context, intent);
                }
            } else if (TextUtils.equals(intent.getAction(), DiskInfo.ACTION_DISK_SCANNED)) {
                handleScan(context, intent);
            } else if (TextUtils.equals(intent.getAction(),
                    "com.google.android.tungsten.setupwraith.TV_SETTINGS_POST_SETUP")) {
                handleSetupComplete(context);
            }
        }

        private void handleScan(Context context, Intent intent) {
            final String diskId = intent.getStringExtra(DiskInfo.EXTRA_DISK_ID);
            if (TextUtils.isEmpty(diskId)) {
                Log.e(TAG, intent.getAction() + " with no " + DiskInfo.EXTRA_DISK_ID);
                return;
            }
            final DiskInfo diskInfo = mStorageManager.findDiskById(diskId);
            if (diskInfo.size <= 0) {
                Log.d(TAG, "Disk ID " + diskId + " has no media");
                return;
            }
            if (intent.getIntExtra(DiskInfo.EXTRA_VOLUME_COUNT, -1) != 0) {
                Log.d(TAG, "Disk ID " + diskId + " has usable volumes, waiting for mount");
                return;
            }
            // No usable volumes, prompt the user to erase the disk
            final Intent i = NewStorageActivity.getNewStorageLaunchIntent(context, null, diskId);
            setPopupLaunchFlags(i);
            context.startActivity(i);
        }

        private void handleMount(Context context, Intent intent) {
            final String volumeId = intent.getStringExtra(VolumeInfo.EXTRA_VOLUME_ID);

            final List<VolumeInfo> volumeInfos = mStorageManager.getVolumes();
            for (final VolumeInfo info : volumeInfos) {
                if (!TextUtils.equals(info.getId(), volumeId)) {
                    continue;
                }
                final String uuid = info.getFsUuid();
                Log.d(TAG, "Scanning volume: " + info);
                if (info.getType() != VolumeInfo.TYPE_PUBLIC || TextUtils.isEmpty(uuid)) {
                    continue;
                }
                final VolumeRecord record = mStorageManager.findRecordByUuid(uuid);
                if (record.isInited() || record.isSnoozed()) {
                    continue;
                }
                final DiskInfo disk = info.getDisk();
                if (disk.isAdoptable()) {
                    final Intent i = NewStorageActivity.getNewStorageLaunchIntent(context,
                            volumeId, disk.getId());
                    setPopupLaunchFlags(i);
                    context.startActivity(i);
                    break;
                }
            }
        }

        private void handleUnmount(Context context, Intent intent) {
            final String fsUuid = intent.getStringExtra(VolumeRecord.EXTRA_FS_UUID);
            if (TextUtils.isEmpty(fsUuid)) {
                Log.e(TAG, "Missing fsUuid, not launching activity.");
                return;
            }
            final VolumeRecord volumeRecord = mStorageManager.findRecordByUuid(fsUuid);
            if (volumeRecord == null) {
                return;
            }
            Log.d(TAG, "Found ejected volume: " + volumeRecord + " for FSUUID: " + fsUuid);
            if (volumeRecord.getType() == VolumeInfo.TYPE_PRIVATE) {
                final Intent i = NewStorageActivity.getMissingStorageLaunchIntent(context, fsUuid);
                setPopupLaunchFlags(i);
                context.startActivity(i);
            }
        }

        private void handleSetupComplete(Context context) {
            Log.d(TAG, "Scanning for storage post-setup");

            final List<DiskInfo> diskInfos = mStorageManager.getDisks();
            for (DiskInfo diskInfo : diskInfos) {
                Log.d(TAG, "Scanning disk: " + diskInfo);
                if (diskInfo.size <= 0) {
                    Log.d(TAG, "Disk ID " + diskInfo.id + " has no media");
                    continue;
                }
                if (diskInfo.volumeCount != 0) {
                    Log.d(TAG, "Disk ID " + diskInfo.id + " has usable volumes, deferring");
                    continue;
                }
                // No usable volumes, prompt the user to erase the disk
                final Intent i =
                        NewStorageActivity.getNewStorageLaunchIntent(context, null, diskInfo.id);
                setPopupLaunchFlags(i);
                context.startActivity(i);
                return;
            }

            final List<VolumeInfo> volumeInfos = mStorageManager.getVolumes();
            for (final VolumeInfo info : volumeInfos) {
                final String uuid = info.getFsUuid();
                Log.d(TAG, "Scanning volume: " + info);
                if (info.getType() != VolumeInfo.TYPE_PUBLIC || TextUtils.isEmpty(uuid)) {
                    continue;
                }
                final VolumeRecord record = mStorageManager.findRecordByUuid(uuid);
                if (record.isInited() || record.isSnoozed()) {
                    continue;
                }
                final DiskInfo disk = info.getDisk();
                if (disk.isAdoptable()) {
                    final Intent i = NewStorageActivity.getNewStorageLaunchIntent(context,
                            info.getId(), disk.getId());
                    setPopupLaunchFlags(i);
                    context.startActivity(i);
                    return;
                }
            }
        }

        private void setPopupLaunchFlags(Intent intent) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
    }

}
