DEVICE_PATH := device/amazon/sloane

# Android TV
PRODUCT_IS_ATV_SDK := true
PRODUCT_CHARACTERISTICS := tv

# Include drawables for various densities.
PRODUCT_AAPT_CONFIG := normal large xlarge tvdpi hdpi xhdpi xxhdpi
PRODUCT_AAPT_PREF_CONFIG := xhdpi

# Overlays
DEVICE_PACKAGE_OVERLAYS += $(DEVICE_PATH)/overlay

# Fonts / Svox
$(call inherit-product-if-exists, frameworks/base/data/sounds/AllAudio.mk)
$(call inherit-product-if-exists, external/svox/pico/lang/all_pico_languages.mk)
$(call inherit-product-if-exists, frameworks/base/data/fonts/fonts.mk)
$(call inherit-product-if-exists, external/google-fonts/dancing-script/fonts.mk)
$(call inherit-product-if-exists, external/google-fonts/carrois-gothic-sc/fonts.mk)
$(call inherit-product-if-exists, external/google-fonts/coming-soon/fonts.mk)
$(call inherit-product-if-exists, external/google-fonts/cutive-mono/fonts.mk)
$(call inherit-product-if-exists, external/lohit-fonts/fonts.mk)
$(call inherit-product-if-exists, external/noto-fonts/fonts.mk)
$(call inherit-product-if-exists, external/naver-fonts/fonts.mk)
$(call inherit-product-if-exists, frameworks/base/data/keyboards/keyboards.mk)
$(call inherit-product-if-exists, frameworks/webview/chromium/chromium.mk)
$(call inherit-product, $(SRC_TARGET_DIR)/product/core_minimal.mk)
-include external/svox/pico/lang/PicoLangDeDeInSystem.mk
-include external/svox/pico/lang/PicoLangEnGBInSystem.mk
-include external/svox/pico/lang/PicoLangEnUsInSystem.mk
-include external/svox/pico/lang/PicoLangEsEsInSystem.mk
-include external/svox/pico/lang/PicoLangFrFrInSystem.mk
-include external/svox/pico/lang/PicoLangItItInSystem.mk

# TV
PRODUCT_PACKAGES += \
    TvProvider \
    TvSettings \
    tv_input.default \
    LeanbackLauncher \
    LeanbackIme \
    Overscan \
    CMLeanbackCustomizer

# Enable frame-exact AV sync
PRODUCT_PROPERTY_OVERRIDES += \
    persist.sys.media.avsync=true

# Zygote it's 64bit
PRODUCT_DEFAULT_PROPERTY_OVERRIDES += \
	ro.zygote=zygote64_32

# Ramdisk
PRODUCT_PACKAGES += \
    advanced_meta_init.rc \
    fstab.mt8173 \
    init-fosflags.sh \
    init.mt8173.rc \
    init.mt8173.usb.rc \
    init.project.rc \
    init.recovery.mt8173.rc \
    init.ssd.rc \
    init.trace.rc \
    meta_init.project.rc \
    meta_init.rc \
    ueventd.mt8173.rc

PRODUCT_COPY_FILES += \
    $(DEVICE_PATH)/rootdir/init.fosflags.sh:system/etc/init.fosflags.sh

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

# Audio
PRODUCT_PACKAGES += \
    audio.a2dp.default \
    audio_policy.stub \
    audio.r_submix.default \
    audio.btle.default \
    libtinycompress \
    libtinymix \
    libtinyxml

PRODUCT_COPY_PACKAGES += \
    $(DEVICE_PATH)/configs/audio/audio_policy.conf:system/etc/audio_policy.conf

# Bluetooth
PRODUCT_PACKAGES += \
    bluetooth.default

# Power
PRODUCT_PACKAGES += \
    power.default

# network
PRODUCT_PACKAGES += \
    netd

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

