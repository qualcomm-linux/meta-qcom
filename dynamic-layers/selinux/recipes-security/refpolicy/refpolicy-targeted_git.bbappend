FILESEXTRAPATHS:append := "${THISDIR}/${PN}:"

SRC_URI:append:qcom =  " \
	file://0057-tee_supplicant-Introduce-SELinux-domain-for-tee_supp.patch \
	"
