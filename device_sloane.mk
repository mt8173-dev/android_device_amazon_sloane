LOCAL_PATH := device/amazon/sloane

# Include all the languages
$(call inherit-product, $(SRC_TARGET_DIR)/product/languages_full.mk)

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
    lights.mt8173

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
	libnbaio

# Blisrc
PRODUCT_PACKAGES += \
    libblisrc \
    libblisrc32

# Media Codecs
PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/configs/media/media_codecs.xml:system/etc/media_codecs.xml \
    $(LOCAL_PATH)/configs/media/media_profiles.xml:system/etc/media_profiles.xml \
    $(LOCAL_PATH)/configs/media/mtk_omx_core.cfg:system/etc/mtk_omx_core.cfg \
    frameworks/av/media/libstagefright/data/media_codecs_google_audio.xml:system/etc/media_codecs_google_audio.xml \
    frameworks/av/media/libstagefright/data/media_codecs_google_telephony.xml:system/etc/media_codecs_google_telephony.xml \
    frameworks/av/media/libstagefright/data/media_codecs_google_video_le.xml:system/etc/media_codecs_google_video_le.xml \
    frameworks/av/media/libstagefright/data/media_codecs_google_tv.xml:system/etc/media_codecs_google_tv.xml

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
    libwpa_client \
    hostapd \
    dhcpcd.conf \
    wpa_supplicant \
	lib_driver_cmd_mt66xx \
    wpa_supplicant.conf
	
# Wi-Fi Configs
PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/configs/wifi/p2p_supplicant_overlay.conf:system/etc/wifi/p2p_supplicant_overlay.conf \
    $(LOCAL_PATH)/configs/wifi/wpa_supplicant.conf:system/etc/wifi/wpa_supplicant.conf \
    $(LOCAL_PATH)/configs/wifi/wpa_supplicant_overlay.conf:system/etc/wifi/wpa_supplicant_overlay.conf

# Network
PRODUCT_PACKAGES += \
    netd

# Graphics
PRODUCT_PACKAGES += \
    libion

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
	libshim_wvm \
	libshim_audio \
	libshim_stagefright

# STLPort
PRODUCT_COPY_FILES += \
    prebuilts/ndk/current/sources/cxx-stl/stlport/libs/armeabi-v7a/libstlport_shared.so:system/lib/libstlport.so \
    prebuilts/ndk/current/sources/cxx-stl/stlport/libs/arm64-v8a/libstlport_shared.so:system/lib64/libstlport.so

# IPv6 tethering
PRODUCT_PACKAGES += \
    ebtables \
    ethertypes

# LeanBack
PRODUCT_PACKAGES += \
    LeanbackLauncher \
    LeanBackIme \
    CMLeanbackCustomizer
    
# TvSettings
PRODUCT_PACKAGES += \
    TvSettings \
    TV
    
# SetupWraith
PRODUCT_PACKAGES += \
    SetupWraith
    
# Remote Pairing Services
PRODUCT_PACKAGES += \
    AtvRemoteService \
    GamepadPairingService \
    GlobalKeyInterceptor \
    RemoteControlService

# TV Input
PRODUCT_PACKAGES += \
	tv_input.default

# OverScan
PRODUCT_PACKAGES += \
    OverScan

# Remove unused packages
PRODUCT_PACKAGES += \
    RemovePackages

# Bluetooth
PRODUCT_PACKAGES += \
    btremoted

# Camera Init
PRODUCT_COPY_FILES += $(LOCAL_PATH)/configs/init/mediaserver.rc:system/etc/init/mediaserver.rc

# Audio Wakelock (So Music will not stop while turn off screen)
PRODUCT_COPY_FILES += $(LOCAL_PATH)/configs/init/audioserver.rc:system/etc/init/audioserver.rc

# Thermal
PRODUCT_COPY_FILES += \
     $(LOCAL_PATH)/configs/thermal/.ht120.mtc:system/etc/.tp/.ht120.mtc \
     $(LOCAL_PATH)/configs/thermal/thermal.policy.conf:system/etc/.tp/thermal.policy.conf

# Ramdisk
PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/rootdir/fstab.mt8173:root/fstab.mt8173 \
    $(LOCAL_PATH)/rootdir/init.mt8173.rc:root/init.mt8173.rc \
    $(LOCAL_PATH)/rootdir/init.mt8173.usb.rc:root/init.mt8173.usb.rc \
    $(LOCAL_PATH)/rootdir/init.recovery.mt8173.rc:root/init.recovery.mt8173.rc \
    $(LOCAL_PATH)/rootdir/init.project.rc:root/init.project.rc \
    $(LOCAL_PATH)/rootdir/init.trace.rc:root/init.trace.rc \
    $(LOCAL_PATH)/rootdir/init.connectivity.rc:root/init.connectivity.rc \
    $(LOCAL_PATH)/rootdir/ueventd.mt8173.rc:root/ueventd.mt8173.rc \
	$(LOCAL_PATH)/rootdir/init.insmod.sh:system/etc/init.insmod.sh \
	$(LOCAL_PATH)/rootdir/ozwpan.sh:root/ozwpan.sh

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
    $(LOCAL_PATH)/configs/permissions/tv_core_hardware.xml:system/etc/permissions/tv_core_hardware.xml \
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

# Configure dalvik heap
$(call inherit-product, frameworks/native/build/phone-xhdpi-2048-dalvik-heap.mk)

# Call hwui memory config
$(call inherit-product-if-exists, frameworks/native/build/phone-xxhdpi-2048-hwui-memory.mk)
