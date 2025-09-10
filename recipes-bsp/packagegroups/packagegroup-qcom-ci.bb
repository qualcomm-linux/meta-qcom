SUMMARY = "Extra userspace packages enabled for CI testing"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

# libssc depends on libqmi and protobuf which are part of meta-oe
RDEPENDS:${PN} = " \
    firmware-qcom-boot-dragonboard410c \
    firmware-qcom-boot-dragonboard820c \
    hexagonrpc \
    ${@bb.utils.contains("BBLAYERS", "openembedded-layer", "libssc","", d)} \
    libvmmem-dev \
    libdmabufheap-dev \
    ${@bb.utils.contains("BBLAYERS", "openembedded-layer", "opencv","", d)} \
    sigma-dut \
"

# These packages support only ARMv8 machines, no other builds required.
RDEPENDS:${PN}:append:aarch64 = " \
    iris-video-dlkm \
    kgsl-dlkm \
    qcom-fastcv-binaries \
    packagegroup-qcom-graphics \
"
