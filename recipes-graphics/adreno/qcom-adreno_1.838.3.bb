SUMMARY = "Qualcomm Adreno Graphics User Mode libraries"

DESCRIPTION = "Collection of prebuilt User Mode libraries to support OpenGL ES, Vulkan, and OpenCL APIs for Qualcomm Adreno GPUs.\
               For Qualcomm-specific OpenCL extensions declared in cl_ext_qcom.h, documentation is available in the Adreno OpenCL SDK: \
               https://softwarecenter.qualcomm.com/catalog/item/Adreno_OpenCL_SDK"

LICENSE = "LICENSE.qcom-2"
LIC_FILES_CHKSUM = "file://NO.LOGIN.BINARY.LICENSE.QTI.pdf;md5=4ceffe94cb40cdce6d2f4fb93cc063d1 \
                    file://NOTICE;md5=18837ae43f290ad72cdcccead4d7700a "

# no top-level dir in the archive, unpack to subdir to prevent UNPACKDIR pollution
SRC_URI = "https://qartifactory-edge.qualcomm.com/artifactory/qsc_releases/software/chip/component/gfx-adreno.le.0.0/${PBT_BUILD_DATE}/prebuilt_yocto/${BPN}_${PV}_armv8-2a.tar.gz;subdir=${BPN}-${PV}"
PBT_BUILD_DATE = "251203"
SRC_URI[sha256sum] = "770e31ac848cc1aa0e94bde840222f8f0e9af411592fa7ede9bfe509c00b87d2"

# These are listed here in order to identify RDEPENDS
DEPENDS += " glib-2.0 libdmabufheap libdrm virtual/libgbm msm-gbm-backend \
             ${@bb.utils.contains('DISTRO_FEATURES', 'glvnd', 'libglvnd', '', d)} \
             ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'wayland', '', d)} \
             ${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'libxcb libx11 xcb-util-image', '', d)}"

inherit features_check

ANY_OF_DISTRO_FEATURES = "glvnd vulkan opencl"

COMPATIBLE_MACHINE = "^$"
# It should be armv8-2a, but then it wouldn't be possible to use it for
# qcom-armv8a machine.
COMPATIBLE_MACHINE:aarch64 = "(.*)"

PACKAGE_BEFORE_PN += " ${PN}-common ${PN}-gles1 ${PN}-gles2 ${PN}-egl ${PN}-vulkan ${PN}-cl"

RPROVIDES:${PN}-egl += "virtual-egl-icd"
RPROVIDES:${PN}-cl += "virtual-opencl-icd"
RPROVIDES:${PN}-vulkan += "virtual-vulkan-icd"

RDEPENDS:${PN}-egl += " kgsl-dlkm ${PN}-common ${PN}-gles1 ${PN}-gles2 msm-gbm-backend"
RDEPENDS:${PN}-vulkan += " kgsl-dlkm ${PN}-common msm-gbm-backend"
RDEPENDS:${PN}-cl += " ${PN}-common"
RDEPENDS:${PN}-dev += "${@bb.utils.contains('DISTRO_FEATURES', 'opencl', 'opencl-headers-dev', '', d)}"

RDEPENDS:${PN} = " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'glvnd', 'qcom-adreno-egl', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'vulkan', 'qcom-adreno-vulkan', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'opencl', 'qcom-adreno-cl', '', d)} \
"

ALLOW_EMPTY:${PN} = "1"

do_install () {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'opencl', 'true', 'false', d)}; then
        install -d ${D}${includedir}/CL
        cp ${S}/usr/include/CL/cl_ext_qcom.h ${D}${includedir}/CL/
    fi

    install -d ${D}/${libdir}
    cp -r ${S}/usr/lib/* ${D}/${libdir}/

    if not ${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'true', 'false', d)}; then
        rm ${D}/${libdir}/libeglSubDriverX11.so*
    fi

    if not ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'true', 'false', d)}; then
        rm ${D}/${libdir}/libeglSubDriverWayland.so*
    fi

    install -d ${D}${datadir}/glvnd/egl_vendor.d
    cp ${S}/usr/share/glvnd/egl_vendor.d/EGL_adreno.json ${D}${datadir}/glvnd/egl_vendor.d/

    install -d ${D}${datadir}/vulkan/icd.d
    cp ${S}/usr/share/vulkan/icd.d/adrenovk.json ${D}${datadir}/vulkan/icd.d/

    install -d ${D}${sysconfdir}/OpenCL/vendors
    cp ${S}/etc/OpenCL/vendors/adrenocl.icd ${D}${sysconfdir}/OpenCL/vendors/

    install -d ${D}${sysconfdir}/modprobe.d
    cp ${S}/etc/modprobe.d/qcom-adreno.conf ${D}/${sysconfdir}/modprobe.d/qcom-adreno.conf
}

FILES:${PN}-common = "${libdir}/libllvm-*.so.* \
                      ${libdir}/libgsl.so.1 \
                      ${libdir}/libadreno_utils.so.1 \
                      ${libdir}/libq3dtools_*.so.* \
                      ${sysconfdir}/modprobe.d/qcom-adreno.conf \
                      ${sysconfdir}/profile.d/qcom-adreno_env.sh"
FILES:${PN}-egl = "${libdir}/libEGL_adreno.so.1 \
                   ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', '${libdir}/libeglSubDriverWayland.so.*', '', d)} \
                   ${@bb.utils.contains('DISTRO_FEATURES', 'x11', '${libdir}/libeglSubDriverX11.so.*', '', d)} \
                   ${datadir}/glvnd/egl_vendor.d/EGL_adreno.json"
FILES:${PN}-gles2 = "${libdir}/libGLESv2*.so.*"
FILES:${PN}-gles1 = "${libdir}/libGLESv1*.so.*"
FILES:${PN}-vulkan = "${libdir}/libvulkan_*.so.* \
                      ${datadir}/vulkan/icd.d/adrenovk.json"
FILES:${PN}-cl = "${libdir}/libOpenCL_*.so.* \
                  ${libdir}/libCB.so.1 \
                  ${sysconfdir}/OpenCL/vendors/adrenocl.icd"
FILES:${PN} = ""

# Prebuilt libraries are already stripped
INSANE_SKIP:${PN} = "already-stripped"
