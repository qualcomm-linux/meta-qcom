FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

SRC_URI:append:qcom = " \
    file://0001-freedreno-Add-chip-id-support-for-A830v1.patch \
    file://0001-freedreno-Add-chip-support-for-a722.patch \
    file://0001-freedreno-Enable-A753-and-A735.patch \
    file://0001-freedreno-Add-chip-id-support-for-Adreno-X2-85.patch \
    file://0002-FROMLIST-freedreno-add-basic-device-support-for-Adreno-850-and-845.patch \
    file://0003-FROMLIST-tu-enable-FDM-offset-handling-for-GMEM-attachment-fetches.patch \
"

# Enable freedreno driver
PACKAGECONFIG_FREEDRENO = "\
    freedreno \
    tools \
"

PACKAGECONFIG:append:qcom = "${PACKAGECONFIG_FREEDRENO}"
