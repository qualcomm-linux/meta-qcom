DESCRIPTION = "CDT (Configuration Data Table) Firmware for Qualcomm Shikra Evaluation Kit (EVK)"

SRC_URI = " \
    https://${CDT_ARTIFACTORY}/CQ2390/cdt/cq2390-itp.zip;downloadfilename=cq2390-itp_${PV}.zip;name=cq2390-itp \
    https://${CDT_ARTIFACTORY}/CQ2390/cdt/iq2390-itp.zip;downloadfilename=iq2390-itp_${PV}.zip;name=iq2390-itp \
    https://${CDT_ARTIFACTORY}/CQ2390/cdt/cq2390-itp-cologne.zip;downloadfilename=cq2390-itp-cologne_${PV}.zip;name=cq2390-itp-cologne \
    https://${CDT_ARTIFACTORY}/CQ2390/cdt/cq2390-itps-cologne.zip;downloadfilename=cq2390-itps-cologne_${PV}.zip;name=cq2390-itps-cologne \
    https://${CDT_ARTIFACTORY}/CQ2390/cdt/iq2390-itp-cologne.zip;downloadfilename=iq2390-itp-cologne_${PV}.zip;name=iq2390-itp-cologne \
    https://${CDT_ARTIFACTORY}/CQ2390/cdt/iq2390-itps-cologne.zip;downloadfilename=iq2390-itps-cologne_${PV}.zip;name=iq2390-itps-cologne \
    "
SRC_URI[cq2390-itp.sha256sum] = "acc8b39a9a8ccde5e04b762e9e4c7bbddf66f0420a5d32ed69a5a74761699f7b"
SRC_URI[iq2390-itp.sha256sum] = "f4ee601a81bfbf55bde8ce5e6215128844d39ba5d43c855b233a05e1ff2476c5"
SRC_URI[cq2390-itp-cologne.sha256sum] = "e9941ab666242d9c0ffa9b534d32775206ff6d7f7469330d2b264e98d0aa870f"
SRC_URI[cq2390-itps-cologne.sha256sum] = "f687679faa8faf2f82ec522e280708edd0c156bbc78d154f7a1ad591a5ce2811"
SRC_URI[iq2390-itp-cologne.sha256sum] = "26dfb16d644f27855c3499b9e19ad9f12a326ab6f0278a9e37f01aab169c73fb"
SRC_URI[iq2390-itps-cologne.sha256sum] = "16415320fa39d2a48faaad1546c82fbdec79e63b6ef80496b97a2cd75f53dec9"

QCOM_CDT_SUBDIR = "shikra"

include firmware-qcom-cdt-common.inc
