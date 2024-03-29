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

LOCAL_PATH := device/amazon/sloane

# Include all the languages
$(call inherit-product, $(SRC_TARGET_DIR)/product/languages_full.mk)

# Include atv base
$(call inherit-product, device/google/atv/products/atv_base.mk)

# Include lineage atv
$(call inherit-product, device/lineage/atv/lineage_atv.mk)

# Call the proprietary vendor makefile
$(call inherit-product-if-exists, vendor/amazon/sloane/sloane-vendor.mk)

# Device uses high-density artwork where available
PRODUCT_AAPT_CONFIG := normal large xlarge tvdpi hdpi xhdpi xxhdpi
PRODUCT_AAPT_PREF_CONFIG := xhdpi

# ATV SDK
PRODUCT_IS_ATV_SDK := true

# Overlay
DEVICE_PACKAGE_OVERLAYS += device/amazon/sloane/overlay

# Lights HAL
PRODUCT_PACKAGES += \
	lights.mt8173 \
	android.hardware.light@2.0-impl-mediatek \
	android.hardware.light@2.0-service-mediatek

# Audio
PRODUCT_PACKAGES += \
	audio.a2dp.default \
	audio.r_submix.default \
	audio.usb.default \
	audio_policy.stub \
	libalsautils \
	libaudio-resampler \
	libtinyalsa \
	libtinycompress \
	libtinyxml \
	libaudioroute \
	libaudiospdif \
	libnbaio \
	libeffects \
	android.hardware.audio@2.0-impl \
	android.hardware.audio.effect@2.0-impl

# USB
PRODUCT_PACKAGES += \
	android.hardware.usb@1.0-service.basic

# Power
PRODUCT_PACKAGES += \
	power.mt8173 \
	android.hardware.power@1.0-impl \
	android.hardware.power@1.0-service

# Memtrack
PRODUCT_PACKAGES += \
	android.hardware.memtrack@1.0-impl \
	android.hardware.memtrack@1.0-service

# Keymaster
PRODUCT_PACKAGES += \
	android.hardware.keymaster@3.0-impl \
	android.hardware.keymaster@3.0-service

# Blisrc
PRODUCT_PACKAGES += \
	libblisrc \
	libblisrc32

# Configstore
PRODUCT_PACKAGES += \
	android.hardware.configstore@1.1-impl \
	android.hardware.configstore@1.1-service

# Media Codecs
PRODUCT_COPY_FILES += \
	$(LOCAL_PATH)/configs/media/media_codecs.xml:system/etc/media_codecs.xml \
	$(LOCAL_PATH)/configs/media/media_profiles.xml:system/etc/media_profiles.xml \
	$(LOCAL_PATH)/configs/media/mtk_omx_core.cfg:system/etc/mtk_omx_core.cfg \
	frameworks/av/media/libstagefright/data/media_codecs_google_audio.xml:system/etc/media_codecs_google_audio.xml \
	frameworks/av/media/libstagefright/data/media_codecs_google_telephony.xml:system/etc/media_codecs_google_telephony.xml \
	frameworks/av/media/libstagefright/data/media_codecs_google_video_le.xml:system/etc/media_codecs_google_video_le.xml \
	frameworks/av/media/libstagefright/data/media_codecs_google_video.xml:system/etc/media_codecs_google_video.xml \
	frameworks/av/media/libstagefright/data/media_codecs_google_tv.xml:system/etc/media_codecs_google_tv.xml

# Seccomp
PRODUCT_COPY_FILES += \
	$(LOCAL_PATH)/seccomp-policy/mediaextractor.policy:$(TARGET_COPY_OUT_VENDOR)/etc/seccomp_policy/mediaextractor.policy \
	$(LOCAL_PATH)/seccomp-policy/mediacodec.policy:$(TARGET_COPY_OUT_VENDOR)/etc/seccomp_policy/mediacodec.policy

# Prebuilt Keylayouts
PRODUCT_COPY_FILES += \
	$(LOCAL_PATH)/configs/keylayouts/lightning-device.kl:system/usr/keylayout/lightning-device.kl \
	$(LOCAL_PATH)/configs/keylayouts/Vendor_1949_Product_0401.kl:system/usr/keylayout/Vendor_1949_Product_0401.kl \
	$(LOCAL_PATH)/configs/keylayouts/Vendor_1949_Product_0402.kl:system/usr/keylayout/Vendor_1949_Product_0402.kl \
	$(LOCAL_PATH)/configs/keylayouts/Vendor_1949_Product_0407.kl:system/usr/keylayout/Vendor_1949_Product_0407.kl

