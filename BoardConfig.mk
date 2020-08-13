DEVICE_FOLDER := device/amazon/douglas

# inherit from the proprietary version
-include vendor/amazon/douglas/BoardConfigVendor.mk

# Platform
TARGET_BOARD_PLATFORM := mt8163
TARGET_NO_BOOTLOADER := true

# GPU
TARGET_BOARD_PLATFORM_GPU := mali-720mp2

# Architecture
TARGET_ARCH := arm64
TARGET_ARCH_VARIANT := armv8-a
TARGET_CPU_ABI := arm64-v8a
TARGET_CPU_VARIANT := generic
TARGET_2ND_ARCH := arm
TARGET_2ND_ARCH_VARIANT := armv7-a-neon
TARGET_2ND_CPU_ABI := armeabi-v7a
TARGET_2ND_CPU_ABI2 := armeabi
TARGET_2ND_CPU_VARIANT := cortex-a15
TARGET_CPU_SMP := true

# Bootloader
TARGET_BOOTLOADER_BOARD_NAME := mt8163

# Filesystems
TARGET_USERIMAGES_USE_EXT4:= true
TARGET_USERIMAGES_SPARSE_EXT_DISABLED := false
BOARD_CACHEIMAGE_FILE_SYSTEM_TYPE := ext4

# Kernel
BOARD_KERNEL_CMDLINE := bootopt=64S3,32N2,64N2
BOARD_KERNEL_CMDLINE += androidboot.selinux=permissive
BOARD_KERNEL_BASE := 0x40078000
BOARD_KERNEL_PAGESIZE := 2048
BOARD_MKBOOTIMG_ARGS := \
	--base 0x40078000 \
	--pagesize 2048 \
	--kernel_offset 0x00008000 \
	--ramdisk_offset 0x03f88000 \
	--second_offset 0x00e88000 \
	--tags_offset 0x07f88000

TARGET_KERNEL_ARCH := arm64
TARGET_KERNEL_HEADER_ARCH := arm64
TARGET_KERNEL_CROSS_COMPILE_PREFIX := $(shell pwd)/prebuilts/gcc/linux-x86/aarch64/aarch64-linux-android-4.9/bin/aarch64-linux-android-
BOARD_KERNEL_IMAGE_NAME := Image.gz-dtb
TARGET_KERNEL_CONFIG := douglas_defconfig
TARGET_KERNEL_SOURCE := kernel/amazon/douglas

# 64 Bit Binder Userspace
TARGET_USES_64_BIT_BINDER := true

# Kernel Modules
TARGET_KMODULES := true

# Disable memcpy opt (for audio libraries)
TARGET_CPU_MEMCPY_OPT_DISABLE := true

# EGL
BOARD_EGL_CFG := $(DEVICE_FOLDER)/configs/graphics/egl.cfg
USE_OPENGL_RENDERER := true
BOARD_EGL_WORKAROUND_BUG_10194508 := true
TARGET_USES_C2D_COMPOSITION := true
TARGET_USES_OVERLAY := true
NUM_FRAMEBUFFER_SURFACE_BUFFERS := 3
TARGET_RUNNING_WITHOUT_SYNC_FRAMEWORK := true
TARGET_FORCE_HWC_FOR_VIRTUAL_DISPLAYS := true
MAX_VIRTUAL_DISPLAY_DIMENSION := 1
MAX_EGL_CACHE_KEY_SIZE := 12*1024
MAX_EGL_CACHE_SIZE := 1024*1024

# MediaTek Hardware
BOARD_HAS_MTK_HARDWARE := true
MTK_HARDWARE := true

# MediaTek Legacy AV Blob
BOARD_USES_LEGACY_MTK_AV_BLOB := true

# WIFI
WPA_SUPPLICANT_VERSION := VER_0_8_X
BOARD_WPA_SUPPLICANT_DRIVER := NL80211
BOARD_HOSTAPD_DRIVER := NL80211
BOARD_WLAN_DEVICE := mt66xx
BOARD_WPA_SUPPLICANT_PRIVATE_LIB := lib_driver_cmd_$(BOARD_WLAN_DEVICE)
BOARD_WPA_SUPPLICANT_DRIVER := NL80211
BOARD_HOSTAPD_PRIVATE_LIB := lib_driver_cmd_$(BOARD_WLAN_DEVICE)
WIFI_DRIVER_FW_PATH_PARAM := "/dev/wmtWifi"
WIFI_DRIVER_FW_PATH_STA := STA
WIFI_DRIVER_FW_PATH_AP := AP
WIFI_DRIVER_FW_PATH_STA := P2P

