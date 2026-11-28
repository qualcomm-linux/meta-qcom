FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

# use latest version of linux-firmware from the lts-linux-firmware-mixin layer
DEFAULT_PREFERENCE:layer-lts-linux-firmware-mixin:qcom = "1"

# To make the layer pass yocto-check-layer only inherit update-alternatives when building for qualcomm
ALTERNATIVES_CLASS = ""
ALTERNATIVES_CLASS:qcom = "update-alternatives"

inherit_defer ${ALTERNATIVES_CLASS}

# firmware-ath6kl provides updated bdata.bin, which can not be accepted into main linux-firmware repo
ALTERNATIVE:${PN}-ath6k:qcom = "ar6004-hw13-bdata"
ALTERNATIVE_LINK_NAME[ar6004-hw13-bdata] = "${nonarch_base_libdir}/firmware/ath6k/AR6004/hw1.3/bdata.bin${@fw_compr_file_suffix(d)}"

# The Nord and Shikra audioreach topologies are missing from linux-firmware
# 20261019, which carries v1.0.4 of the audioreach-topology project; the v1.0.5
# sync was merged upstream after the release tag was cut. Drop the backport once
# a release carries it. Keep it under the qcom override like the rest of the
# file, so non-qualcomm builds are left untouched.
FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
SRC_URI:append:qcom = " file://0001-linux-firmware-qcom-sync-audioreach-firmwares-from-v.patch"

# The topologies are binaries, which only git can apply.
PATCHTOOL:qcom = "git"

# The sync extends WHENCE, which LIC_FILES_CHKSUM checksums.
WHENCE_CHKSUM:qcom = "ad950af38523644b6e9bf4a112b9ff38"

# The topologies join the audio package their machine already has. They carry
# the Linaro licence, which these packages did not need before.
FILES:${PN}-qcom-nord-audio:append:qcom = " \
    ${firmwaredir}/qcom/nord/NORD-RIDE-tplg.bin* \
    ${firmwaredir}/qcom/nord/NORD-RRD-tplg.bin* \
"
LICENSE:${PN}-qcom-nord-audio:append:qcom = " AND LicenseRef-Firmware-linaro"
RDEPENDS:${PN}-qcom-nord-audio:append:qcom = " ${PN}-linaro-license"

FILES:${PN}-qcom-shikra-audio:append:qcom = " \
    ${firmwaredir}/qcom/shikra/shikra-cqs-evk-tplg.bin* \
    ${firmwaredir}/qcom/shikra/shikra-iqs-evk-tplg.bin* \
"
LICENSE:${PN}-qcom-shikra-audio:append:qcom = " AND LicenseRef-Firmware-linaro"
RDEPENDS:${PN}-qcom-shikra-audio:append:qcom = " ${PN}-linaro-license"
