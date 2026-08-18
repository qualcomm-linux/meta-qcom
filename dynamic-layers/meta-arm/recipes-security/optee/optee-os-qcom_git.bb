require recipes-security/optee/optee-os.inc

PV = "4.10.0-qcom-20260721+git"

SRC_TAG = "tag=qcom-next-4.10-20260721"
SRC_URI = "git://github.com/qualcomm-linux/optee_os.git;protocol=https;name=optee;nobranch=1;${SRC_TAG}"
SRCREV_optee = "690842e042dc470bf0625eb77a8cab926c372a8f"

require optee-qcom.inc
