SUMMARY = "Extra userspace packages for QCOM platforms"

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
    tqftpserv \
"

RDEPENDS:${PN}-boot-additional = " \
    fastrpc \
"

# libssc depends on libqmi and protobuf which are part of meta-oe
RDEPENDS:${PN}-miscellaneous = " \
    hexagonrpc \
    ${@bb.utils.contains("BBLAYERS", "openembedded-layer", "libssc","", d)} \
    libvmmem-dev \
    libdmabufheap-dev \
"
