LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=2ca5f2c35c8cc335f0a19756634782f1"

DEPENDS += "flex-native bison-native"

SRC_URI = "git://git.codelinaro.org/linaro/qcomlt/u-boot.git;branch=${SRCBRANCH};protocol=https"

SRCREV = "bea334ce7578d4cb645cf22d423ec11271feecfe"
SRCBRANCH = "caleb/rbx-integration"

S = "${WORKDIR}/git"
B = "${WORKDIR}/build"