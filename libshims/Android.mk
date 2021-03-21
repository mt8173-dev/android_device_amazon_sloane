LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_SRC_FILES := log/lab126_log_shim.cpp log/xlog_shim.cpp
LOCAL_SHARED_LIBRARIES := liblog
LOCAL_MODULE := libshim_log
LOCAL_MODULE_TAGS := optional
LOCAL_MODULE_CLASS := SHARED_LIBRARIES
LOCAL_PROPRIETARY_MODULE := true
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_SRC_FILES := ui/ui_shim.cpp ui/GraphicBuffer.cpp
LOCAL_SHARED_LIBRARIES := libui libgui libutils libcutils
LOCAL_MODULE := libshim_ui
LOCAL_MODULE_TAGS := optional
LOCAL_MODULE_CLASS := SHARED_LIBRARIES
LOCAL_PROPRIETARY_MODULE := true
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_SRC_FILES := parcel/parcel_shim.cpp
LOCAL_SHARED_LIBRARIES := libbinder
LOCAL_MODULE := libshim_parcel
LOCAL_CFLAGS := -Wno-unused-parameter
LOCAL_MODULE_TAGS := optional
LOCAL_MODULE_CLASS := SHARED_LIBRARIES
LOCAL_PROPRIETARY_MODULE := true
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_SRC_FILES := drm/icu_shim.cpp drm/crypto_shim.cpp
LOCAL_SHARED_LIBRARIES := libicuuc libcrypto
LOCAL_MODULE := libshim_drm
LOCAL_MODULE_TAGS := optional
LOCAL_MODULE_CLASS := SHARED_LIBRARIES
LOCAL_PROPRIETARY_MODULE := true
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_SRC_FILES := audio/audio_shim.cpp audio/media_shim.cpp
LOCAL_SHARED_LIBRARIES := libmedia libutils
LOCAL_MODULE := libshim_audio
LOCAL_MODULE_TAGS := optional
LOCAL_MODULE_CLASS := SHARED_LIBRARIES
LOCAL_PROPRIETARY_MODULE := true
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_SRC_FILES := stagefright/stagefright_shim.cpp
LOCAL_SHARED_LIBRARIES := libstagefright
LOCAL_MODULE := libshim_stagefright
LOCAL_MODULE_TAGS := optional
LOCAL_MODULE_CLASS := SHARED_LIBRARIES
LOCAL_PROPRIETARY_MODULE := true
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_SRC_FILES := atomic/atomic_shim.cpp
LOCAL_WHOLE_STATIC_LIBRARIES := libcutils
LOCAL_SHARED_LIBRARIES := liblog libbase
LOCAL_MODULE := libshim_atomic
LOCAL_MODULE_TAGS := optional
LOCAL_VENDOR_MODULE := true
LOCAL_MODULE_CLASS := SHARED_LIBRARIES
include $(BUILD_SHARED_LIBRARY)