# Specify location of the corresponding dspso.bin file by adding
# DSPSO_URI:pn-firmware-qcom-sm8450-hdk = "..."  to local.conf. Use "file://"
# if the file is provided locally.

DESCRIPTION = "Hexagon DSP binaries for SM8450 HDK board"

DSPSO_SOC = "sm8450"
DSPSO_DEVICE = "SM8450-HDK"

LICENSE = "CLOSED"
DEPENDS = "firmware-${DSP_PKG_NAME}"
S = "${UNPACKDIR}"

require hexagon-dspso.inc

SKIP_FILEDEPS:hexagon-dsp-binaries-${DSP_PKG_NAME}-adsp = "1"
SKIP_FILEDEPS:hexagon-dsp-binaries-${DSP_PKG_NAME}-cdsp = "1"
