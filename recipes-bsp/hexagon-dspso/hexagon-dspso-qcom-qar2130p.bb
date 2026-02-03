# Specify location of the corresponding dspso.bin file by adding
# DSPSO_URI:pn-firmware-qcom-qar2130p = "..."  to local.conf. Use "file://"
# if the file is provided locally.

DESCRIPTION = "Hexagon DSP binaries for QAR2130P device"

DSPSO_SOC = "sar2130p"
DSPSO_DEVICE = "QAR2130P"

LICENSE = "CLOSED"
DEPENDS = "firmware-${DSP_PKG_NAME}"
S = "${UNPACKDIR}"

require hexagon-dspso.inc

SKIP_FILEDEPS:hexagon-dsp-binaries-${DSP_PKG_NAME}-adsp = "1"
SKIP_FILEDEPS:hexagon-dsp-binaries-${DSP_PKG_NAME}-cdsp = "1"
