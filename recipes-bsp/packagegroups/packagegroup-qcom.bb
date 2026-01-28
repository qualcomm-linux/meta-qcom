SUMMARY = "Extra userspace packages for QCOM platforms"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

PACKAGES = " \
    ${PN}-boot-essential \
    ${PN}-boot-additional \
    ${PN}-miscellaneous \
"

RDEPENDS:${PN}-boot-essential = " \
    pd-mapper \
    qrtr \
    rmtfs \
    virtual-tftp-server \
"

RDEPENDS:${PN}-boot-additional:append:aarch64 = " \
    fastrpc \
"
