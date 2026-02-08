SUMMARY = "CamX camera ${PLATFORM} module with core interfaces, image processing libraries"
DESCRIPTION = "This recipe introduces the Qualcomm CamX camera module which creates \
three components. \
camxlib: Includes common utilities, image processing algorithms \
and hardware support libraries extending CamX functionality for platform-specific \
enhancements. \
camx: Core CamX engine that manages camera pipelines, mediates \
between camera clients and hardware, and exposes structured interfaces to \
higher-level frameworks. \
chicdk: Camera hardware interface development kit delivering a configurable \
mechanism for use case selection and camera pipeline topology creation. \
Includes configurable sensor and tuning binaries, which are essential for enabling \
full camera functionality."

LICENSE = "LICENSE.qcom-2"
LIC_FILES_CHKSUM = "file://usr/share/doc/${BPN}/NO.LOGIN.BINARY.LICENSE.QTI.pdf;md5=7a5da794b857d786888bbf2b7b7529c8 \
                    file://usr/share/doc/${BPN}/NOTICE;md5=198d001f49d9a313355d5219f669a76c"

SRC_URI = " \
    https://qartifactory-edge.qualcomm.com/artifactory/qsc_releases/software/chip/component/camx.qclinux.0.0/${PBT_BUILD_DATE}/prebuilt_yocto/${BPN}_${PV}_armv8-2a.tar.gz;name=camxlib \
    https://qartifactory-edge.qualcomm.com/artifactory/qsc_releases/software/chip/component/camx.qclinux.0.0/${PBT_BUILD_DATE}/prebuilt_yocto/camx-${PLATFORM}_${PV}_armv8-2a.tar.gz;name=camx \
    https://qartifactory-edge.qualcomm.com/artifactory/qsc_releases/software/chip/component/camx.qclinux.0.0/${PBT_BUILD_DATE}/prebuilt_yocto/chicdk-${PLATFORM}_${PV}_armv8-2a.tar.gz;name=chicdk \
    https://qartifactory-edge.qualcomm.com/artifactory/qsc_releases/software/chip/component/camx.qclinux.0.0/${PBT_BUILD_DATE}/prebuilt_yocto/camxcommon-${PLATFORM}_${PV}_armv8-2a.tar.gz;name=camxcommon \
"

S = "${UNPACKDIR}"

DEPENDS += "glib-2.0 fastrpc protobuf-camx libxml2"

# This package is currently only used and tested on ARMv8 (aarch64) machines.
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:aarch64 = "(.*)"

# Prebuilt binaries: skip configure/compile
do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}${libdir}
    install -d ${D}${bindir}
    install -d ${D}${datadir}/doc/${BPN}
    install -d ${D}${datadir}/doc/camx-${PLATFORM}
    install -d ${D}${datadir}/doc/chicdk-${PLATFORM}

    cp -r ${S}/usr/lib/* ${D}${libdir}
    cp -r ${S}/usr/bin/* ${D}${bindir}

    # Remove unnecessary development symlinks (.so) from the staged image
    rm -f ${D}${libdir}/camx/${PLATFORM}/*${SOLIBSDEV}
    rm -f ${D}${libdir}/camx/${PLATFORM}/camera/components/*${SOLIBSDEV}
    rm -f ${D}${libdir}/camx/${PLATFORM}/hw/*${SOLIBSDEV}
    rm -f ${D}${libdir}/camx/${PLATFORM}/camera/*${SOLIBSDEV}

    # Base docs
    install -m 0644 ${S}/usr/share/doc/${BPN}/NOTICE \
        ${D}${datadir}/doc/${BPN}
    install -m 0644 ${S}/usr/share/doc/${BPN}/NO.LOGIN.BINARY.LICENSE.QTI.pdf \
        ${D}${datadir}/doc/${BPN}

    # camx docs
    install -m 0644 ${S}/usr/share/doc/camx-${PLATFORM}/NOTICE \
        ${D}${datadir}/doc/camx-${PLATFORM}
    install -m 0644 ${S}/usr/share/doc/${BPN}/NO.LOGIN.BINARY.LICENSE.QTI.pdf \
        ${D}${datadir}/doc/camx-${PLATFORM}

    # chicdk docs
    install -m 0644 ${S}/usr/share/doc/chicdk-${PLATFORM}/NOTICE \
        ${D}${datadir}/doc/chicdk-${PLATFORM}
    install -m 0644 ${S}/usr/share/doc/${BPN}/NO.LOGIN.BINARY.LICENSE.QTI.pdf \
        ${D}${datadir}/doc/chicdk-${PLATFORM}
}


PACKAGE_BEFORE_PN += "camx-${PLATFORM} chicdk-${PLATFORM}"
RDEPENDS:${PN} += "chicdk-${PLATFORM}"
RDEPENDS:${PN}-dev += "camxcommon-headers-dev"

FILES:camx-${PLATFORM} = "\
    ${libdir}/libcamera_hardware_${PLATFORM}*${SOLIBS} \
    ${libdir}/libcamxexternalformatutils_${PLATFORM}*${SOLIBS} \
    ${libdir}/camx/${PLATFORM}/hw/camera.qcom*${SOLIBS} \
    ${libdir}/camx/${PLATFORM}/libcamera_hardware*${SOLIBS} \
    ${libdir}/camx/${PLATFORM}/libcamxexternalformatutils*${SOLIBS} \
    ${libdir}/camx/${PLATFORM}/libcom.qti.camx.chiiqutils*${SOLIBS} \
    ${libdir}/camx/${PLATFORM}/libcom.qti.node.eisv*${SOLIBS} \
"

FILES:chicdk-${PLATFORM} = "\
    ${libdir}/camx/${PLATFORM}/com.qti.feature2*${SOLIBS} \
    ${libdir}/camx/${PLATFORM}/com.qualcomm*${SOLIBS} \
    ${libdir}/camx/${PLATFORM}/libcommonchiutils*${SOLIBS} \
    ${libdir}/camx/${PLATFORM}/libiccprofile*${SOLIBS} \
    ${libdir}/camx/${PLATFORM}/com.qti.chiusecaseselector*${SOLIBS} \
    ${libdir}/camx/${PLATFORM}/camera/components/com.qti.node*${SOLIBS} \
    ${libdir}/camx/${PLATFORM}/camera/com.qti.sensormodule*${SOLIBS} \
    ${libdir}/camx/${PLATFORM}/camera/*.bin \
    ${libdir}/camx/${PLATFORM}/camera/com.qti.sensor*${SOLIBS} \
    ${libdir}/camx/${PLATFORM}/hw/com.qti.chi.*${SOLIBS} \
    ${bindir}/camx \
"

FILES:${PN} = "\
    ${libdir}/libcamera_metadata_${PLATFORM}*${SOLIBS} \
    ${libdir}/camx/${PLATFORM}/*${SOLIBS} \
    ${libdir}/camx/${PLATFORM}/camera/components/com.qti.node.swregistration*${SOLIBS} \
    ${libdir}/camx/${PLATFORM}/hw/*${SOLIBS} \
    ${libdir}/camx/${PLATFORM}/camera/components/*${SOLIBS} \
"

FILES:${PN}-dev = "\
    ${libdir}/*${SOLIBSDEV} \
"

# Preserve ${PN} naming to avoid ambiguity in package identification.
DEBIAN_NOAUTONAME:${PN} = "1"
