# To make the layer pass yocto-check-layer only inherit update-alternatives when building for qualcomm
ALTERNATIVES_CLASS = ""
ALTERNATIVES_CLASS:qcom = "update-alternatives"

inherit_defer ${ALTERNATIVES_CLASS}

# firmware-ath6kl provides updated bdata.bin, which can not be accepted into main linux-firmware repo
ALTERNATIVE:${PN}-ath6k:qcom = "ar6004-hw13-bdata"
ALTERNATIVE_LINK_NAME[ar6004-hw13-bdata] = "${nonarch_base_libdir}/firmware/ath6k/AR6004/hw1.3/bdata.bin${@fw_compr_file_suffix(d)}"

# The audioreach topologies in linux-firmware 20261019 are built from v1.0.4 of
# the audioreach-topology project; the v1.0.5 sync was merged upstream after the
# release tag was cut. Drop the backport once a release carries it. Keep it
# under the qcom override like the rest of the file, so non-qualcomm builds are
# left untouched.
SRC_URI:append:qcom = " file://0001-linux-firmware-qcom-sync-audioreach-firmwares-from-v.patch"

# The topologies are binaries, which only git can apply.
PATCHTOOL:qcom = "git"

# The sync extends WHENCE, which LIC_FILES_CHKSUM checksums.
WHENCE_CHKSUM:qcom = "530356ed22ebc408fbdcd84f38219a80"

# Topologies installed into a vendor sub-directory and symlinked from the SoC
# directory get a package of their own, as the recipe already does for the
# other laptops.
PACKAGES:append:qcom = " \
    ${PN}-qcom-glymur-asus-zenbook-a14-ux3407na-audio-tplg \
    ${PN}-qcom-glymur-asus-zenbook-a16-ux3607oa-audio-tplg \
    ${PN}-qcom-glymur-lenovo-thinkpad-t14s-g7-audio-tplg \
    ${PN}-qcom-x1e80100-lenovo-ideacentre-mini-audio-tplg \
"

FILES:${PN}-qcom-glymur-asus-zenbook-a14-ux3407na-audio-tplg:qcom = " \
    ${firmwaredir}/qcom/glymur/ASUSTeK/UX3407NA/GLYMUR-ASUS-Zenbook-A14-UX3407NA-tplg.bin* \
    ${firmwaredir}/qcom/glymur/GLYMUR-ASUS-Zenbook-A14-UX3407NA-tplg.bin* \
"
LICENSE:${PN}-qcom-glymur-asus-zenbook-a14-ux3407na-audio-tplg:qcom = "LicenseRef-Firmware-linaro"
RDEPENDS:${PN}-qcom-glymur-asus-zenbook-a14-ux3407na-audio-tplg:qcom = "${PN}-linaro-license"

FILES:${PN}-qcom-glymur-asus-zenbook-a16-ux3607oa-audio-tplg:qcom = " \
    ${firmwaredir}/qcom/glymur/ASUSTeK/UX3607OA/GLYMUR-ASUS-Zenbook-A16-UX3607OA-tplg.bin* \
    ${firmwaredir}/qcom/glymur/GLYMUR-ASUS-Zenbook-A16-UX3607OA-tplg.bin* \
"
LICENSE:${PN}-qcom-glymur-asus-zenbook-a16-ux3607oa-audio-tplg:qcom = "LicenseRef-Firmware-linaro"
RDEPENDS:${PN}-qcom-glymur-asus-zenbook-a16-ux3607oa-audio-tplg:qcom = "${PN}-linaro-license"

FILES:${PN}-qcom-glymur-lenovo-thinkpad-t14s-g7-audio-tplg:qcom = " \
    ${firmwaredir}/qcom/glymur/LENOVO/22A8/Lenovo-ThinkPad-T14s-Gen-7-tplg.bin* \
    ${firmwaredir}/qcom/glymur/Lenovo-ThinkPad-T14s-Gen-7-tplg.bin* \
"
LICENSE:${PN}-qcom-glymur-lenovo-thinkpad-t14s-g7-audio-tplg:qcom = "LicenseRef-Firmware-linaro"
RDEPENDS:${PN}-qcom-glymur-lenovo-thinkpad-t14s-g7-audio-tplg:qcom = "${PN}-linaro-license"

FILES:${PN}-qcom-x1e80100-lenovo-ideacentre-mini-audio-tplg:qcom = " \
    ${firmwaredir}/qcom/x1e80100/LENOVO/91B6/X1E80100-LENOVO-IdeaCentre-Mini-tplg.bin* \
    ${firmwaredir}/qcom/x1e80100/X1E80100-LENOVO-IdeaCentre-Mini-tplg.bin* \
"
LICENSE:${PN}-qcom-x1e80100-lenovo-ideacentre-mini-audio-tplg:qcom = "LicenseRef-Firmware-linaro"
RDEPENDS:${PN}-qcom-x1e80100-lenovo-ideacentre-mini-audio-tplg:qcom = "${PN}-linaro-license"

# The reference board topologies join the audio package their machine already
# has. They carry the Linaro licence, so a package that gains its first
# topology now also pulls in the Linaro licence text.
FILES:${PN}-qcom-eliza-audio:append:qcom = " ${firmwaredir}/qcom/eliza/eliza-cqs-evk-tplg.bin*"
LICENSE:${PN}-qcom-eliza-audio:append:qcom = " AND LicenseRef-Firmware-linaro"
RDEPENDS:${PN}-qcom-eliza-audio:append:qcom = " ${PN}-linaro-license"

FILES:${PN}-qcom-hawi-audio:append:qcom = " ${firmwaredir}/qcom/hawi/HAWI-MTP-tplg.bin*"
LICENSE:${PN}-qcom-hawi-audio:append:qcom = " AND LicenseRef-Firmware-linaro"
RDEPENDS:${PN}-qcom-hawi-audio:append:qcom = " ${PN}-linaro-license"

FILES:${PN}-qcom-maili-audio:append:qcom = " ${firmwaredir}/qcom/maili/MAILI-MTP-tplg.bin*"
LICENSE:${PN}-qcom-maili-audio:append:qcom = " AND LicenseRef-Firmware-linaro"
RDEPENDS:${PN}-qcom-maili-audio:append:qcom = " ${PN}-linaro-license"

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

FILES:${PN}-qcom-sc8280xp-radxa-dragon-q8b-audio:append:qcom = " ${firmwaredir}/qcom/sc8280xp/radxa/dragon-q8b/SC8280XP-Radxa-Dragon-Q8B-tplg.bin*"
LICENSE:${PN}-qcom-sc8280xp-radxa-dragon-q8b-audio:append:qcom = " AND LicenseRef-Firmware-linaro"
RDEPENDS:${PN}-qcom-sc8280xp-radxa-dragon-q8b-audio:append:qcom = " ${PN}-linaro-license"

# x1e80100 already ships topologies, so only the file list grows.
FILES:${PN}-qcom-x1e80100-audio:append:qcom = " \
    ${firmwaredir}/qcom/x1e80100/X1E80100-ACER-SWIFT-14-tplg.bin* \
    ${firmwaredir}/qcom/x1e80100/X1E80100-MEDION-SPRCHRGD-14-S1-tplg.bin* \
    ${firmwaredir}/qcom/x1e80100/X1E80100-Microsoft-Surface-Pro-11-tplg.bin* \
"
