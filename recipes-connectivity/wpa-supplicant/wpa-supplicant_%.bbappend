LIC_FILES_CHKSUM:qcom = "file://${COMMON_LICENSE_DIR}/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

FILESEXTRAPATHS:prepend:qcom := "${THISDIR}/${BPN}:"

PV:qcom="2.12+git"

SRC_URI:qcom = "git://w1.fi/hostap.git;protocol=git;branch=main \
                file://defconfig.qcom \
		file://wpa-supplicant.sh \
		file://wpa_supplicant.conf \
		file://wpa_supplicant.conf-sane \
		file://99_wpa_supplicant \
                "

#This version includes fixes related to AP MLD
SRCREV:qcom = "4c1ae91015e34ba83bc892e83739445e0cb4a9f9"

S:qcom = "${UNPACKDIR}/git"

do_configure:prepend:qcom() {
	install -m 0644 ${UNPACKDIR}/defconfig.qcom ${UNPACKDIR}/defconfig
}
