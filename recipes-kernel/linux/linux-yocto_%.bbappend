# do not override KBRANCH and SRCREV_machine, use default ones.
COMPATIBLE_MACHINE:qcom = "qcom-armv8a|qcom-armv7a"

BASEVER = "${@ d.getVar('LINUX_VERSION').rpartition('.')[0]}"

# prioritizing the versioned path if it exists and fallback to the unversioned
FILESEXTRAPATHS:prepend:qcom := "${THISDIR}/${PN}-${BASEVER}:${THISDIR}/${PN}:"

# include all Qualcomm-specific files
SRC_URI:append:qcom = " \
    file://qcom.scc \
"
