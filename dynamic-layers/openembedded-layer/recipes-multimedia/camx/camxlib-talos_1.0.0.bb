require common.inc
LICENSE = "LICENSE.qcom-2"
LIC_FILES_CHKSUM = "file://usr/share/doc/${BPN}/NO.LOGIN.BINARY.LICENSE.QTI.pdf;md5=7a5da794b857d786888bbf2b7b7529c8 \
                    file://usr/share/doc/${BPN}/NOTICE;md5=198d001f49d9a313355d5219f669a76c"

PBT_BUILD_DATE = "260203"
SRC_URI = " \
    https://qartifactory-edge.qualcomm.com/artifactory/qsc_releases/software/chip/component/camx.qclinux.0.0/${PBT_BUILD_DATE}/prebuilt_yocto/${BPN}_${PV}_armv8-2a.tar.gz;name=camxlib \
    https://qartifactory-edge.qualcomm.com/artifactory/qsc_releases/software/chip/component/camx.qclinux.0.0/${PBT_BUILD_DATE}/prebuilt_yocto/camx-talos_${PV}_armv8-2a.tar.gz;name=camx \
    https://qartifactory-edge.qualcomm.com/artifactory/qsc_releases/software/chip/component/camx.qclinux.0.0/${PBT_BUILD_DATE}/prebuilt_yocto/chicdk-talos_${PV}_armv8-2a.tar.gz;name=chicdk \
    https://qartifactory-edge.qualcomm.com/artifactory/qsc_releases/software/chip/component/camx.qclinux.0.0/${PBT_BUILD_DATE}/prebuilt_yocto/camxcommon-talos_${PV}_armv8-2a.tar.gz;name=camxcommon \
    "

SRC_URI[camxlib.sha256sum] = "272ecd97c7be21dfb2f8a337260d5056fc5fcfe54cbbc95f61290cbac54f682d"
SRC_URI[camx.sha256sum] = "f50ffe2babe9db1b8c976115b4d3363d05cb16a89af2feecd80ef53588cb74b5"
SRC_URI[chicdk.sha256sum] = "1aaead1b2571578316d4d0f033a51e869d808096f57763d5ea1441dc8073674c"
SRC_URI[camxcommon.sha256sum] = "a208722d7fd742c81d9bc316a476c00e67b280447fc492caa74781cda64d17a3"

S = "${UNPACKDIR}"

do_install() {
    install -d ${D}${libdir}
    install -d ${D}${datadir}/doc/${BPN}
    install -d ${D}${datadir}/doc/camx-talos
    install -d ${D}${datadir}/doc/chicdk-talos
    install -d ${D}${bindir}

    cp -r ${S}/usr/lib/* ${D}${libdir}
    cp -r ${S}/usr/bin/* ${D}${bindir}

    # Remove unnecessary development symlinks (.so) from the staged image
    rm -f ${D}${libdir}/camx/talos/*${SOLIBSDEV}
    rm -f ${D}${libdir}/camx/talos/camera/components/*${SOLIBSDEV}
    rm -f ${D}${libdir}/camx/talos/hw/*${SOLIBSDEV}
    rm -f ${D}${libdir}/camx/talos/camera/*${SOLIBSDEV}

    install -m 0644 ${S}/usr/share/doc/${BPN}/NOTICE ${D}${datadir}/doc/${BPN}
    install -m 0644 ${S}/usr/share/doc/${BPN}/NO.LOGIN.BINARY.LICENSE.QTI.pdf ${D}${datadir}/doc/${BPN}

    install -m 0644 ${S}/usr/share/doc/camx-talos/NOTICE ${D}${datadir}/doc/camx-talos
    install -m 0644 ${S}/usr/share/doc/${BPN}/NO.LOGIN.BINARY.LICENSE.QTI.pdf ${D}${datadir}/doc/camx-talos

    install -m 0644 ${S}/usr/share/doc/chicdk-talos/NOTICE ${D}${datadir}/doc/chicdk-talos
    install -m 0644 ${S}/usr/share/doc/${BPN}/NO.LOGIN.BINARY.LICENSE.QTI.pdf ${D}${datadir}/doc/chicdk-talos
}

PACKAGE_BEFORE_PN += "camx-talos chicdk-talos"
RDEPENDS:${PN} += "chicdk-talos"

FILES:camx-talos = "\
    ${libdir}/libcamera_hardware_talos*${SOLIBS} \
    ${libdir}/libcamxexternalformatutils_talos*${SOLIBS} \
    ${libdir}/camx/talos/hw/camera.qcom*${SOLIBS} \
    ${libdir}/camx/talos/libcamera_hardware*${SOLIBS} \
    ${libdir}/camx/talos/libcamxexternalformatutils*${SOLIBS} \
    ${libdir}/camx/talos/libcom.qti.camx.chiiqutils*${SOLIBS} \
    ${libdir}/camx/talos/libcom.qti.node.eisv*${SOLIBS} \
    "
FILES:chicdk-talos = "\
    ${libdir}/camx/talos/com.qti.feature2*${SOLIBS} \
    ${libdir}/camx/talos/com.qualcomm*${SOLIBS} \
    ${libdir}/camx/talos/libcommonchiutils*${SOLIBS} \
    ${libdir}/camx/talos/libiccprofile*${SOLIBS} \
    ${libdir}/camx/talos/com.qti.chiusecaseselector*${SOLIBS} \
    ${libdir}/camx/talos/camera/components/com.qti.node*${SOLIBS} \
    ${libdir}/camx/talos/camera/com.qti.sensormodule*${SOLIBS} \
    ${libdir}/camx/talos/camera/*.bin \
    ${libdir}/camx/talos/camera/com.qti.sensor*${SOLIBS} \
    ${libdir}/camx/talos/hw/com.qti.chi.*${SOLIBS} \
    ${bindir}/camx \
    "
FILES:${PN} = "\
    ${libdir}/libcamera_metadata_talos*${SOLIBS} \
    ${libdir}/camx/talos/*${SOLIBS} \
    ${libdir}/camx/talos/camera/components/com.qti.node.swregistration*${SOLIBS} \
    ${libdir}/camx/talos/hw/*${SOLIBS} \
    ${libdir}/camx/talos/camera/components/*${SOLIBS} \
    "
FILES:${PN}-dev = "\
    ${libdir}/*${SOLIBSDEV} \
    "
# Preserve ${PN} naming to avoid ambiguity in package identification.
DEBIAN_NOAUTONAME:${PN} = "1"
