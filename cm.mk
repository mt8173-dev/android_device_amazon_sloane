# Inherit some common CM stuff.
$(call inherit-product, vendor/cm/config/common_full_tv.mk)
$(call inherit-product, $(SRC_TARGET_DIR)/product/core_64_bit.mk)
$(call inherit-product, $(SRC_TARGET_DIR)/product/full_base.mk)
$(call inherit-product, $(SRC_TARGET_DIR)/product/languages_full.mk)

# Include atv base
$(call inherit-product, device/google/atv/products/atv_base.mk)

# Inherit from sloane device
$(call inherit-product, device/amazon/sloane/device.mk)

# Bootanimation
TARGET_SCREEN_WIDTH := 800
TARGET_SCREEN_HEIGHT := 1280

# Set those variables here to overwrite the inherited values.
BOARD_VENDOR := amzn
PRODUCT_BRAND := google
PRODUCT_DEVICE := sloane
PRODUCT_NAME := cm_sloane
PRODUCT_MANUFACTURER := amzn
PRODUCT_MODEL := Fire TV2
TARGET_VENDOR := amazn

# Use the latest approved GMS identifiers unless running a signed build
PRODUCT_BUILD_PROP_OVERRIDES += \
    BUILD_FINGERPRINT=Amazon/full_sloane/sloane:5.1.1/LVY48F/36.6.3.9_user_639566320:user/release-keys \
    PRIVATE_BUILD_DESC="full_sloane-user 5.1.1 LVY48F 36.6.3.9_user_639566320 release-keys"