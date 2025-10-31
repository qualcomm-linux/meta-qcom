SUMMARY = "Optimized Qualcomm FastCV library for Image Processing and Computer Vision"
DESCRIPTION = "Qualcomm FastCV userspace library supporting Image Processing and Computer Vision applications"
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = "file://${UNPACKDIR}/usr/share/doc/${PN}/NOLOGINBINARYLICENSEQTI.pdf;md5=4ceffe94cb40cdce6d2f4fb93cc063d1 \
                    file://${UNPACKDIR}/usr/share/doc/${PN}/NOTICE;md5=4b722aa0574e24873e07b94e40b92e4d "

PBT_BUILD_DATE = "251015"
ARTIFACTORY_URL = "https://qartifactory-edge.qualcomm.com/artifactory/qsc_releases/software/chip/component/computervision-fastcv.qclinux.0.1/${PBT_BUILD_DATE}/prebuilt_yocto"
PBT_ARCH = "armv8a"

SRC_URI = "${ARTIFACTORY_URL}/${BPN}_${PV}_${PBT_ARCH}.tar.gz"
SRC_URI[sha256sum] = "72accb95a1b29cd704db0b4a52e2e47b9905f80275599bc6b898a520a2663295"
S = "${UNPACKDIR}"

RDEPENDS:${PN} += "glib-2.0 fastrpc"

# This package is currently only used and tested on ARMv8 (aarch64) machines.
# Therefore, builds for other architectures are not necessary and are explicitly excluded.
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:aarch64 = "(.*)"

do_install() {
    install -d ${D}${libdir}/pkgconfig
    install -d ${D}${datadir}/doc/${PN}
    install -d ${D}${includedir}/fastcv
    install -d ${D}${libdir}/dsp
    install -d ${D}${libdir}/dsp/cdsp
    install -d ${D}${libdir}/dsp/cdsp/cv
    install -d ${D}${libdir}/dsp/cdsp/cv/v68
    install -d ${D}${libdir}/dsp/cdsp/cv/v68/KODIAK
    install -d ${D}${libdir}/dsp/cdsp/cv/v69
    install -d ${D}${libdir}/dsp/cdsp/cv/v69/KAILUA
    install -d ${D}${libdir}/dsp/cdsp/cv/v73
    install -d ${D}${libdir}/dsp/cdsp/cv/v73/LEMANS
    install -d ${D}${libdir}/dsp/cdsp/cv/v75
    install -d ${D}${libdir}/dsp/cdsp/cv/v75/MONACO

    install -m 0755 ${S}/usr/lib/dsp/cdsp/cv/v68/KODIAK/libdspCV_skel.so ${D}${libdir}/dsp/cdsp/cv/v68/KODIAK/
    install -m 0755 ${S}/usr/lib/dsp/cdsp/cv/v68/KODIAK/libfastcvadsp.so ${D}${libdir}/dsp/cdsp/cv/v68/KODIAK/
    install -m 0755 ${S}/usr/lib/dsp/cdsp/cv/v68/KODIAK/libfastcvdsp_skel.so ${D}${libdir}/dsp/cdsp/cv/v68/KODIAK/
    install -m 0755 ${S}/usr/lib/dsp/cdsp/cv/v69/KAILUA/libdspCV_skel.so ${D}${libdir}/dsp/cdsp/cv/v69/KAILUA/
    install -m 0755 ${S}/usr/lib/dsp/cdsp/cv/v69/KAILUA/libfastcvadsp.so ${D}${libdir}/dsp/cdsp/cv/v69/KAILUA/
    install -m 0755 ${S}/usr/lib/dsp/cdsp/cv/v69/KAILUA/libfastcvdsp_skel.so ${D}${libdir}/dsp/cdsp/cv/v69/KAILUA/
    install -m 0755 ${S}/usr/lib/dsp/cdsp/cv/v73/LEMANS/libdspCV_skel.so ${D}${libdir}/dsp/cdsp/cv/v73/LEMANS/
    install -m 0755 ${S}/usr/lib/dsp/cdsp/cv/v73/LEMANS/libfastcvadsp.so ${D}${libdir}/dsp/cdsp/cv/v73/LEMANS/
    install -m 0755 ${S}/usr/lib/dsp/cdsp/cv/v73/LEMANS/libfastcvdsp_skel.so ${D}${libdir}/dsp/cdsp/cv/v73/LEMANS/
    install -m 0755 ${S}/usr/lib/dsp/cdsp/cv/v75/MONACO/libdspCV_skel.so ${D}${libdir}/dsp/cdsp/cv/v75/MONACO/
    install -m 0755 ${S}/usr/lib/dsp/cdsp/cv/v75/MONACO/libfastcvadsp.so ${D}${libdir}/dsp/cdsp/cv/v75/MONACO/
    install -m 0755 ${S}/usr/lib/dsp/cdsp/cv/v75/MONACO/libfastcvdsp_skel.so ${D}${libdir}/dsp/cdsp/cv/v75/MONACO/

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
}

INSANE_SKIP:${PN} += " arch already-stripped"
FILES:${PN} += "\
    ${libdir}/libfastcvopt.so* \
    ${libdir}/libfastcvdsp_stub.so* \
    ${libdir}/dsp/cdsp/cv/v68/KODIAK/ \
    ${libdir}/dsp/cdsp/cv/v69/KAILUA/ \
    ${libdir}/dsp/cdsp/cv/v73/LEMANS/ \
    ${libdir}/dsp/cdsp/cv/v75/MONACO/ \
"
