# Specify location of the corresponding dspso.bin file by adding
# DSPSO_URI:pn-firmware-qcom-sm8550-hdk = "..."  to local.conf. Use "file://"
# if the file is provided locally.

DESCRIPTION = "Hexagon DSP binaries for SM8550 HDK board"

DSPSO_SOC = "sm8550"
DSPSO_DEVICE = "SM8550-HDK"

LICENSE = "CLOSED"
DEPENDS = "firmware-${DSP_PKG_NAME}"
S = "${UNPACKDIR}"

require hexagon-dspso.inc

SKIP_FILEDEPS:hexagon-dsp-binaries-${DSP_PKG_NAME}-adsp = "1"
SKIP_FILEDEPS:hexagon-dsp-binaries-${DSP_PKG_NAME}-cdsp = "1"