# Input
PRODUCT_COPY_FILES += \
	$(LOCAL_PATH)/configs/idc/amazon_touch.idc:system/usr/keylayout/amazon_touch.idc

# Kernel Modules
PRODUCT_COPY_FILES += \
	$(LOCAL_PATH)/configs/insmod/init.insmod.cfg:system/etc/init.insmod.cfg

# Wifi
PRODUCT_PACKAGES += \
	hostapd \
	lib_driver_cmd_mt76xx \
	wpa_supplicant.conf \
	android.hardware.wifi@1.0-service \
	wificond
	
# Wi-Fi Configs
PRODUCT_COPY_FILES += \
	$(LOCAL_PATH)/configs/wifi/dhcpcd.conf:system/etc/dhcpcd/dhcpcd.conf \
	$(LOCAL_PATH)/configs/wifi/p2p_supplicant_overlay.conf:system/etc/wifi/p2p_supplicant_overlay.conf \
	$(LOCAL_PATH)/configs/wifi/wpa_supplicant.conf:system/etc/wifi/wpa_supplicant.conf \
	$(LOCAL_PATH)/configs/wifi/wpa_supplicant_overlay.conf:system/etc/wifi/wpa_supplicant_overlay.conf

# Network
PRODUCT_PACKAGES += \
	netd \
	android.system.net.netd@1.0 \
	netutils-wrapper-1.0  

# Graphics
PRODUCT_PACKAGES += \
	libion \
	android.hardware.graphics.allocator@2.0-impl \
	android.hardware.graphics.mapper@2.0-impl \
	android.hardware.graphics.composer@2.1-impl

# Graphics (MTK)
PRODUCT_PACKAGES += \
	libgui_ext \
	libui_ext \
	libgralloc_extra

# Shim Libraries
PRODUCT_PACKAGES += \
	libshim_log \
	libshim_ui \
	libshim_parcel \
	libshim_drm \
	libshim_audio \
	libshim_stagefright \
	libshim_atomic \
	libshim_mutexdestroy \
	libshim_callstack \
	libshim_selinux \
    libshim_wpa

# Keyboard
PRODUCT_PACKAGES += \
	LeanBackIme

# SetupWraith
PRODUCT_PACKAGES += \
	SetupWraith

PRODUCT_COPY_FILES += \
	$(LOCAL_PATH)/apps/prebuilts/SetupWraith/privapp_whitelist_com.google.android.tungsten.setupwraith.xml:system/etc/permissions/privapp_whitelist_com.google.android.tungsten.setupwraith.xml
	
# Remote Pairing Services
PRODUCT_PACKAGES += \
	AtvRemoteService \
	GamepadPairingService \
	GlobalKeyInterceptor \
	RemoteControlService

# TV Input
PRODUCT_PACKAGES += \
	android.hardware.tv.input@1.0-impl \
	android.hardware.tv.input@1.0-service

# CEC
PRODUCT_PACKAGES += \
	android.hardware.tv.cec@1.0-impl \
	android.hardware.tv.cec@1.0-service

# OverScan
PRODUCT_PACKAGES += \
	OverScan

# Remove unused packages
PRODUCT_PACKAGES += \
	RemovePackages

# Bluetooth
PRODUCT_PACKAGES += \
	btremoted \
	libbt-vendor \
	libbluetooth_mtk \
	android.hardware.bluetooth@1.0-impl-mediatek \
	android.hardware.bluetooth@1.0-service-mediatek

# Bluetooth Configs
PRODUCT_COPY_FILES += \
	$(LOCAL_PATH)/configs/bluetooth/bt_did.conf:system/etc/bluetooth/bt_did.conf \
	$(LOCAL_PATH)/configs/bluetooth/bt_stack.conf:system/etc/bluetooth/bt_stack.conf \
	$(LOCAL_PATH)/configs/bluetooth/auto_pair_devlist.conf:system/etc/bluetooth/auto_pair_devlist.conf

# Camera Init
PRODUCT_COPY_FILES += $(LOCAL_PATH)/configs/init/mediaserver.rc:system/etc/init/mediaserver.rc

# Camera
PRODUCT_SUPPORTS_CAMERA := false

# Audio Wakelock (So Music will not stop while turn off screen)
PRODUCT_COPY_FILES += $(LOCAL_PATH)/configs/init/audioserver.rc:system/etc/init/audioserver.rc

# Thermal
PRODUCT_PACKAGES += \
    android.hardware.thermal@1.0-impl \
    android.hardware.thermal@1.0-service

