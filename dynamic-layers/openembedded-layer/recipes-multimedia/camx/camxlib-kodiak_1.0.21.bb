SUMMARY = "CamX camera module with core interface utilities, extended image processing libraries, and CHI developer kit including sensor, tuning binaries"
DESCRIPTION = "This recipe introduces Qualcomm CamX camera module which creates three components.\
   camxlib: Includes common utilities, image processing algorithms and hardware support libraries extending CamX functionality for platform-specific enhancements. \
   camx: Core CamX engine that manages camera pipelines, mediates between camera clients and hardware, and exposes structured interfaces to higher-level frameworks. \
   chicdk: Camera hardware interface development kit delivering a configurable mechanism for use case selection and camera pipeline topology creation. \
   Includes configurable sensor and tuning binaries, which are essential for enabling full camera functionality."
LICENSE = "LICENSE.qcom-2"
LIC_FILES_CHKSUM = "file://usr/share/doc/${BPN}/LICENSE.QCOM-2.txt;md5=165287851294f2fb8ac8cbc5e24b02b0 \
                    file://usr/share/doc/${BPN}/NOTICE;md5=04facc2e07e3d41171a931477be0c690"

PBT_BUILD_DATE = "260512.2"
SRC_URI = " \
   https://qartifactory-edge.qualcomm.com/artifactory/qsc_releases/software/chip/component/camx.qclinux.0.0/${PBT_BUILD_DATE}/prebuilt_yocto/${BPN}_${PV}_armv8-2a.tar.gz;name=camxlib \
   https://qartifactory-edge.qualcomm.com/artifactory/qsc_releases/software/chip/component/camx.qclinux.0.0/${PBT_BUILD_DATE}/prebuilt_yocto/camx-kodiak_${PV}_armv8-2a.tar.gz;name=camx \
   https://qartifactory-edge.qualcomm.com/artifactory/qsc_releases/software/chip/component/camx.qclinux.0.0/${PBT_BUILD_DATE}/prebuilt_yocto/chicdk-kodiak_${PV}_armv8-2a.tar.gz;name=chicdk \
   "
SRC_URI[camxlib.sha256sum] = "2edfdff2ff6ee08d2c1ff5f7b807235ec4916091337f1467cb40e61337d22017"
SRC_URI[camx.sha256sum] = "04de35828b2d151564df1088c4a6f60b221620dc1b6439776d1eaae2cc44a773"
SRC_URI[chicdk.sha256sum] = "0650383e28123b51aea279a8dc84d5f09dd9c1be0aef6032820536315041eb6e"

S = "${UNPACKDIR}"

DEPENDS += "glib-2.0 fastrpc protobuf-camx libxml2 virtual/egl virtual/libgles2 qmi-framework sensinghub qcom-sensors-binaries \
    ${@bb.utils.contains('DISTRO_FEATURES', 'opencl', 'qcom-adreno virtual/libopencl1', '', d)}"

# This package is currently only used and tested on ARMv8 (aarch64) machines.
# Therefore, builds for other architectures are not necessary and are explicitly excluded.
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:aarch64 = "(.*)"

# Use do_install:append to preserve cmake_do_install
do_install:append() {
    install -d ${D}${libdir}
    install -d ${D}${datadir}/doc/${BPN}
    install -d ${D}${datadir}/doc/camx-kodiak
    install -d ${D}${datadir}/doc/chicdk-kodiak

    cp -r ${S}/usr/lib/* ${D}${libdir}

    # Install bin files only if /usr/bin exists in ${S}
    if [ -d "${S}${bindir}" ]; then
        install -d ${D}${bindir}
        cp -r ${S}/usr/bin/* ${D}${bindir}
    fi

    # Remove unnecessary development symlinks (.so) from the staged image
    rm -f ${D}${libdir}/camx/kodiak/*${SOLIBSDEV}
    rm -f ${D}${libdir}/camx/kodiak/camera/components/*${SOLIBSDEV}
    rm -f ${D}${libdir}/camx/kodiak/hw/*${SOLIBSDEV}
    rm -f ${D}${libdir}/camx/kodiak/camera/*${SOLIBSDEV}

    install -m 0644 ${S}/usr/share/doc/${BPN}/NOTICE ${D}${datadir}/doc/${BPN}
    install -m 0644 ${S}/usr/share/doc/${BPN}/LICENSE.QCOM-2.txt ${D}${datadir}/doc/${BPN}

    # install camx docs only if /usr/share/doc/camx-kodiak exists in ${S}
    if [ -d "${S}/usr/share/doc/camx-kodiak" ]; then
        install -m 0644 ${S}/usr/share/doc/camx-kodiak/NOTICE ${D}${datadir}/doc/camx-kodiak
        install -m 0644 ${S}/usr/share/doc/${BPN}/LICENSE.QCOM-2.txt ${D}${datadir}/doc/camx-kodiak
    fi

    # install chicdk docs only if /usr/share/doc/chicdk-kodiak exists in ${S}
    if [ -d "${S}/usr/share/doc/chicdk-kodiak" ]; then
        install -m 0644 ${S}/usr/share/doc/chicdk-kodiak/NOTICE ${D}${datadir}/doc/chicdk-kodiak
        install -m 0644 ${S}/usr/share/doc/${BPN}/LICENSE.QCOM-2.txt ${D}${datadir}/doc/chicdk-kodiak
    fi
}

PACKAGE_BEFORE_PN += "camx-kodiak chicdk-kodiak"
RDEPENDS:${PN} += "chicdk-kodiak"
RDEPENDS:${PN}-dev += "camxcommon-headers-dev"
RRECOMMENDS:${PN} += "${@bb.utils.contains('DISTRO_FEATURES', 'opencl', 'virtual-opencl-icd', '', d)} sensinghub qcom-sensors-binaries"

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
    ${@bb.utils.contains('DISTRO_FEATURES', 'opencl', '${libdir}/camx/kodiak/*.cl', '', d)} \
    "
FILES:${PN}-dev = "\
    ${libdir}/*${SOLIBSDEV} \
    "
FILES:${PN}-staticdev = "${libdir}/camx/kodiak/*.a"

# Preserve ${PN} naming to avoid ambiguity in package identification.
DEBIAN_NOAUTONAME:${PN} = "1"

# Algo librarires are pre-compiled, pre-stripped.
# Skipping QA checks: 'already-stripped' because:
# - Library files are Pre-stripped  (already-stripped)
INSANE_SKIP:${PN} = "already-stripped"
