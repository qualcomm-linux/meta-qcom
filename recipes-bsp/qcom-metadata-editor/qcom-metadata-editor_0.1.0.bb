SUMMARY = "Read or edit the DTB overlay list inside a Qualcomm qclinux_fit.img"
DESCRIPTION = "Rust CLI that parses the multi-DTB FIT image the UEFI \
DtPlatformDxe reads (the 'dtb' partition, dtb.bin) and prints or rewrites the \
per-config 'compatible' and 'fdt' properties. Lets the enabled base DTB and \
overlays be changed on-target without reflashing the dtb partition."
HOMEPAGE = "https://github.com/imd-tec/qcom-metadata-editor"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=be497cfb223765dae07d1fa35204e4e2"

SRC_URI = "git://github.com/imd-tec/${BPN}.git;branch=master;protocol=https;tag=v${PV}"
SRCREV = "0d8766bf985bea89bfa774675aa6d7fd2aeb09be"

inherit cargo cargo-update-recipe-crates

# Regenerate after a SRCREV bump: bitbake -c update_crates qcom-metadata-editor
require ${BPN}-crates.inc
