DESCRIPTION = "CDT (Configuration Data Table) Firmware for Qualcomm QRB2210 platform"

SRC_URI = " \
    https://${CDT_ARTIFACTORY}/QRB2210/cdt/rb1_core_kit.zip;downloadfilename=cdt-rb1-core-kit_${PV}.zip;name=rb1-core-kit \
    "
SRC_URI[rb1-core-kit.sha256sum] = "edacea2aafc8a79697135f4d9312528ba89a464251b566486b3b9d6213c90340"

QCOM_CDT_SUBDIR = "qrb2210"

include firmware-qcom-cdt-common.inc
