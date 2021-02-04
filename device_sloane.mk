LOCAL_PATH := device/amazon/sloane

# Include all the languages
$(call inherit-product, $(SRC_TARGET_DIR)/product/languages_full.mk)

# Call the proprietary vendor makefile
$(call inherit-product-if-exists, vendor/amazon/sloane/sloane-vendor.mk)

# Device uses high-density artwork where available
PRODUCT_AAPT_CONFIG := normal mdpi
PRODUCT_AAPT_PREF_CONFIG := mdpi

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
    libaudioroute

# Blisrc
PRODUCT_PACKAGES += \
    libblisrc \
    libblisrc32

# Media Codecs
PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/configs/media/media_codecs.xml:system/etc/media_codecs.xml \
    $(LOCAL_PATH)/configs/media/media_profiles.xml:system/etc/media_profiles.xml \
    $(LOCAL_PATH)/configs/media/media_codecs_performance.xml:system/etc/media_codecs_performance.xml \
    $(LOCAL_PATH)/configs/media/media_codecs_mediatek_audio.xml:system/etc/media_codecs_mediatek_audio.xml \
    $(LOCAL_PATH)/configs/media/media_codecs_mediatek_video.xml:system/etc/media_codecs_mediatek_video.xml \
    frameworks/av/media/libstagefright/data/media_codecs_google_audio.xml:system/etc/media_codecs_google_audio.xml \
    frameworks/av/media/libstagefright/data/media_codecs_google_telephony.xml:system/etc/media_codecs_google_telephony.xml \
    frameworks/av/media/libstagefright/data/media_codecs_google_video_le.xml:system/etc/media_codecs_google_video_le.xml \

# Prebuilt Keylayouts
PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/configs/keylayouts/lightning-device.kl:system/usr/keylayout/lightning-device.kl \
    $(LOCAL_PATH)/configs/keylayouts/mtk-kpd.kl:system/usr/keylayout/mtk-kpd.kl \
    $(LOCAL_PATH)/configs/keylayouts/Vendor_0171_Product_040b.kl:system/usr/keylayout/Vendor_0171_Product_040b.kl \
    $(LOCAL_PATH)/configs/keylayouts/Vendor_0171_Product_040c.kl:system/usr/keylayout/Vendor_0171_Product_040c.kl \
    $(LOCAL_PATH)/configs/keylayouts/Vendor_0171_Product_0413.kl:system/usr/keylayout/Vendor_0171_Product_0413.kl \
    $(LOCAL_PATH)/configs/keylayouts/Vendor_045e_Product_0291.kl:system/usr/keylayout/Vendor_045e_Product_0291.kl \
    $(LOCAL_PATH)/configs/keylayouts/Vendor_1949_Product_0402.kl:system/usr/keylayout/Vendor_1949_Product_0402.kl \
    $(LOCAL_PATH)/configs/keylayouts/Vendor_1949_Product_0404.kl:system/usr/keylayout/Vendor_1949_Product_0404.kl \
    $(LOCAL_PATH)/configs/keylayouts/Vendor_1949_Product_0406.kl:system/usr/keylayout/Vendor_1949_Product_0406.kl \
    $(LOCAL_PATH)/configs/keylayouts/Vendor_1949_Product_0407.kl:system/usr/keylayout/Vendor_1949_Product_0407.kl \
    $(LOCAL_PATH)/configs/keylayouts/Vendor_1949_Product_0415.kl:system/usr/keylayout/Vendor_1949_Product_0415.kl \
    $(LOCAL_PATH)/configs/keylayouts/Vendor_20a0_Product_0004.kl:system/usr/keylayout/Vendor_20a0_Product_0004.kl

# Input
PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/configs/idc/amazon_touch.idc:system/usr/keylayout/amazon_touch.idc

# Wifi
PRODUCT_PACKAGES += \
    libwpa_client \
    hostapd \
    dhcpcd.conf \
    wpa_supplicant \
    wpa_supplicant.conf

# Patched Audio Configuration Files
PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/configs/audio/audio_device.xml:system/etc/audio_device.xml \
    $(LOCAL_PATH)/configs/audio/audio_policy.conf:system/vendor/etc/audio_policy.conf

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
    libshim_wvm

# IPv6 tethering
PRODUCT_PACKAGES += \
    ebtables \
    ethertypes

# LeanBack
PRODUCT_PACKAGES += \
    LeanbackLauncher \
    LeanBackIme \
    CMLeanbackCustomizer
    
# OverScan
PRODUCT_PACKAGES += \
    OverScan

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
     $(LOCAL_PATH)/configs/thermal/thermal.conf:system/etc/.tp/thermal.conf \
     $(LOCAL_PATH)/configs/thermal/thermal.off.conf:system/etc/.tp/thermal.off.conf \
     $(LOCAL_PATH)/configs/thermal/thermal.policy.conf:system/etc/.tp/thermal.policy.conf

# Ramdisk
PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/rootdir/fstab.mt8173:root/fstab.mt8173 \
    $(LOCAL_PATH)/rootdir/init.mt8173.rc:root/init.mt8173.rc \
    $(LOCAL_PATH)/rootdir/init.mt8173usb.rc:root/init.mt8173.usb.rc \
    $(LOCAL_PATH)/rootdir/init.recovery.mt8173.rc:root/init.recovery.mt8173.rc \
    $(LOCAL_PATH)/rootdir/init.project.rc:root/init.project.rc \
    $(LOCAL_PATH)/rootdir/init.connectivity.rc:root/init.connectivity.rc \
    $(LOCAL_PATH)/rootdir/ueventd.mt8173.rc:root/ueventd.mt8173.rc

# Permissions
PRODUCT_COPY_FILES += \
    frameworks/native/data/etc/android.hardware.bluetooth_le.xml:system/etc/permissions/android.hardware.bluetooth_le.xml \
    frameworks/native/data/etc/android.hardware.bluetooth.xml:system/etc/permissions/android.hardware.bluetooth.xml \
    frameworks/native/data/etc/android.hardware.sensor.accelerometer.xml:system/etc/permissions/android.hardware.sensor.accelerometer.xml \
    frameworks/native/data/etc/android.hardware.sensor.light.xml:system/etc/permissions/android.hardware.sensor.light.xml \
    frameworks/native/data/etc/android.hardware.touchscreen.multitouch.jazzhand.xml:system/etc/permissions/android.hardware.touchscreen.multitouch.jazzhand.xml \
    frameworks/native/data/etc/android.hardware.usb.accessory.xml:system/etc/permissions/android.hardware.usb.accessory.xml \
    frameworks/native/data/etc/android.hardware.usb.host.xml:system/etc/permissions/android.hardware.usb.host.xml \
    frameworks/native/data/etc/android.hardware.wifi.direct.xml:system/etc/permissions/android.hardware.wifi.direct.xml \
    frameworks/native/data/etc/android.hardware.wifi.xml:system/etc/permissions/android.hardware.wifi.xml \
    $(LOCAL_PATH)/configs/permissions/tv_core_hardware.xml:system/etc/permissions/tv_core_hardware.xml

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
