#
# Copyright (C) 2021 The LineageOS Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

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
BOARD_KERNEL_TAGS_OFFSET := 0x00000100
BOARD_RAMDISK_OFFSET := 0x01000000
BOARD_SECOND_OFFSET := 0x00f00000

BOARD_MKBOOTIMG_ARGS := \
	--base 0x40000000 \
	--pagesize 2048 \
	--kernel_offset 0x00080000 \
	--ramdisk_offset $(BOARD_RAMDISK_OFFSET) \
	--second_offset $(BOARD_SECOND_OFFSET) \
	--tags_offset $(BOARD_KERNEL_TAGS_OFFSET)
	
# Second Image
BOARD_CUSTOM_BOOTIMG_MK := $(DEVICE_FOLDER)/mkbootimg.mk

TARGET_KERNEL_ARCH := arm64
TARGET_KERNEL_HEADER_ARCH := arm64
TARGET_KERNEL_CROSS_COMPILE_PREFIX := $(shell pwd)/prebuilts/gcc/linux-x86/aarch64/aarch64-linux-android-4.9/bin/aarch64-linux-android-
BOARD_KERNEL_IMAGE_NAME := Image
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
SUPPRESS_MTK_AUDIO_BLOB_ERR_MSG := true

# OMX
TARGET_OMX_LEGACY_RESCALING:= true

# EGL
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
BOARD_WLAN_DEVICE := MediaTek
WPA_SUPPLICANT_VERSION := VER_0_8_X
BOARD_HOSTAPD_DRIVER := NL80211
BOARD_HOSTAPD_PRIVATE_LIB := lib_driver_cmd_mt66xx
BOARD_WPA_SUPPLICANT_DRIVER := NL80211
BOARD_WPA_SUPPLICANT_PRIVATE_LIB := lib_driver_cmd_mt66xx
WIFI_DRIVER_FW_PATH_PARAM := /dev/wmtWifi
WIFI_DRIVER_FW_PATH_STA := STA
WIFI_DRIVER_FW_PATH_AP := AP
WIFI_DRIVER_FW_PATH_P2P := P2P
WIFI_DRIVER_STATE_CTRL_PARAM := /dev/wmtWifi
WIFI_DRIVER_STATE_ON := 1
WIFI_DRIVER_STATE_OFF := 0
BOARD_CONNECTIVITY_MODULE := combo_mt76xx

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
	$(DEVICE_FOLDER)/sepolicy/mediatek/basic/non_plat \
	$(DEVICE_FOLDER)/sepolicy/mediatek/bsp/non_plat \
	$(DEVICE_FOLDER)/sepolicy/mt8173/basic \
	$(DEVICE_FOLDER)/sepolicy/mt8173/bsp \
	$(DEVICE_FOLDER)/sepolicy

BOARD_PLAT_PUBLIC_SEPOLICY_DIR += \
	$(DEVICE_FOLDER)/sepolicy/mediatek/basic/plat_public \
	$(DEVICE_FOLDER)/sepolicy/mediatek/bsp/plat_public

BOARD_PLAT_PRIVATE_SEPOLICY_DIR += \
	$(DEVICE_FOLDER)/sepolicy/mediatek/basic/plat_private \
	$(DEVICE_FOLDER)/sepolicy/mediatek/bsp/plat_private

# Media Extractors
BOARD_SECCOMP_POLICY := \
    $(DEVICE_FOLDER)/seccomp-policy

# Shim Libraries
TARGET_LD_SHIM_LIBS := \
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
	/system/lib/libwvm.so|libshim_wvm.so \
	/system/bin/audiocmdservice_atci|libshim_audio.so \
	/system/lib/hw/audio.primary.mt8173.so|libshim_audio.so \
	/system/lib64/hw/audio.primary.mt8173.so|libshim_audio.so \
	/system/vendor/lib/libstagefright_soft_ddpdec.so|libshim_stagefright.so

# Device-Specific Headers
TARGET_SPECIFIC_HEADER_PATH := $(DEVICE_FOLDER)/include

# Partitions
BOARD_BOOTIMAGE_PARTITION_SIZE := 0x2000000
BOARD_RECOVERYIMAGE_PARTITION_SIZE := 0x2000000
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

# Gatekeeper
BOARD_USE_SOFT_GATEKEEPER := true

# VINTF
DEVICE_MANIFEST_FILE := $(DEVICE_FOLDER)/configs/vintf/manifest.xml
DEVICE_MATRIX_FILE   := $(DEVICE_FOLDER)/configs/vintf/compatibility_matrix.xml