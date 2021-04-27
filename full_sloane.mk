# Inherit from the common Open Source product configuration
$(call inherit-product, $(SRC_TARGET_DIR)/product/core_64_bit.mk)
$(call inherit-product, $(SRC_TARGET_DIR)/product/full_base.mk)
$(call inherit-product, $(SRC_TARGET_DIR)/product/languages_full.mk)

# Inherit from sloane device
$(call inherit-product, device/amazon/sloane/device.mk)

# Device identifier. This must come after all inclusions
PRODUCT_DEVICE := sloane
PRODUCT_NAME := full_sloane
PRODUCT_BRAND := google
PRODUCT_MODEL := Fire TV2
PRODUCT_MANUFACTURER := amzn
