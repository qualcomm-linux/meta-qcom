FILESEXTRAPATHS:append := "${THISDIR}/${PN}:"

SRC_URI:append:qcom =  " \
	file://0057-pd-mapper-Introduce-SELinux-domain-for-pd-mapper.patch \
	"	
