SUMMARY = "Optimized Qualcomm FastCV library for Image Processing and Computer Vision"
DESCRIPTION = "Qualcomm FastCV userspace library supporting Image Processing and Computer Vision applications"
LICENSE = "LICENSE.qcom-2"
LIC_FILES_CHKSUM = "file://${UNPACKDIR}/usr/share/doc/${PN}/NOLOGINBINARYLICENSEQTI.pdf;md5=4ceffe94cb40cdce6d2f4fb93cc063d1 \
                    file://${UNPACKDIR}/usr/share/doc/${PN}/NOTICE;md5=4b722aa0574e24873e07b94e40b92e4d "

PBT_BUILD_DATE = "251120"
ARTIFACTORY_URL = "https://qartifactory-edge.qualcomm.com/artifactory/qsc_releases/software/chip/component/computervision-fastcv.qclinux.0.1/${PBT_BUILD_DATE}/prebuilt_yocto"
PBT_ARCH = "armv8a"

SRC_URI = "${ARTIFACTORY_URL}/${BPN}_${PV}_${PBT_ARCH}.tar.gz"
SRC_URI[sha256sum] = "14a88a64fa02f41a105965de5c6f9932f1fbf929bebff677d7a450e294bd7f5d"
S = "${UNPACKDIR}"

DEPENDS += "glib-2.0 fastrpc"

# This package is currently only used and tested on ARMv8 (aarch64) machines.
# Therefore, builds for other architectures are not necessary and are explicitly excluded.
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:aarch64 = "(.*)"

do_install() {
    install -d ${D}${libdir}/pkgconfig
    install -d ${D}${datadir}/doc/${PN}
    install -d ${D}${includedir}/fastcv
    install -d ${D}${datadir}/qcom/qcm6490/Thundercomm/RB3gen2/dsp/cdsp
    install -d ${D}${datadir}/qcom/qcs615/Qualcomm/QCS615-RIDE/dsp/cdsp
    install -d ${D}${datadir}/qcom/qcs8300/Qualcomm/QCS8300-RIDE/dsp/cdsp
    install -d ${D}${datadir}/qcom/sa8775p/Qualcomm/SA8775P-RIDE/dsp/cdsp

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

    install -m 0644 ${S}/usr/lib/dsp/cdsp/cv/v68/KODIAK/*.so ${D}${datadir}/qcom/qcm6490/Thundercomm/RB3gen2/dsp/cdsp
    install -m 0644 ${S}/usr/lib/dsp/cdsp/cv/v65/TALOS_MOOREA/*.so ${D}${datadir}/qcom/qcs615/Qualcomm/QCS615-RIDE/dsp/cdsp
    install -m 0644 ${S}/usr/lib/dsp/cdsp/cv/v75/MONACO/*.so ${D}${datadir}/qcom/qcs8300/Qualcomm/QCS8300-RIDE/dsp/cdsp
    install -m 0644 ${S}/usr/lib/dsp/cdsp/cv/v73/LEMANS/*.so ${D}${datadir}/qcom/sa8775p/Qualcomm/SA8775P-RIDE/dsp/cdsp

}

PACKAGES += "\
    ${PN}-qcs615-ride-dsp \
    ${PN}-qcs8300-ride-dsp \
    ${PN}-sa8775p-ride-dsp \
    ${PN}-thundercomm-rb3gen2-dsp \
"

INSANE_SKIP:${PN}-qcs615-ride-dsp = "arch libdir"
INSANE_SKIP:${PN}-qcs8300-ride-dsp = "arch libdir"
INSANE_SKIP:${PN}-sa8775p-ride-dsp = "arch libdir"
INSANE_SKIP:${PN}-thundercomm-rb3gen2-dsp = "arch libdir"

FILES:${PN}-qcs615-ride-dsp += "${datadir}/qcom/qcs615/Qualcomm/QCS615-RIDE/dsp"
FILES:${PN}-qcs8300-ride-dsp += "${datadir}/qcom/qcs8300/Qualcomm/QCS8300-RIDE/dsp"
FILES:${PN}-sa8775p-ride-dsp += "${datadir}/qcom/sa8775p/Qualcomm/SA8775P-RIDE/dsp"
FILES:${PN}-thundercomm-rb3gen2-dsp += "${datadir}/qcom/qcm6490/Thundercomm/RB3gen2/dsp"
