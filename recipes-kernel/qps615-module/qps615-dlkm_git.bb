SUMMARY = "QPS615 PCIe Ethernet kernel module (Qualcomm fork)"
DESCRIPTION = "Kernel module for the QPS615 PCIe Ethernet \
bridge chip. Builds the host driver plus Qualcomm specific platform logic"

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${S}/drivers/net/ethernet/toshiba/tc956x/LICENSE;md5=5cecbcf0c040b635e20026c75b838d63"

inherit module

SRCREV = "2cbd7b9ddf2318d58044dcbd57dc187dfca55034"

SRC_URI = "git://github.com/qualcomm-linux/TC9564_Host_Driver.git;protocol=https;branch=qcom"

PV = "6.0.3+git"

B = "${S}/drivers/net/ethernet/toshiba/tc956x"

# devtool/externalsrc uses a separate build directory unless the out-of-tree
# build directory is explicitly provided.
EXTERNALSRC_BUILD = "${EXTERNALSRC}/drivers/net/ethernet/toshiba/tc956x"

# The original Makefile uses an "ifeq ($(pf), 1)" check to pick the default config.
# Therefore, pf=1 needs to be set while compiling for non-SRIOV VF config.
EXTRA_OEMAKE += "KCFLAGS='-DTC956X -DCONFIG_TC956X_PLATFORM_SUPPORT -DTC956X_SRIOV_PF' pf=1"

RDEPENDS:${PN} += "qps615-firmware"
