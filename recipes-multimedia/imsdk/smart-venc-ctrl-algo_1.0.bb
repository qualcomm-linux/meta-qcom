SUMMARY = "Smart Video Encoder Control Algorithm Prebuilt Libraries"
DESCRIPTION = "Provides prebuilt binaries for the Smart Video Encoder Control Algorithm, used to dynamically optimize video encoding parameters and performance."
LICENSE = "LicenseRef-LICENSE.qcom-2"
LIC_FILES_CHKSUM = "file://${UNPACKDIR}/usr/share/doc/qcom-video-ctrl/NO.LOGIN.BINARY.LICENSE.QTI;md5=eabe5444aa94c3e0e8b37b132a94e08b"

PBT_BUILD_DATE = "260112.1"

SRC_URI = "https://qartifactory-edge.qualcomm.com/artifactory/qsc_releases/software/chip/component/iot-core-algs.lnx.0.0/${PBT_BUILD_DATE}/prebuilt_yocto/qcom-video-ctrl_${PV}_armv8-2a.tar.gz"

SRC_URI[sha256sum] = "9e6b8b6e0b013b6126fe6ab0776591347f0f66fd7153f4afa8e100b9d64af308"

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
