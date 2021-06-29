DEVICE_PATH := device/amazon/sloane

# Android TV
PRODUCT_IS_ATV_SDK := true
PRODUCT_CHARACTERISTICS := tv

# Include drawables for various densities.
PRODUCT_AAPT_CONFIG := normal large xlarge tvdpi hdpi xhdpi xxhdpi
PRODUCT_AAPT_PREF_CONFIG := xhdpi

# Overlays
DEVICE_PACKAGE_OVERLAYS += $(DEVICE_PATH)/overlay

# TV
PRODUCT_PACKAGES += \
    TvProvider \
    TvSettings \
    tv_input.default \
    LeanbackLauncher \
    LeanbackIme \
    CMLeanbackCustomizer

# Enable frame-exact AV sync
PRODUCT_PROPERTY_OVERRIDES += \
    persist.sys.media.avsync=true

# Ramdisk
PRODUCT_PACKAGES += \
    fstab.mt8173 \
    init.mt8173.rc \
    init.mt8173.usb.rc \
    init.project.rc \
    init.recovery.mt8173.rc \
    init.trace.rc \
    ueventd.mt8173.rc \
	ozwpan.sh

PRODUCT_COPY_FILES += \
    $(DEVICE_PATH)/rootdir/etc/init-fosflags.sh:system/etc/init.fosflags.sh

# Permissions
PRODUCT_COPY_FILES += \
    frameworks/native/data/etc/tablet_core_hardware.xml:system/etc/permissions/tablet_core_hardware.xml \
    frameworks/native/data/etc/android.hardware.wifi.xml:system/etc/permissions/android.hardware.wifi.xml \
    frameworks/native/data/etc/android.hardware.wifi.direct.xml:system/etc/permissions/android.hardware.wifi.direct.xml \
    frameworks/native/data/etc/android.hardware.usb.accessory.xml:system/etc/permissions/android.hardware.usb.accessory.xml \
    frameworks/native/data/etc/android.hardware.usb.host.xml:system/etc/permissions/android.hardware.usb.host.xml \
    frameworks/native/data/etc/android.software.sip.voip.xml:system/etc/permissions/android.software.sip.voip.xml \
    frameworks/native/data/etc/android.hardware.bluetooth_le.xml:system/etc/permissions/android.hardware.bluetooth_le.xml \
    $(DEVICE_PATH)/configs/permissions/tv_core_hardware.xml:system/etc/permissions/tv_core_hardware.xml \
    $(DEVICE_PATH)/configs/permissions/android.hardware.hdmi.cec.xml:system/etc/permissions/android.hardware.hdmi.cec.xml

# Media
PRODUCT_COPY_FILES += \
    frameworks/av/media/libstagefright/data/media_codecs_google_audio.xml:system/etc/media_codecs_google_audio.xml \
    frameworks/av/media/libstagefright/data/media_codecs_google_telephony.xml:system/etc/media_codecs_google_telephony.xml \
    frameworks/av/media/libstagefright/data/media_codecs_google_video_le.xml:system/etc/media_codecs_google_video_le.xml \
    frameworks/av/media/libstagefright/data/media_codecs_google_video.xml:system/etc/media_codecs_google_video.xml \
    frameworks/av/media/libeffects/data/audio_effects.conf:system/etc/audio_effects.conf \
    $(DEVICE_PATH)/configs/media/media_codecs.xml:system/etc/media_codecs.xml \
    $(DEVICE_PATH)/configs/media/media_profiles.xml:system/etc/media_profiles.xml \
    $(DEVICE_PATH)/configs/media/mtk_omx_core.cfg:system/etc/mtk_omx_core.cfg

# Thermal
PRODUCT_COPY_FILES += \
     $(DEVICE_PATH)/configs/thermal/thermal.policy.conf:system/etc/.tp/thermal.policy.conf \
     $(DEVICE_PATH)/configs/thermal/.ht120.mtc:system/etc/.tp/.ht120.mtc

# Graphics
PRODUCT_PACKAGES += \
	libion

# Kernel Modules
PRODUCT_COPY_FILES += \
    $(DEVICE_PATH)/configs/insmod/init.insmod.cfg:system/etc/init.insmod.cfg \
    $(DEVICE_PATH)/rootdir/etc/init.insmod.sh:system/etc/init.insmod.sh

# Audio
PRODUCT_PACKAGES += \
    audio.a2dp.default \
    audio_policy.stub \
    audio.r_submix.default \
    audio.btle.default \
    libtinycompress \
    libtinymix \
    libtinyxml \
    libtinyalsa \
    libalsautils \
    libaudioroute \
    libaudiospdif

PRODUCT_COPY_PACKAGES += \
    $(DEVICE_PATH)/configs/audio/audio_policy.conf:system/etc/audio_policy.conf

# IPv6 tethering
PRODUCT_PACKAGES += \
    ebtables \
    ethertypes

# Amazon's btremoted
PRODUCT_PACKAGES += \
    btremoted

# Lights
PRODUCT_PACKAGES += \
    lights.mt8173

# Shim Libraries
PRODUCT_PACKAGES += \
    libshim_gralloc \
    libshim_log

# Keylayout
PRODUCT_COPY_FILES += \
	$(DEVICE_PATH)/configs/keylayout/Vendor_1949_Product_0401.kl:system/usr/keylayout/Vendor_1949_Product_0401.kl \
	$(DEVICE_PATH)/configs/keylayout/Vendor_1949_Product_0402.kl:system/usr/keylayout/Vendor_1949_Product_0402.kl \
	$(DEVICE_PATH)/configs/keylayout/Vendor_1949_Product_0407.kl:system/usr/keylayout/Vendor_1949_Product_0407.kl \
	$(DEVICE_PATH)/configs/keylayout/lightning-device.kl:system/usr/keylayout/lightning-device.kl

# Keychars
PRODUCT_COPY_FILES += \
	$(DEVICE_PATH)/configs/keychars/lightning-device.kcm:system/usr/keychars/lightning-device.kcm

# IDC
PRODUCT_COPY_FILES += \
	$(DEVICE_PATH)/configs/idc/lightning-device.idc:system/usr/idc/lightning-device.idc \
	$(DEVICE_PATH)/configs/idc/amazon_touch.idc:system/usr/idc/amazon_touch.idc

# Wi-Fi Configs
PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/configs/wifi/dhcpcd.conf:system/etc/dhcpcd/dhcpcd.conf \
    $(LOCAL_PATH)/configs/wifi/p2p_supplicant_overlay.conf:system/etc/wifi/p2p_supplicant_overlay.conf \
    $(LOCAL_PATH)/configs/wifi/wpa_supplicant.conf:system/etc/wifi/wpa_supplicant.conf \
    $(LOCAL_PATH)/configs/wifi/wpa_supplicant_overlay.conf:system/etc/wifi/wpa_supplicant_overlay.conf

# call dalvik heap config
$(call inherit-product, frameworks/native/build/tablet-7in-hdpi-1024-dalvik-heap.mk)

# call hwui memory config
$(call inherit-product-if-exists, frameworks/native/build/phone-xxhdpi-2048-hwui-memory.mk)

# Get non-open-source specific aspects
$(call inherit-product-if-exists, vendor/amazon/sloane/sloane-vendor.mk)
