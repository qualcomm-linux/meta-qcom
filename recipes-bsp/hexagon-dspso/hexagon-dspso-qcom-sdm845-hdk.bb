# Specify location of the corresponding dspso.bin file by adding
# DSPSO_URI:pn-firmware-qcom-sm8150-hdk = "..."  to local.conf. Use "file://"
# if the file is provided locally.

DESCRIPTION = "Hexagon DSP binaries for SDM845 HDK (aka HDK845) board"

DSPSO_SOC = "sdm845"
DSPSO_DEVICE = "SDM845-HDK"

# Config for SDM845-HDK is a part of the main repo
DSPSO_CONFIG = "hexagon-dsp-binaries-qualcomm-sdm845-hdk-config"

LICENSE = "CLOSED"
DEPENDS = "firmware-${DSP_PKG_NAME}"
S = "${UNPACKDIR}"

require hexagon-dspso.inc

# Only package SLPI binaries, ADSP and CDSP are provided by
# hexagon-dsp-binaries
PACKAGES = " \
    hexagon-dsp-binaries-${DSP_PKG_NAME}-sdsp \
"
