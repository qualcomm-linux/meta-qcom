# do not override KBRANCH and SRCREV_machine, use default ones.
COMPATIBLE_MACHINE:qcom = "qcom-armv8a|qcom-armv7a"

BASEVER = "${@ d.getVar('LINUX_VERSION').rpartition('.')[0]}"

FILESEXTRAPATHS:prepend:qcom := "${THISDIR}/${PN}-${BASEVER}:"

# include all Qualcomm-specific files
SRC_URI:append:qcom = " \
    file://qcom.scc \
"
