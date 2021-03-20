# Release name
PRODUCT_RELEASE_NAME := AFTVS

# Inherit some common CM stuff.
$(call inherit-product-if-exists, vendor/cm/config/common_full_tv.mk)
$(call inherit-product, $(SRC_TARGET_DIR)/product/core_64_bit.mk)
$(call inherit-product, $(SRC_TARGET_DIR)/product/full_base.mk)
$(call inherit-product, $(SRC_TARGET_DIR)/product/product_launched_with_l_mr1.mk)

# Inherit device configuration
$(call inherit-product, device/amazon/sloane/device_sloane.mk)

# Device identifier. This must come after all inclusions
PRODUCT_DEVICE := sloane
PRODUCT_NAME := lineage_sloane
PRODUCT_BRAND := google
PRODUCT_MODEL := Fire TV 2
PRODUCT_MANUFACTURER := amzn

# Product Characteristics
PRODUCT_CHARACTERISTICS := tv