# Keylayout
PRODUCT_COPY_FILES += \
     $(DEVICE_PATH)/configs/usr/idc/amazon_touch.idc:system/usr/idc/amazon_touch.idc \
     $(DEVICE_PATH)/configs/usr/idc/lightning-device.idc:system/usr/idc/lightning-device.idc \
     $(DEVICE_PATH)/configs/usr/keychars/Generic.kcm:system/usr/keychars/Generic.kcm \
     $(DEVICE_PATH)/configs/usr/keychars/lightning-device.kcm:system/usr/keychars/lightning-device.kcm \
     $(DEVICE_PATH)/configs/usr/keylayout/AVRCP.kl:system/usr/keylayout/AVRCP.kl \
     $(DEVICE_PATH)/configs/usr/keylayout/Generic.kl:system/usr/keylayout/Generic.kl \
     $(DEVICE_PATH)/configs/usr/keylayout/lightning-device.kl:system/usr/keylayout/lightning-device.kl \
     $(DEVICE_PATH)/configs/usr/keylayout/qwerty.kl:system/usr/keylayout/qwerty.kl \
     $(DEVICE_PATH)/configs/usr/keylayout/Vendor_0079_Product_0011.kl:system/usr/keylayout/Vendor_0079_Product_0011.kl \
     $(DEVICE_PATH)/configs/usr/keylayout/Vendor_0171_Product_0413.kl:system/usr/keylayout/Vendor_0171_Product_0413.kl \
     $(DEVICE_PATH)/configs/usr/keylayout/Vendor_045e_Product_028e.kl:system/usr/keylayout/Vendor_045e_Product_028e.kl \
     $(DEVICE_PATH)/configs/usr/keylayout/Vendor_045e_Product_0291.kl:system/usr/keylayout/Vendor_045e_Product_0291.kl \
     $(DEVICE_PATH)/configs/usr/keylayout/Vendor_045e_Product_0719.kl:system/usr/keylayout/Vendor_045e_Product_0719.kl \
     $(DEVICE_PATH)/configs/usr/keylayout/Vendor_046d_Product_b501.kl:system/usr/keylayout/Vendor_046d_Product_b501.kl \
     $(DEVICE_PATH)/configs/usr/keylayout/Vendor_046d_Product_c216.kl:system/usr/keylayout/Vendor_046d_Product_c216.kl \
     $(DEVICE_PATH)/configs/usr/keylayout/Vendor_046d_Product_c219.kl:system/usr/keylayout/Vendor_046d_Product_c219.kl \
     $(DEVICE_PATH)/configs/usr/keylayout/Vendor_046d_Product_c21d.kl:system/usr/keylayout/Vendor_046d_Product_c21d.kl \
     $(DEVICE_PATH)/configs/usr/keylayout/Vendor_046d_Product_c21e.kl:system/usr/keylayout/Vendor_046d_Product_c21e.kl \
     $(DEVICE_PATH)/configs/usr/keylayout/Vendor_046d_Product_c21f.kl:system/usr/keylayout/Vendor_046d_Product_c21f.kl \
     $(DEVICE_PATH)/configs/usr/keylayout/Vendor_046d_Product_c294.kl:system/usr/keylayout/Vendor_046d_Product_c294.kl \
     $(DEVICE_PATH)/configs/usr/keylayout/Vendor_046d_Product_c299.kl:system/usr/keylayout/Vendor_046d_Product_c299.kl \
     $(DEVICE_PATH)/configs/usr/keylayout/Vendor_046d_Product_c532.kl:system/usr/keylayout/Vendor_046d_Product_c532.kl \
     $(DEVICE_PATH)/configs/usr/keylayout/Vendor_054c_Product_0268.kl:system/usr/keylayout/Vendor_054c_Product_0268.kl \
     $(DEVICE_PATH)/configs/usr/keylayout/Vendor_054c_Product_05c4.kl:system/usr/keylayout/Vendor_054c_Product_05c4.kl \
     $(DEVICE_PATH)/configs/usr/keylayout/Vendor_057e_Product_0306.kl:system/usr/keylayout/Vendor_057e_Product_0306.kl \
     $(DEVICE_PATH)/configs/usr/keylayout/Vendor_0583_Product_2060.kl:system/usr/keylayout/Vendor_0583_Product_2060.kl \
     $(DEVICE_PATH)/configs/usr/keylayout/Vendor_05ac_Product_0239.kl:system/usr/keylayout/Vendor_05ac_Product_0239.kl \
     $(DEVICE_PATH)/configs/usr/keylayout/Vendor_0b05_Product_4500.kl:system/usr/keylayout/Vendor_0b05_Product_4500.kl \
     $(DEVICE_PATH)/configs/usr/keylayout/Vendor_1038_Product_1412.kl:system/usr/keylayout/Vendor_1038_Product_1412.kl \
     $(DEVICE_PATH)/configs/usr/keylayout/Vendor_12bd_Product_d015.kl:system/usr/keylayout/Vendor_12bd_Product_d015.kl \
     $(DEVICE_PATH)/configs/usr/keylayout/Vendor_1532_Product_0900.kl:system/usr/keylayout/Vendor_1532_Product_0900.kl \
     $(DEVICE_PATH)/configs/usr/keylayout/Vendor_1689_Product_fd00.kl:system/usr/keylayout/Vendor_1689_Product_fd00.kl \
     $(DEVICE_PATH)/configs/usr/keylayout/Vendor_1689_Product_fd01.kl:system/usr/keylayout/Vendor_1689_Product_fd01.kl \
     $(DEVICE_PATH)/configs/usr/keylayout/Vendor_1689_Product_fe00.kl:system/usr/keylayout/Vendor_1689_Product_fe00.kl \
     $(DEVICE_PATH)/configs/usr/keylayout/Vendor_18d1_Product_2c40.kl:system/usr/keylayout/Vendor_18d1_Product_2c40.kl \
     $(DEVICE_PATH)/configs/usr/keylayout/Vendor_1949_Product_0401.kl:system/usr/keylayout/Vendor_1949_Product_0401.kl \
     $(DEVICE_PATH)/configs/usr/keylayout/Vendor_1949_Product_0402.kl:system/usr/keylayout/Vendor_1949_Product_0402.kl \
     $(DEVICE_PATH)/configs/usr/keylayout/Vendor_1949_Product_0404.kl:system/usr/keylayout/Vendor_1949_Product_0404.kl \
     $(DEVICE_PATH)/configs/usr/keylayout/Vendor_1949_Product_0406.kl:system/usr/keylayout/Vendor_1949_Product_0406.kl \
     $(DEVICE_PATH)/configs/usr/keylayout/Vendor_1949_Product_0407.kl:system/usr/keylayout/Vendor_1949_Product_0407.kl \
     $(DEVICE_PATH)/configs/usr/keylayout/Vendor_1949_Product_0409.kl:system/usr/keylayout/Vendor_1949_Product_0409.kl \
     $(DEVICE_PATH)/configs/usr/keylayout/Vendor_1949_Product_040a.kl:system/usr/keylayout/Vendor_1949_Product_040a.kl \
     $(DEVICE_PATH)/configs/usr/keylayout/Vendor_1949_Product_0415.kl:system/usr/keylayout/Vendor_1949_Product_0415.kl \
     $(DEVICE_PATH)/configs/usr/keylayout/Vendor_1bad_Product_f016.kl:system/usr/keylayout/Vendor_1bad_Product_f016.kl \
     $(DEVICE_PATH)/configs/usr/keylayout/Vendor_1bad_Product_f023.kl:system/usr/keylayout/Vendor_1bad_Product_f023.kl \
     $(DEVICE_PATH)/configs/usr/keylayout/Vendor_1bad_Product_f027.kl:system/usr/keylayout/Vendor_1bad_Product_f027.kl \
     $(DEVICE_PATH)/configs/usr/keylayout/Vendor_1bad_Product_f036.kl:system/usr/keylayout/Vendor_1bad_Product_f036.kl \
     $(DEVICE_PATH)/configs/usr/keylayout/Vendor_1d79_Product_0009.kl:system/usr/keylayout/Vendor_1d79_Product_0009.kl \
     $(DEVICE_PATH)/configs/usr/keylayout/Vendor_20a0_Product_0004.kl:system/usr/keylayout/Vendor_20a0_Product_0004.kl \
     $(DEVICE_PATH)/configs/usr/keylayout/Vendor_22b8_Product_093d.kl:system/usr/keylayout/Vendor_22b8_Product_093d.kl \
     $(DEVICE_PATH)/configs/usr/keylayout/Vendor_2378_Product_1008.kl:system/usr/keylayout/Vendor_2378_Product_1008.kl \
     $(DEVICE_PATH)/configs/usr/keylayout/Vendor_2378_Product_100a.kl:system/usr/keylayout/Vendor_2378_Product_100a.kl \
     $(DEVICE_PATH)/configs/usr/keylayout/Vendor_2836_Product_0001.kl:system/usr/keylayout/Vendor_2836_Product_0001.kl

# call dalvik heap config
$(call inherit-product, frameworks/native/build/tablet-7in-hdpi-1024-dalvik-heap.mk)

# call hwui memory config
$(call inherit-product-if-exists, frameworks/native/build/phone-xxhdpi-2048-hwui-memory.mk)

# Get non-open-source specific aspects
$(call inherit-product-if-exists, vendor/amazon/sloane/sloane-vendor.mk)
