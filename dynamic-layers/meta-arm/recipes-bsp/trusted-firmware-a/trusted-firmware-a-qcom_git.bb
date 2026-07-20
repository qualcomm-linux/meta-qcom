require recipes-bsp/trusted-firmware-a/trusted-firmware-a.inc

PV = "2.14.0-qcom+git"
PV:qcs9100 = "2.15.0-qcom+git"

# lemans uses native-driver TF-A, no qtiseclib.
SRC_TAG = "tag=qcom-next-2.14-20260608"
SRC_TAG:qcs9100 = "tag=qcom-lemans-2.15-20260717"
SRC_URI = "git://github.com/qualcomm-linux/trusted-firmware-a.git;protocol=https;name=tfa;nobranch=1;${SRC_TAG}"
SRCREV_tfa = "86899924561b02678ba3c7e535883151e37a4b1f"
SRCREV_tfa:qcs9100 = "745fdf377feadf4db18505cbc8712b2f7f810ac5"

LIC_FILES_CHKSUM += "file://docs/license.rst;md5=6ed7bace7b0bc63021c6eba7b524039e"

require trusted-firmware-a-qcom.inc
