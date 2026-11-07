SUMMARY = "CamX camera module with core interface utilities, extended image processing libraries, and CHI developer kit including sensor, tuning binaries"
DESCRIPTION = "This recipe introduces Qualcomm CamX camera module which creates three components.\
   camxlib: Includes common utilities, image processing algorithms and hardware support libraries extending CamX functionality for platform-specific enhancements. \
   camx: Core CamX engine that manages camera pipelines, mediates between camera clients and hardware, and exposes structured interfaces to higher-level frameworks. \
   chicdk: Camera hardware interface development kit delivering a configurable mechanism for use case selection and camera pipeline topology creation. \
   Includes configurable sensor and tuning binaries, which are essential for enabling full camera functionality."
LICENSE = "LICENSE.qcom-2"
LIC_FILES_CHKSUM = "file://usr/share/doc/${BPN}/LICENSE.QCOM-2.txt;md5=165287851294f2fb8ac8cbc5e24b02b0 \
                    file://usr/share/doc/${BPN}/NOTICE;md5=04facc2e07e3d41171a931477be0c690"

PBT_BUILD_DATE = "261026"
PBT_BRANCH = "wrynose"
SRC_URI = " \
   https://qartifactory-edge.qualcomm.com/artifactory/qsc_releases/software/chip/component/camx.qclinux.0.0/${PBT_BUILD_DATE}/prebuilt_yocto_${PBT_BRANCH}/${BPN}_${PV}_armv8-2a.tar.gz;name=camxlib \
   https://qartifactory-edge.qualcomm.com/artifactory/qsc_releases/software/chip/component/camx.qclinux.0.0/${PBT_BUILD_DATE}/prebuilt_yocto_${PBT_BRANCH}/camx-kodiak_${PV}_armv8-2a.tar.gz;name=camx \
   https://qartifactory-edge.qualcomm.com/artifactory/qsc_releases/software/chip/component/camx.qclinux.0.0/${PBT_BUILD_DATE}/prebuilt_yocto_${PBT_BRANCH}/chicdk-kodiak_${PV}_armv8-2a.tar.gz;name=chicdk \
   https://qartifactory-edge.qualcomm.com/artifactory/qsc_releases/software/chip/component/camx.qclinux.0.0/${PBT_BUILD_DATE}/prebuilt_yocto_${PBT_BRANCH}/camxtest-kodiak_${PV}_armv8-2a.tar.gz;name=camxtest \
   "
SRC_URI[camxlib.sha256sum] = "d5641f0d490e986302a0613dcae3eb1d916de38231d5b22d6521139f95f9647b"
SRC_URI[camx.sha256sum] = "cbccf6483cbac093ac899fac782a03316a453ebf9adba84ec1815dbbdd0d5a56"
SRC_URI[chicdk.sha256sum] = "69bf94eecce6451f59d0427718b69f2e18d4bdf9d49aabcb533bfa106e577f5f"
SRC_URI[camxtest.sha256sum] = "9d94036a5d6afc5f775232f069179528d89e9781a2632980334199817312f8d8"

S = "${UNPACKDIR}"

DEPENDS += "glib-2.0 fastrpc protobuf libxml2 qmi-framework sensinghub qcom-sensors-binaries"

DEPENDS += " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'opengl', 'virtual/egl virtual/libgles2', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'opencl', 'virtual/libopencl1', '', d)} \
"

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

    # Remove OpenCL-dependent libraries when opencl is not enabled.
    if ${@bb.utils.contains('DISTRO_FEATURES', 'opencl', 'false', 'true', d)}; then
        rm -f ${D}${libdir}/camx/kodiak/*.cl
        rm -f ${D}${libdir}/camx/kodiak/lib_algo_svhdr*
        rm -f ${D}${libdir}/camx/kodiak/lib_algo_rgbir*
        rm -f ${D}${libdir}/camx/kodiak/camera/components/libshdr3*
        rm -f ${D}${libdir}/camx/kodiak/camera/components/libbanding_correction*
    fi

    # Remove the libraries that need both OpenGL/EGL and OpenCL when either
    # opengl or opencl is not enabled.
    if ${@bb.utils.contains('DISTRO_FEATURES', 'opengl opencl', 'false', 'true', d)}; then
        rm -f ${D}${libdir}/camx/kodiak/camera/components/libiwarp*
    fi

    # copy Deep Learning based binary
    cp -r ${S}/usr/share/camx ${D}${datadir}
    # copy skel file
    cp -r ${S}/usr/share/qcom ${D}${datadir}

    # Install bin files only if /usr/bin exists in ${S}
    if [ -d "${S}${libexecdir}" ]; then
        install -d ${D}${libexecdir}
        cp -r ${S}/usr/libexec/* ${D}${libexecdir}
    fi

    # Remove unnecessary development symlinks (.so) from the staged image
    rm -f ${D}${libdir}/camx/kodiak/*${SOLIBSDEV}
    rm -f ${D}${libdir}/camx/kodiak/camera/components/*${SOLIBSDEV}
    rm -f ${D}${libdir}/camx/kodiak/hw/*${SOLIBSDEV}
    rm -f ${D}${libdir}/camx/kodiak/camera/*${SOLIBSDEV}

    # Remove  legacy libcamera* libs from the staged image
    rm -f ${D}${libdir}/libcamera*

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

PACKAGE_BEFORE_PN += "camx-kodiak chicdk-kodiak ${PN}-skel"
RDEPENDS:${PN} += "chicdk-kodiak ${PN}-skel"
RDEPENDS:${PN}-dev += "camxcommon-headers-dev"
RRECOMMENDS:${PN} += "sensinghub"

FILES:camx-kodiak = "\
    ${libdir}/libcamx_hardware_kodiak*${SOLIBS} \
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
    ${libdir}/libcamxchiofflinepostproclib_kodiak*${SOLIBS} \
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
    ${libexecdir}/ \
    "
FILES:${PN}-skel = "\
    ${datadir}/camx \
    ${datadir}/qcom \
    "
FILES:${PN} = "\
    ${libdir}/libcamx_metadata_kodiak*${SOLIBS} \
    ${libdir}/camx/kodiak/*${SOLIBS} \
    ${libdir}/camx/kodiak/hw/*${SOLIBS} \
    ${libdir}/camx/kodiak/camera/components/*${SOLIBS} \
    "
FILES:${PN}-dev = "\
    ${libdir}/*${SOLIBSDEV} \
    "
FILES:${PN}-staticdev = "${libdir}/camx/kodiak/*.a"

# Preserve ${PN},${PN}-skel naming to avoid ambiguity in package identification.
DEBIAN_NOAUTONAME:${PN} = "1"
DEBIAN_NOAUTONAME:${PN}-skel = "1"

# Algo librarires are pre-compiled, pre-stripped.
# Skipping QA checks: 'already-stripped', 'arch', 'libdir' because:
# - Library files are Pre-stripped  (already-stripped)
# - skel binaries/library are not AArch64 (arch mismatch)      (arch)
# - Files are installed under /usr/share (non-libdir path) (libdir)
INSANE_SKIP:${PN} = "already-stripped"
INSANE_SKIP:${PN}-skel += " arch libdir already-stripped"
