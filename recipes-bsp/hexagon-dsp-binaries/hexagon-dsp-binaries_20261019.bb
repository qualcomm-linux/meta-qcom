require hexagon-dsp-binaries.inc

LIC_FILES_CHKSUM = "\
    file://LICENSE.qcom;md5=56e86b6c508490dadc343f39468b5f5e \
    file://LICENSE.qcom-2;md5=165287851294f2fb8ac8cbc5e24b02b0 \
    file://WHENCE;md5=2b088d688daa8224fb4580e91b09f7fd \
    file://Notice.qcs8550.txt;md5=412afc27ddceb9cf8f743f61c3d6c401 \
"

SRCREV = "38f254d4da12aeaa4d82b7e855979b6a5d6aee22"

# HACK: since patch files to binaries are blocked in bitbake patch tooling
SRCREV = "2cedba69a2ca20080284c46c41c5229caf4c8fd1"
SRC_URI = "git://git@github.com/mikkorapeli-linaro/hexagon-dsp-binaries.git;nobranch=1;protocol=ssh"

# HACK: remove not-yet-packaged binaries
do_install:append() {
    rm -rf ${D}${datadir}/qcom/qcs615/Qualcomm/Talos-Lyra-EVK
}
