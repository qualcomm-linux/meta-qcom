SUMMARY = "Optimized Qualcomm FastCV library for Image Processing and Computer Vision"
DESCRIPTION = "Qualcomm FastCV userspace library supporting Image Processing and Computer Vision applications"
LICENSE = "LicenseRef-LICENSE.qcom-2"
LIC_FILES_CHKSUM = "file://${UNPACKDIR}/usr/share/doc/${PN}/NOLOGINBINARYLICENSEQTI.pdf;md5=4ceffe94cb40cdce6d2f4fb93cc063d1 \
                    file://${UNPACKDIR}/usr/share/doc/${PN}/NOTICE;md5=4b722aa0574e24873e07b94e40b92e4d "

PBT_BUILD_DATE = "260805"
ARTIFACTORY_URL = "https://qartifactory-edge.qualcomm.com/artifactory/qsc_releases/software/chip/component/computervision-fastcv.qclinux.0.1/${PBT_BUILD_DATE}/prebuilt_yocto_wrynose"
PBT_ARCH = "armv8a"

SRC_URI = "${ARTIFACTORY_URL}/${BPN}_${PV}_${PBT_ARCH}.tar.gz"
SRC_URI[sha256sum] = "894846e3ea7c436515e07785b9fe9ed69dc3b5a450a3b710335bc05e82d54b41"
S = "${UNPACKDIR}"

DEPENDS += "glib-2.0 fastrpc"

# This package is currently only used and tested on ARMv8 (aarch64) machines.
# Therefore, builds for other architectures are not necessary and are explicitly excluded.
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:aarch64 = "(.*)"

inherit qcom-hexagon

do_install() {
    install -d ${D}${bindir}/
    install -d ${D}${libdir}/pkgconfig
    install -d ${D}${datadir}/doc/${PN}
    install -d ${D}${includedir}/fastcv

    install -m 0755 ${S}/usr/lib/libfastcvopt.so.1.8.0 ${D}${libdir}
    install -m 0755 ${S}/usr/lib/libfastcvdsp_stub.so.1.8.0 ${D}${libdir}
    cp -d ${S}/usr/lib/libfastcvopt.so.1 ${D}${libdir}
    cp -d ${S}/usr/lib/libfastcvopt.so ${D}${libdir}
    cp -d ${S}/usr/lib/libfastcvdsp_stub.so.1 ${D}${libdir}
    cp -d ${S}/usr/lib/libfastcvdsp_stub.so ${D}${libdir}

    install -m 0644 ${S}/usr/lib/pkgconfig/qcom-fastcv-binaries.pc ${D}${libdir}/pkgconfig/
    install -m 0644 ${S}/usr/share/doc/${PN}/NOTICE ${D}${datadir}/doc/${PN}
    install -m 0644 ${S}/usr/share/doc/${PN}/NOLOGINBINARYLICENSEQTI.pdf ${D}${datadir}/doc/${PN}
    install -m 0644 ${S}/usr/include/fastcv/fastcv.h ${D}${includedir}/fastcv/
    install -m 0644 ${S}/usr/include/fastcv/fastcvExt.h ${D}${includedir}/fastcv/

    # The per-SoC builds of a Hexagon version differ only in their signature
    for dir in ${S}/usr/lib/dsp/cdsp/cv/v*/*; do
        case $(basename ${dir}) in
        # Built for v66 and v73 respectively, not for the Hexagon version of
        # the directory they are shipped in
        TALOS_MOOREA|KAILUA)
            continue
            ;;
        esac

        arch=$(basename $(dirname ${dir}))
        install -d ${D}${datadir}/qcom/${arch}
        install -m 0644 ${dir}/*.so ${D}${datadir}/qcom/${arch}
    done

    install -m 0755 ${S}/usr/bin/fastcv_simple_test64 ${D}${bindir}
}

PACKAGE_BEFORE_PN = "${PN}-dsp fastcv-apps"

FILES:${PN}-dsp = "${libdir}/libfastcvdsp_stub.so.*"
FILES:fastcv-apps = "${bindir}/fastcv_simple_test64"

RRECOMMENDS:${PN} += "packagegroup-qcom-hexagon-fastcv"

QCOM_HEXAGON_RDEPENDS = "${PN}-dsp"
