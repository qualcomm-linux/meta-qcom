LIC_FILES_CHKSUM:qcom = "file://hostapd/README;md5=0e430ef1be3d6eebf257cf493fc7661d"

FILESEXTRAPATHS:prepend:qcom := "${THISDIR}/${BPN}:"

PV:qcom = "2.12+git"

SRC_URI:qcom = "git://w1.fi/hostap.git;protocol=git;branch=main \
                file://defconfig.qcom \
		file://init \
		file://hostapd.service \
                "

#This version includes fixes related to AP MLD
SRCREV:qcom = "4c1ae91015e34ba83bc892e83739445e0cb4a9f9"

S:qcom = "${UNPACKDIR}/git"

do_configure:prepend:qcom() {
    install -m 0644 ${UNPACKDIR}/defconfig.qcom ${UNPACKDIR}/defconfig
}