# Bluetooth
BOARD_HAVE_BLUETOOTH := true
BOARD_HAVE_BLUETOOTH_MTK := true
BOARD_BLUETOOTH_DOES_NOT_USE_RFKILL := true
BOARD_BLUETOOTH_BDROID_BUILDCFG_INCLUDE_DIR := $(DEVICE_FOLDER)/bluetooth

# Digital Restrictions Management (DRM)
MTK_WVDRM_SUPPORT := yes
MTK_WVDRM_L1_SUPPORT := no

# System Properties
TARGET_SYSTEM_PROP := $(DEVICE_FOLDER)/system.prop

# Recovery Partition Table
TARGET_RECOVERY_FSTAB := $(DEVICE_FOLDER)/recovery/etc/recovery.fstab

# Screen/Bootanimation
DEVICE_RESOLUTION := 800x1280
TARGET_SCREEN_WIDTH := 800
TARGET_SCREEN_HEIGHT := 1280
TARGET_BOOTANIMATION_MULTITHREAD_DECODE := true
TARGET_BOOTANIMATION_PRELOAD := true
TARGET_BOOTANIMATION_TEXTURE_CACHE := true

# Enable Minikin Text Layout Engine
USE_MINIKIN := true

# Use dlmalloc Instead of Jemalloc for Mallocs
MALLOC_SVELTE := true

# Sotrage Lun File Path
TARGET_USE_CUSTOM_LUN_FILE_PATH := "/sys/devices/virtual/android_usb/android0/f_mass_storage/lun%d/file"

# SELinux
BOARD_SEPOLICY_DIRS += \
    $(DEVICE_FOLDER)/sepolicy

# Cyanogenmod H/W Hooks
BOARD_USES_CYANOGEN_HARDWARE := true
BOARD_HARDWARE_CLASS := $(DEVICE_FOLDER)/cmhw

# Shim Libraries
LINKER_FORCED_SHIM_LIBS := \
    /system/lib/liblog.so|libshim_log.so \
    /system/lib64/liblog.so|libshim_log.so \
    /system/lib/hw/hwcomposer.mt8163.so|libshim_ui.so \
    /system/lib64/hw/hwcomposer.mt8163.so|libshim_ui.so \
    /system/lib/libMtkOmxVdecEx.so|libshim_ui.so \
    /system/lib/libasp.so|libshim_parcel.so \
    /system/lib64/libasp.so|libshim_parcel.so \
    /system/lib/libcam.utils.sensorlistener.so|libshim_camera.so \
    /system/lib64/libcam.utils.sensorlistener.so|libshim_camera.so \
    /system/lib/libdrmmtkutil.so|libshim_drm.so \
    /system/lib64/libdrmmtkutil.so|libshim_drm.so \
    /system/lib/libui_ext.so|libshim_ui.so \
    /system/lib64/libui_ext.so|libshim_ui.so \
    /system/lib/libcam_utils.so|libshim_ui.so \
    /system/lib64/libcam_utils.so|libshim_ui.so \
	/system/bin/amzn_dha_hmac|libshim_drm.so \
	/system/bin/amzn_dha_tool|libshim_drm.so \

# Device-Specific Headers
TARGET_SPECIFIC_HEADER_PATH := $(DEVICE_FOLDER)/include

# Partitions
BOARD_BOOTIMAGE_PARTITION_SIZE := 16777216
BOARD_RECOVERYIMAGE_PARTITION_SIZE := 16777216
BOARD_SYSTEMIMAGE_PARTITION_SIZE := 1696512081
BOARD_CACHEIMAGE_PARTITION_SIZE := 444596224
BOARD_USERDATAIMAGE_PARTITION_SIZE := 13153337344
BOARD_FLASH_BLOCK_SIZE := 131072
BOARD_HAS_LARGE_FILESYSTEM := true

# Assert
TARGET_OTA_ASSERT_DEVICE := douglas,KFDOWI

# Block Based OTA
BLOCK_BASED_OTA := false
