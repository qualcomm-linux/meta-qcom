require recipes-bsp/trusted-firmware-a/trusted-firmware-a.inc

PV = "2.15.0-qcom+git"

SRC_TAG = "tag=qcom-next-2.15-20260804"
SRC_URI = "git://github.com/qualcomm-linux/trusted-firmware-a.git;protocol=https;name=tfa;nobranch=1;${SRC_TAG}"
SRCREV_tfa = "10ecf7dcc641e8453e2e2ea2f21c13c7a867ffe0"

LIC_FILES_CHKSUM += "file://docs/license.rst;md5=6ed7bace7b0bc63021c6eba7b524039e"

require trusted-firmware-a-qcom.inc
