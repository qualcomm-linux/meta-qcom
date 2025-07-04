FILESEXTRAPATHS:prepend:qcom := "${THISDIR}/${BPN}:"

SRC_URI:append:qcom = "file://defconfig.qcom \
		      "

do_configure:prepend:qcom() {
	install -m 0644 ${UNPACKDIR}/defconfig.qcom ${UNPACKDIR}/defconfig
}
