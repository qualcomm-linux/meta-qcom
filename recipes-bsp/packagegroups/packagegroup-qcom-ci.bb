SUMMARY = "Extra userspace packages enabled for CI testing"

inherit packagegroup

# libssc depends on libqmi and protobuf which are part of meta-oe
RDEPENDS:${PN} = " \
    firmware-qcom-boot-dragonboard410c \
    firmware-qcom-boot-dragonboard820c \
    hexagonrpc \
    ${@bb.utils.contains("BBLAYERS", "openembedded-layer", "libssc","", d)} \
    libvmmem-dev \
    libdmabufheap-dev \
"
