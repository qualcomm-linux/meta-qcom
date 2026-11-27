DESCRIPTION = "CDT (Configuration Data Table) Firmware for Qualcomm Eliza Evaluation Kit (EVK)"

SRC_URI = " \
    https://${CDT_ARTIFACTORY}/CQ7790/cdt/cq7790-evk_v1.zip;downloadfilename=cq7790-evk_v1_${PV}.zip;name=cq7790-evk \
    "
SRC_URI[cq7790-evk.sha256sum] = "c2d1fb4b5a913ad5adc691b3c94b34c6d09cd35aafbf58fe48e0ce94e884d5f4"

QCOM_CDT_SUBDIR = "eliza"

include firmware-qcom-cdt-common.inc
