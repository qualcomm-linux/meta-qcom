# Specify location of the corresponding dspso.bin file by adding
# DSPSO_URI:pn-firmware-qcom-sm8350-hdk = "..."  to local.conf. Use "file://"
# if the file is provided locally.

DESCRIPTION = "Hexagon DSP binaries for SM8350 HDK (aka HDK888) board"

DSPSO_SOC = "sm8350"
DSPSO_DEVICE = "SM8350-HDK"

LICENSE = "CLOSED"
DEPENDS = "firmware-${DSP_PKG_NAME}"
S = "${UNPACKDIR}"

require hexagon-dspso.inc

SKIP_FILEDEPS:hexagon-dsp-binaries-${DSP_PKG_NAME}-cdsp = "1"
