LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := RemovePackages
LOCAL_MODULE_TAGS := optional
LOCAL_MODULE_CLASS := APPS

LOCAL_OVERRIDES_PACKAGES := \
    AudioFX \
	SoundRecorder \
	Trebuchet \
	Browser \
	Calculator \
	Camera2 \
	CMFileManager \
	Email \
	Eleven \
	TelephonyProvider \
	TeleService \
	Telecomm

LOCAL_UNINSTALLABLE_MODULE := true
LOCAL_CERTIFICATE := PRESIGNED
include $(BUILD_PREBUILT)
