SUMMARY = "Extra userspace packages for QCOM platforms"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

PACKAGES = " \
    ${PN}-boot-essential \
    ${PN}-boot-additional \
    ${PN}-miscellaneous \
"

RDEPENDS:${PN}-boot-essential = " \
    qrtr \
    rmtfs \
    tqftpserv \
    rpmsgexport \
"

RDEPENDS:${PN}-boot-additional:append:aarch64 = " \
    fastrpc \
"
