# Specify location of the corresponding dspso.bin file by adding
# DSPSO_URI:pn-firmware-qcom-sm8150-hdk = "..."  to local.conf. Use "file://"
# if the file is provided locally.

DESCRIPTION = "Hexagon DSP binaries for SM8150 HDK (aka HDK855) board"

DSPSO_SOC = "sm8150"
DSPSO_DEVICE = "SM8150-HDK"

LICENSE = "CLOSED"
DEPENDS = "firmware-${DSP_PKG_NAME}"
S = "${UNPACKDIR}"

require hexagon-dspso.inc
