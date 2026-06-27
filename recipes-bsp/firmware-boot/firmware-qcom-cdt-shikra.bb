DESCRIPTION = "CDT (Configuration Data Table) Firmware for Qualcomm Shikra Evaluation Kit (EVK)"

SRC_URI = " \
    https://${CDT_ARTIFACTORY}/CQ2390/cdt/cq2390-itp.zip;downloadfilename=cq2390-itp_${PV}.zip;name=cq2390-itp \
    "
SRC_URI[cq2390-itp.sha256sum] = "acc8b39a9a8ccde5e04b762e9e4c7bbddf66f0420a5d32ed69a5a74761699f7b"

QCOM_CDT_SUBDIR = "shikra"

include firmware-qcom-cdt-common.inc
