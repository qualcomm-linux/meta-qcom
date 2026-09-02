# Configurations and variables for QCS8300 SoC family.

SOC_FAMILY = "qcs8300"
require conf/machine/include/qcom-base.inc
require conf/machine/include/qcom-common.inc

DEFAULTTUNE = "armv8-2a-crypto"
require conf/machine/include/arm/arch-armv8-2a.inc

# Disable PAuth via cmdline to allow all cores to boot with KVM.
# More details at https://lore.kernel.org/all/3fcf6614-ee83-4a06-9024-83573b2e642e@quicinc.com/
KERNEL_CMDLINE_EXTRA:append = " arm64.nopauth"

# Without the kvm machine feature the board boots under Gunyah, so pre-seed
# the VendorDtbOverlays UEFI variable with the el2gh overlay selector.
QCOM_VENDOR_DTB_OVERLAYS ?= "${@bb.utils.contains("MACHINE_FEATURES", "kvm", "", "el2gh", d)}"

MACHINE_ESSENTIAL_EXTRA_RRECOMMENDS += " \
    packagegroup-qcom-boot-essential \
    packagegroup-machine-essential-qcom-qcs8300-soc \
"

MACHINE_EXTRA_RRECOMMENDS += " \
    packagegroup-qcom-boot-additional \
"
