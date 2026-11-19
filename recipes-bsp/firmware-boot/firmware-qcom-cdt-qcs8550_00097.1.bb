DESCRIPTION = "CDT (Configuration Data Table) Firmware for Qualcomm QCS8550 platform"

# Firmware boot binaries and CDT are hosted in a private artifactory.
# Configure the download base URL in local.conf e.g.
#
# FW_ARTIFACTORY:pn-firmware-qcom-boot-qcs8550 = "<artifactory URI>"
#
SRC_URI = " \
    https://${CDT_ARTIFACTORY}/r${PV}/rb5gen2-core-kit.zip;downloadfilename=cdt-rb5gen2-core-kit_${PV}.zip;name=rb5gen2-core-kit \
"
SRC_URI[rb5gen2-core-kit.sha256sum] = "e6f679804dc44ebb4e253be79b2999774df1de4059b722bdcb8a8cd82b095902"

include firmware-qcom-cdt-common.inc
