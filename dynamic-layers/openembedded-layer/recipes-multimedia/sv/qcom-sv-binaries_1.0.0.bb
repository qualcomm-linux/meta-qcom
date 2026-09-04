SUMMARY = "Snapdragon Vision API libraries for running hardware accelerated ComputerVision Algorithms"
DESCRIPTION = "SV delivers scalable, hardware- and software-agnostic computer vision APIs for diverse applications and platforms."
LICENSE = "LICENSE.qcom-2"
LIC_FILES_CHKSUM = "file://${UNPACKDIR}/usr/share/doc/${PN}/LICENSE.qcom-2.txt;md5=165287851294f2fb8ac8cbc5e24b02b0 \
                    file://${UNPACKDIR}/usr/share/doc/${PN}/NOTICE;md5=3d0b939e05e766ef65341b22c9acad07 "

PBT_BUILD_DATE = "260817"
PBT_ARCH = "armv8a"

SRC_URI = "https://qartifactory-edge.qualcomm.com/artifactory/qsc_releases/software/chip/component/computervision-sv.qclinux.0.0/${PBT_BUILD_DATE}/prebuilt_yocto_wrynose/${BPN}_${PV}_${PBT_ARCH}.tar.gz"
SRC_URI[sha256sum] = "f04432e42860068743054d408a309af892fbadfe37850f28d8defc26c26ea831"
S = "${UNPACKDIR}"

DEPENDS += "glib-2.0 fastrpc jsoncpp"

# This package is currently only used and tested on ARMv8 (aarch64) machines.
# Therefore, builds for other architectures are not necessary and are explicitly excluded.
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:aarch64 = "(.*)"

do_install() {
    install -d ${D}${libdir}/pkgconfig
    install -d ${D}${datadir}/doc/${PN}
    install -d ${D}${includedir}/sv

    install -m 0755 ${S}/usr/lib/libsv.so.1.0.0 ${D}${libdir}
    install -m 0755 ${S}/usr/lib/libsv_common.so.1.0.0 ${D}${libdir}
    install -m 0755 ${S}/usr/lib/libsv_eclib.so.1.0.0 ${D}${libdir}
    install -m 0755 ${S}/usr/lib/libsv_hfi.so.1.0.0 ${D}${libdir}
    cp -d ${S}/usr/lib/libsv.so.1 ${D}${libdir}
    cp -d ${S}/usr/lib/libsv.so ${D}${libdir}
    cp -d ${S}/usr/lib/libsv_common.so.1 ${D}${libdir}
    cp -d ${S}/usr/lib/libsv_common.so ${D}${libdir}
    cp -d ${S}/usr/lib/libsv_eclib.so.1 ${D}${libdir}
    cp -d ${S}/usr/lib/libsv_eclib.so ${D}${libdir}
    cp -d ${S}/usr/lib/libsv_hfi.so.1 ${D}${libdir}
    cp -d ${S}/usr/lib/libsv_hfi.so ${D}${libdir}

    install -m 0644 ${S}/usr/lib/pkgconfig/qcom-sv-binaries.pc ${D}${libdir}/pkgconfig/
    install -m 0644 ${S}/usr/share/doc/${PN}/NOTICE ${D}${datadir}/doc/${PN}
    install -m 0644 ${S}/usr/share/doc/${PN}/LICENSE.qcom-2.txt ${D}${datadir}/doc/${PN}
    install -m 0644 ${S}/usr/include/sv/svBuffer.h ${D}${includedir}/sv/
    install -m 0644 ${S}/usr/include/sv/svConfigMap.h ${D}${includedir}/sv/
    install -m 0644 ${S}/usr/include/sv/svDescriptor.h ${D}${includedir}/sv/
    install -m 0644 ${S}/usr/include/sv/svDescriptorMatch.h ${D}${includedir}/sv/
    install -m 0644 ${S}/usr/include/sv/svFeature.h ${D}${includedir}/sv/
    install -m 0644 ${S}/usr/include/sv/svFpx.h ${D}${includedir}/sv/
    install -m 0644 ${S}/usr/include/sv/svGme.h ${D}${includedir}/sv/
    install -m 0644 ${S}/usr/include/sv/svLme.h ${D}${includedir}/sv/
    install -m 0644 ${S}/usr/include/sv/svNcc.h ${D}${includedir}/sv/
    install -m 0644 ${S}/usr/include/sv/svPyramidFpx.h ${D}${includedir}/sv/
    install -m 0644 ${S}/usr/include/sv/svPyramidScaler.h ${D}${includedir}/sv/
    install -m 0644 ${S}/usr/include/sv/svScaler.h ${D}${includedir}/sv/
    install -m 0644 ${S}/usr/include/sv/svSession.h ${D}${includedir}/sv/
    install -m 0644 ${S}/usr/include/sv/svSpatialStats.h ${D}${includedir}/sv/
    install -m 0644 ${S}/usr/include/sv/svStereoDisparity.h ${D}${includedir}/sv/
    install -m 0644 ${S}/usr/include/sv/svTypes.h ${D}${includedir}/sv/
    install -m 0644 ${S}/usr/include/sv/svUtils.h ${D}${includedir}/sv/
    install -m 0644 ${S}/usr/include/sv/svWarp.h ${D}${includedir}/sv/
}