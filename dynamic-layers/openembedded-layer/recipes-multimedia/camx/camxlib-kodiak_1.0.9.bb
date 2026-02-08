SUMMARY = "CamX camera module with core interface utilities, extended image processing libraries, and CHI developer kit including sensor, tuning binaries"
DESCRIPTION = "This recipe introduces Qualcomm CamX camera module which creates three components.\
   camxlib: Includes common utilities, image processing algorithms and hardware support libraries extending CamX functionality for platform-specific enhancements. \
   camx: Core CamX engine that manages camera pipelines, mediates between camera clients and hardware, and exposes structured interfaces to higher-level frameworks. \
   chicdk: Camera hardware interface development kit delivering a configurable mechanism for use case selection and camera pipeline topology creation. \
   Includes configurable sensor and tuning binaries, which are essential for enabling full camera functionality."
LICENSE = "LICENSE.qcom-2"
LIC_FILES_CHKSUM = "file://usr/share/doc/${BPN}/NO.LOGIN.BINARY.LICENSE.QTI.pdf;md5=7a5da794b857d786888bbf2b7b7529c8 \
                    file://usr/share/doc/${BPN}/NOTICE;md5=04facc2e07e3d41171a931477be0c690"

PBT_BUILD_DATE = "260203"
SRC_URI = " \
   https://qartifactory-edge.qualcomm.com/artifactory/qsc_releases/software/chip/component/camx.qclinux.0.0/${PBT_BUILD_DATE}/prebuilt_yocto/${BPN}_${PV}_armv8-2a.tar.gz;name=camxlib \
   https://qartifactory-edge.qualcomm.com/artifactory/qsc_releases/software/chip/component/camx.qclinux.0.0/${PBT_BUILD_DATE}/prebuilt_yocto/camx-kodiak_${PV}_armv8-2a.tar.gz;name=camx \
   https://qartifactory-edge.qualcomm.com/artifactory/qsc_releases/software/chip/component/camx.qclinux.0.0/${PBT_BUILD_DATE}/prebuilt_yocto/chicdk-kodiak_${PV}_armv8-2a.tar.gz;name=chicdk \
   "
SRC_URI[camxlib.sha256sum] = "2c2ba360dd0b19637a983d2426188f9677e7fff12d3704361b995812fbc47e7d"
SRC_URI[camx.sha256sum] = "5f42f2ab165f49d0b39113baf2b7bd842d5ffa793d2cf44efc209fc8e121e9bd"
SRC_URI[chicdk.sha256sum] = "491f171b533fa7249ddfb4b32bfbe822ff4125021708d2e0f0a6737068b2eee4"

S = "${UNPACKDIR}"

DEPENDS += "glib-2.0 fastrpc protobuf-camx libxml2 virtual/egl virtual/libgles2 virtual/libopencl1"

# This package is currently only used and tested on ARMv8 (aarch64) machines.
# Therefore, builds for other architectures are not necessary and are explicitly excluded.
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:aarch64 = "(.*)"

