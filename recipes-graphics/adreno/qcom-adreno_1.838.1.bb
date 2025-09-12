LICENSE = "CLOSED"

DESCRIPTION = "Qualcomm Adreno Graphics User Mode libraries"

SRC_URI = "https://qartifactory-edge.qualcomm.com/artifactory/qsc_releases/software/chip/component/gfx-adreno.le.0.0/250908/prebuilt_yocto/${BPN}_${PV}_armv8-2a.tar.gz;subdir=${BPN}-${PV}"

SRC_URI[sha256sum] = "02974d294597994a255f7aca6d6d61422fb44f11bd4cf6064e183c3888ea9def"

# These are listed here in order to identify RDEPENDS
DEPENDS += " glib-2.0 libdmabufheap libglvnd libdrm virtual/libgbm msm-gbm-backend \
             ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'wayland', '', d)} \
             ${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'libxcb libx11 xcb-util-image', '', d)}"

PACKAGE_BEFORE_PN += " ${PN}-common ${PN}-gles ${PN}-gles2 ${PN}-egl ${PN}-vulkan ${PN}-cl"

RPROVIDES:${PN}-common = "${PN}-common"
RPROVIDES:${PN}-egl = "${PN}-egl"
RPROVIDES:${PN}-cl = "${PN}-cl"

# Use dynamically renamed package names
RPROVIDES:${PN}-gles = "libglesv1-cm-adreno1"
RPROVIDES:${PN}-gles2 = "libglesv2-adreno2"
RPROVIDES:${PN}-vulkan = "libvulkan-adreno1"

RDEPENDS:${PN}-common += " kgsl-dlkm"
RDEPENDS:${PN}-egl += " ${PN}-common"
RDEPENDS:${PN}-gles2 += " ${PN}-common ${PN}-egl"
RDEPENDS:${PN}-gles += " ${PN}-gles2"
RDEPENDS:${PN}-vulkan += " vulkan-loader ${PN}-common"
RDEPENDS:${PN}-cl += " ${PN}-common"

do_install () {
    install -d ${D}/${libdir}
    cp -r ${S}/usr/lib/* ${D}/${libdir}/

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
FILES:${PN}-gles = "${libdir}/libGLESv1*.so.*"
FILES:${PN}-vulkan = "${libdir}/libvulkan_*.so.* \
                      ${datadir}/vulkan/icd.d/adrenovk.json"
FILES:${PN}-cl = "${libdir}/libOpenCL_*.so.* \
                  ${libdir}/libCB.so.1 \
                  ${sysconfdir}/OpenCL/vendors/adrenocl.icd"
FILES:${PN} = ""

# Prebuilt libraries are already stripped
INSANE_SKIP:${PN} = "already-stripped"
