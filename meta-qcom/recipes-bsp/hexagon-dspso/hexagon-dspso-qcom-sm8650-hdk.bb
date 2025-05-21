# Specify location of the corresponding dspso.bin file by adding
# DSPSO_URI:pn-firmware-qcom-sm8650-hdk = "..."  to local.conf. Use "file://"
# if the file is provided locally.

DESCRIPTION = "Hexagon DSP binaries for SM8650 HDK board"

DSPSO_SOC = "sm8650"
DSPSO_DEVICE = "SM8650-HDK"

LICENSE = "CLOSED"
DEPENDS = "firmware-${DSP_PKG_NAME}"
S = "${UNPACKDIR}"

require hexagon-dspso.inc
