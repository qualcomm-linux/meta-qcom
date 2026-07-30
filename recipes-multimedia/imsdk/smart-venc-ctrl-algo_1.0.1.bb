SUMMARY = "Smart Video Encoder Control Algorithm Prebuilt Libraries"
DESCRIPTION = "Provides prebuilt binaries for the Smart Video Encoder Control Algorithm, used to dynamically optimize video encoding parameters and performance."
LICENSE = "LICENSE.qcom-2"
LIC_FILES_CHKSUM = "file://${UNPACKDIR}/usr/share/doc/qcom-video-ctrl/LICENSE.qcom-2;md5=165287851294f2fb8ac8cbc5e24b02b0"

PBT_BUILD_DATE = "260709"

SRC_URI = "https://softwarecenter.qualcomm.com/nexus/generic/software/chip/component/iot-core-algs.lnx.0.0/${PBT_BUILD_DATE}/prebuilt_yocto/qcom-video-ctrl_${PV}_armv8a.tar.gz"

SRC_URI[sha256sum] = "72fd2cbe5ab1e12c718d67f9a0a9687063b7b22a1c2c5c4bad1eca6d2eedfe14"

S = "${UNPACKDIR}"

# Dependencies.
DEPENDS += "glib-2.0 qcom-fastcv-binaries"

# This package is currently only used and tested on ARMv8 (aarch64) machines.
# Therefore, builds for other architectures are not necessary and are explicitly excluded.
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:aarch64 = "(.*)"

do_install() {
    install -d ${D}${includedir}
    install -d ${D}${libdir}

    # Install headers
    cp -r ${S}/usr/include/* ${D}${includedir}

    # Install libs
    cp -r ${S}/usr/lib/* ${D}${libdir}
}
