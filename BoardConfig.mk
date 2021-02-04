DEVICE_FOLDER := device/amazon/sloane

# inherit from the proprietary version
-include vendor/amazon/sloane/BoardConfigVendor.mk

# Platform
TARGET_BOARD_PLATFORM := mt8173
TARGET_NO_BOOTLOADER := true

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
TARGET_BOOTLOADER_BOARD_NAME := mt8173

# Filesystems
TARGET_USERIMAGES_USE_EXT4:= true
TARGET_USERIMAGES_SPARSE_EXT_DISABLED := false
BOARD_CACHEIMAGE_FILE_SYSTEM_TYPE := ext4

# Kernel
BOARD_KERNEL_CMDLINE := bootopt=64S3,32N2,64N2
BOARD_KERNEL_CMDLINE += androidboot.selinux=permissive
BOARD_KERNEL_BASE := 0x40000000
BOARD_KERNEL_PAGESIZE := 2048
BOARD_MKBOOTIMG_ARGS := \
	--base 0x40000000 \
	--pagesize 2048 \
	--kernel_offset 0x00080000 \
	--ramdisk_offset 0x01000000 \
	--second_offset 0x00f00000 \
	--tags_offset 0x00000100
	
# Second Image
BOARD_CUSTOM_BOOTIMG_MK := $(DEVICE_FOLDER)/mkbootimg.mk

TARGET_KERNEL_ARCH := arm64
TARGET_KERNEL_HEADER_ARCH := arm64
TARGET_KERNEL_CROSS_COMPILE_PREFIX := $(shell pwd)/prebuilts/gcc/linux-x86/aarch64/aarch64-linux-android-4.9/bin/aarch64-linux-android-
BOARD_KERNEL_IMAGE_NAME := Image.gz-dtb
TARGET_KERNEL_CONFIG := sloane_defconfig
TARGET_KERNEL_SOURCE := kernel/amazon/sloane

# 64 Bit Binder Userspace
TARGET_USES_64_BIT_BINDER := true

# Kernel Modules
TARGET_KMODULES := true

# Disable memcpy opt (for audio libraries)
TARGET_CPU_MEMCPY_OPT_DISABLE := true

# Audio
BOARD_USES_MTK_AUDIO := true
TARGET_EXCLUDES_AUDIOFX := true

# OMX
TARGET_OMX_LEGACY_RESCALING:= true

# EGL
BOARD_EGL_CFG := $(DEVICE_FOLDER)/configs/graphics/egl.cfg
USE_OPENGL_RENDERER := true
BOARD_EGL_WORKAROUND_BUG_10194508 := true
TARGET_USES_C2D_COMPOSITION := true
TARGET_USES_OVERLAY := true
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

# Camera
TARGET_HAS_LEGACY_CAMERA_HAL1 := true

# Digital Restrictions Management (DRM)
MTK_WVDRM_SUPPORT := yes
MTK_WVDRM_L1_SUPPORT := yes

# Global Flags
TARGET_GLOBAL_CPPFLAGS += -DMTK_HARDWARE
BOARD_GLOBAL_CFLAGS += -DCOMPAT_SENSORS_M -DAMAZON_LOG
BOARD_GLOBAL_CLFAGS += -DMTK_HARDWARE -DADD_LEGACY_ACQUIRE_BUFFER_SYMBOL

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
TARGET_BOOTANIMATION_HALF_RES := true

# Enable Minikin Text Layout Engine
USE_MINIKIN := true

# Use dlmalloc Instead of Jemalloc for Mallocs
MALLOC_SVELTE := true

# Sotrage Lun File Path
TARGET_USE_CUSTOM_LUN_FILE_PATH := "/sys/devices/virtual/android_usb/android0/f_mass_storage/lun%d/file"

# SELinux
BOARD_SEPOLICY_DIRS += \
    $(DEVICE_FOLDER)/sepolicy

# Media Extractors
BOARD_SECCOMP_POLICY := \
    $(DEVICE_FOLDER)/seccomp-policy

# Shim Libraries
LINKER_FORCED_SHIM_LIBS := \
    /system/lib/liblog.so|libshim_log.so \
    /system/lib64/liblog.so|libshim_log.so \
    /system/lib/libMtkOmxVdecEx.so|libshim_ui.so \
    /system/lib/libMtkOmxVenc.so|libshim_ui.so \
    /system/lib/libasp.so|libshim_parcel.so \
    /system/lib64/libasp.so|libshim_parcel.so \
    /system/lib/libdrmmtkutil.so|libshim_drm.so \
    /system/lib64/libdrmmtkutil.so|libshim_drm.so \
    /system/lib/libui_ext.so|libshim_ui.so \
    /system/lib64/libui_ext.so|libshim_ui.so \
    /system/bin/amzn_dha_hmac|libshim_drm.so \
    /system/bin/amzn_dha_tool|libshim_drm.so \
    /system/lib/libwvm.so|libshim_wvm.so

# Device-Specific Headers
TARGET_SPECIFIC_HEADER_PATH := $(DEVICE_FOLDER)/include

# Init Library
TARGET_INIT_VENDOR_LIB := libinit_sloane

# Partitions
BOARD_BOOTIMAGE_PARTITION_SIZE := 16777216
BOARD_RECOVERYIMAGE_PARTITION_SIZE := 16777216
BOARD_SYSTEMIMAGE_PARTITION_SIZE := 1696512081
BOARD_CACHEIMAGE_PARTITION_SIZE := 444596224
BOARD_USERDATAIMAGE_PARTITION_SIZE := 13153337344
BOARD_FLASH_BLOCK_SIZE := 131072

# System Stability
TARGET_USES_MKE2FS := true

# LPM
BOARD_CHARGING_MODE_BOOTING_LPM := /sys/class/BOOT/BOOT/boot/boot_mode

# Assert
TARGET_OTA_ASSERT_DEVICE := sloane,AFTVS

# Block Based OTA
BLOCK_BASED_OTA := false

# Disable CM API check
WITHOUT_CHECK_API := true
