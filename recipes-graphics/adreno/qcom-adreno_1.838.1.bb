inherit pkgconfig

ARTIFACTORY_URL = "https://qartifactory-edge.qualcomm.com/artifactory/qsc_releases/software/chip/component/gfx-adreno.le.0.0/250828.1/prebuilt_yocto"

LICENSE          = "NO.LOGIN.BINARY.LICENSE.QTI.pdf"
LIC_FILES_CHKSUM = "file://${UNPACKDIR}/${LICENSE};md5=4ceffe94cb40cdce6d2f4fb93cc063d1"
LICENSE_PATH     +=  "${UNPACKDIR}"

DESCRIPTION = "Qualcomm Adreno Graphics User Mode libraries"

DEPENDS = "wayland glib-2.0 libdmabufheap libglvnd libdrm libxcb libx11 xcb-util-image virtual/libgbm msm"

SRC_URI = "${ARTIFACTORY_URL}/${BPN}_${PV}_armv8-2a.tar.gz"
SRC_URI[sha256sum] = "7d59e09799efaf7e80efae270341339042717d715ec78af0d6440925463159d6"

S = "${UNPACKDIR}"

do_install () {
    install -d ${D}${includedir}/adreno
    cp -rf ${S}${includedir}/adreno/* ${D}${includedir}/adreno/

    install -d ${D}/${libdir}
    cp -r ${S}/${libdir}/* ${D}/${libdir}/

    install -d ${D}/lib/firmware
    cp -rf ${S}/lib/firmware/* ${D}/lib/firmware/

    install -d ${D}/usr/share/glvnd/egl_vendor.d
    cp ${S}/usr/share/glvnd/egl_vendor.d/EGL_adreno.json ${D}/usr/share/glvnd/egl_vendor.d/

    install -d ${D}/usr/share/vulkan/icd.d
    cp ${S}/usr/share/vulkan/icd.d/adrenovk.json ${D}/usr/share/vulkan/icd.d/

    install -d ${D}/etc/OpenCL/vendors
    cp ${S}/etc/OpenCL/vendors/adrenocl.icd ${D}/etc/OpenCL/vendors/

    install -d ${D}/${sysconfdir}/profile.d
    cp ${S}/${sysconfdir}/profile.d/qcom-adreno_env.sh ${D}/${sysconfdir}/profile.d/qcom-adreno_env.sh

    install -d ${D}/${sysconfdir}/modprobe.d
    cp ${S}/${sysconfdir}/modprobe.d/qcom-adreno.conf ${D}/${sysconfdir}/modprobe.d/qcom-adreno.conf
}

INSANE_SKIP:${PN} = "already-stripped arch usrmerge"

FILES:${PN} = "${libdir}/lib*.so.* \
               /lib/firmware/* \
               /usr/share/glvnd/egl_vendor.d/EGL_adreno.json \
               /usr/share/vulkan/icd.d/adrenovk.json \
               /etc/OpenCL/vendors/adrenocl.icd \
               ${sysconfdir}/profile.d/qcom-adreno_env.sh \
               ${sysconfdir}/modprobe.d/qcom-adreno.conf "

FILES:${PN}-dev = "${includedir}/adreno \
                   ${libdir}/pkgconfig \
                   ${libdir}/lib*.so "

FILES:${PN}-dbg = ""