PRODUCT_COPY_FILES += \
	$(LOCAL_PATH)/configs/thermal/.ht120.mtc:system/etc/.tp/.ht120.mtc \
	$(LOCAL_PATH)/configs/thermal/thermal.policy.conf:system/etc/.tp/thermal.policy.conf

# HIDL
PRODUCT_PACKAGES += \
	android.hidl.base@1.0

# Enable StagefrightCodec 2.0
PRODUCT_PACKAGES += libstagefright_ccodec

# Ramdisk
PRODUCT_COPY_FILES += \
	$(LOCAL_PATH)/rootdir/fstab.mt8173:root/fstab.mt8173 \
	$(LOCAL_PATH)/rootdir/init.mt8173.rc:root/init.mt8173.rc \
	$(LOCAL_PATH)/rootdir/init.mt8173.usb.rc:root/init.mt8173.usb.rc \
	$(LOCAL_PATH)/rootdir/init.recovery.mt8173.rc:root/init.recovery.mt8173.rc \
	$(LOCAL_PATH)/rootdir/init.project.rc:root/init.project.rc \
	$(LOCAL_PATH)/rootdir/init.trace.rc:root/init.trace.rc \
	$(LOCAL_PATH)/rootdir/ueventd.mt8173.rc:root/ueventd.mt8173.rc \
	$(LOCAL_PATH)/rootdir/init.insmod.sh:system/etc/init.insmod.sh \
	$(LOCAL_PATH)/rootdir/ozwpan.sh:root/ozwpan.sh \
	$(LOCAL_PATH)/rootdir/baddr_loader.sh:root/baddr_loader.sh

# Permissions
PRODUCT_COPY_FILES += \
	frameworks/native/data/etc/android.hardware.bluetooth_le.xml:system/etc/permissions/android.hardware.bluetooth_le.xml \
	frameworks/native/data/etc/android.hardware.bluetooth.xml:system/etc/permissions/android.hardware.bluetooth.xml \
	frameworks/native/data/etc/android.hardware.sensor.light.xml:system/etc/permissions/android.hardware.sensor.light.xml \
	frameworks/native/data/etc/android.hardware.usb.accessory.xml:system/etc/permissions/android.hardware.usb.accessory.xml \
	frameworks/native/data/etc/android.hardware.usb.host.xml:system/etc/permissions/android.hardware.usb.host.xml \
	frameworks/native/data/etc/android.hardware.wifi.direct.xml:system/etc/permissions/android.hardware.wifi.direct.xml \
	frameworks/native/data/etc/android.hardware.wifi.xml:system/etc/permissions/android.hardware.wifi.xml \
	frameworks/native/data/etc/android.hardware.ethernet.xml:system/etc/permissions/android.hardware.ethernet.xml \
	$(LOCAL_PATH)/configs/permissions/android.hardware.hdmi.cec.xml:system/etc/permissions/android.hardware.hdmi.cec.xml

# Default OMX service to non-Treble
PRODUCT_PROPERTY_OVERRIDES += \
	persist.media.treble_omx=false

# Disable cameraserver
PRODUCT_PROPERTY_OVERRIDES += \
	media.stagefright.legacyencoder=true \
	media.stagefright.less-secure=true \

# Override Default Properties
PRODUCT_DEFAULT_PROPERTY_OVERRIDES += \
	ro.secure=0 \
	ro.adb.secure=0 \
	ro.allow.mock.location=0 \
	ro.mount.fs=EXT4 \
	camera.disable_zsl_mode=1 \
	ro.debuggable=1 \
	persist.service.acm.enable=0 \
	service.adb.root=1

# Graphics
PRODUCT_PROPERTY_OVERRIDES += \
	debug.hwui.renderer=opengl \
	debug.hwui.use_partial_updates=false \
	debug.sf.disable_backpressure=1 \
	debug.sf.latch_unsignaled=1 \
	persist.sys.display.clearMotion=1 \
	debug.hwui.use_buffer_age=false
	
# DRM
PRODUCT_PACKAGES += \
	libdrm \
	libfwdlockengine

# No RIL
PRODUCT_PROPERTY_OVERRIDES += \
	keyguard.no_require_sim=1 \
	ro.radio.use-ppp=no \
	ro.config.nocheckin=yes \
	ro.radio.noril=1 \
	ro.carrier=wifi-only \
	persist.radio.noril=1

# AudioFlinger
PRODUCT_PROPERTY_OVERRIDES += ro.af.client_heap_size_kbyte=7168

# Configure dalvik heap
$(call inherit-product, frameworks/native/build/phone-xhdpi-2048-dalvik-heap.mk)
