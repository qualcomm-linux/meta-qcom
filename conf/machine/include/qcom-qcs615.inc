# Configuration and variables for QCS615 family.

SOC_FAMILY = "qcs615"
require conf/machine/include/qcom-base.inc
require conf/machine/include/qcom-common.inc

DEFAULTTUNE = "armv8-2a-crypto"
require conf/machine/include/arm/arch-armv8-2a.inc

# Without the kvm machine feature the board boots under Gunyah, so pre-seed
# the VendorDtbOverlays UEFI variable with the el2gh overlay selector.
QCOM_VENDOR_DTB_OVERLAYS ?= "${@bb.utils.contains("MACHINE_FEATURES", "kvm", "", "el2gh", d)}"

MACHINE_ESSENTIAL_EXTRA_RRECOMMENDS += " \
    packagegroup-qcom-boot-essential \
    packagegroup-machine-essential-qcom-qcs615-soc \
"

MACHINE_EXTRA_RRECOMMENDS += " \
    packagegroup-qcom-boot-additional \
"