# Disable configure and compile steps since this recipe uses prebuilt binaries.
do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}${libdir}
    install -d ${D}${datadir}/doc/${BPN}
    install -d ${D}${datadir}/doc/camx-kodiak
    install -d ${D}${datadir}/doc/chicdk-kodiak
    install -d ${D}${bindir}

    cp -r ${S}/usr/lib/* ${D}${libdir}
    cp -r ${S}/usr/bin/* ${D}${bindir}

    # Remove unnecessary development symlinks (.so) from the staged image
    rm -f ${D}${libdir}/camx/kodiak/*${SOLIBSDEV}
    rm -f ${D}${libdir}/camx/kodiak/camera/components/*${SOLIBSDEV}
    rm -f ${D}${libdir}/camx/kodiak/hw/*${SOLIBSDEV}
    rm -f ${D}${libdir}/camx/kodiak/camera/*${SOLIBSDEV}

    install -m 0644 ${S}/usr/share/doc/${BPN}/NOTICE ${D}${datadir}/doc/${BPN}
    install -m 0644 ${S}/usr/share/doc/${BPN}/NO.LOGIN.BINARY.LICENSE.QTI.pdf ${D}${datadir}/doc/${BPN}

    install -m 0644 ${S}/usr/share/doc/camx-kodiak/NOTICE ${D}${datadir}/doc/camx-kodiak
    install -m 0644 ${S}/usr/share/doc/${BPN}/NO.LOGIN.BINARY.LICENSE.QTI.pdf ${D}${datadir}/doc/camx-kodiak

    install -m 0644 ${S}/usr/share/doc/chicdk-kodiak/NOTICE ${D}${datadir}/doc/chicdk-kodiak
    install -m 0644 ${S}/usr/share/doc/${BPN}/NO.LOGIN.BINARY.LICENSE.QTI.pdf ${D}${datadir}/doc/chicdk-kodiak
}

PACKAGE_BEFORE_PN += "camx-kodiak chicdk-kodiak"
RDEPENDS:${PN} += "chicdk-kodiak"
RDEPENDS:${pn}-dev += "camxcommon-headers-dev"

FILES:camx-kodiak = "\
    ${libdir}/libcamera_hardware_kodiak*${SOLIBS} \
    ${libdir}/libcamxexternalformatutils_kodiak*${SOLIBS} \
    ${libdir}/camx/kodiak/libchilog*${SOLIBS} \
    ${libdir}/camx/kodiak/camera/components/com.qti.node.eisv*${SOLIBS} \
    ${libdir}/camx/kodiak/camera/components/com.qti.node.swregistration*${SOLIBS} \
    ${libdir}/camx/kodiak/hw/camera.qcom*${SOLIBS} \
    ${libdir}/camx/kodiak/libcamera_hardware*${SOLIBS} \
    ${libdir}/camx/kodiak/libcamxexternalformatutils*${SOLIBS} \
    ${libdir}/camx/kodiak/libcom.qti.camx.chiiqutils*${SOLIBS} \
    ${libdir}/camx/kodiak/libcom.qti.node.eisv*${SOLIBS} \
    "
FILES:chicdk-kodiak = "\
    ${libdir}/camx/kodiak/com.qti.feature2*${SOLIBS} \
    ${libdir}/camx/kodiak/libchimldw*${SOLIBS} \
    ${libdir}/camx/kodiak/com.qualcomm*${SOLIBS} \
    ${libdir}/camx/kodiak/chiofflinepostproclib*${SOLIBS} \
    ${libdir}/camx/kodiak/libcommonchiutils*${SOLIBS} \
    ${libdir}/camx/kodiak/libiccprofile*${SOLIBS} \
    ${libdir}/camx/kodiak/com.qti.chiusecaseselector*${SOLIBS} \
    ${libdir}/camx/kodiak/camera/components/com.qti.node*${SOLIBS} \
    ${libdir}/camx/kodiak/camera/com.qti.sensormodule*${SOLIBS} \
    ${libdir}/camx/kodiak/camera/components/libshdr3*${SOLIBS} \
    ${libdir}/camx/kodiak/camera/components/libbanding_correction*${SOLIBS} \
    ${libdir}/camx/kodiak/camera/components/com.qti.stats.hafoverride*${SOLIBS} \
    ${libdir}/camx/kodiak/camera/*.bin \
    ${libdir}/camx/kodiak/camera/com.qti.sensor*${SOLIBS} \
    ${libdir}/camx/kodiak/hw/com.qti.chi.*${SOLIBS} \
    ${bindir}/ \
    "
FILES:${PN} = "\
    ${libdir}/libcamera_metadata_kodiak*${SOLIBS} \
    ${libdir}/camx/kodiak/*${SOLIBS} \
    ${libdir}/camx/kodiak/hw/*${SOLIBS} \
    ${libdir}/camx/kodiak/camera/components/*${SOLIBS} \
    "
FILES:${PN}-dev = "\
    ${libdir}/*${SOLIBSDEV} \
    "
# Preserve ${PN} naming to avoid ambiguity in package identification.
DEBIAN_NOAUTONAME:${PN} = "1"
