FILESEXTRAPATHS:prepend:qcom := "${THISDIR}/${BPN}:"

SRC_URI:append:qcom = "file://0001-QCLINUX-defconfig-enable-802.11be-owe-suiteb-fils-an.patch \
		       "
