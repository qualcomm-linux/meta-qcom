SUMMARY = "Qualcomm capsule generation tools for UEFI firmware updates (native)"
DESCRIPTION = "qcom-capsule-tool: unified CLI for UEFI FMP capsule generation \
on Qualcomm platforms."
HOMEPAGE = "https://github.com/quic/cbsp-boot-utilities"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://LICENSE;md5=8e1eb38e3de3966193d29f31f5d7e684"

SRC_URI = "git://github.com/quic/cbsp-boot-utilities.git;protocol=https;branch=main"
SRCREV = "8a0f1deef97beae600910506bfba488976465828"

S = "${UNPACKDIR}/${BPN}-${PV}/uefi_capsule_generation"

inherit python_poetry_core native

DEPENDS += " \
    dtc-native \
    python3-dtc-native \
    python3-pyelftools-native \
"

# FvUpdate.xml ships alongside pyproject.toml (not inside the Python
# package), so poetry-core does not install it. Stage it explicitly so
# qcom-capsule.bbclass can fall back to a default when no board-specific
# override is provided via SRC_URI:append.
do_install:append() {
    install -d "${D}${datadir}/cbsp-boot-utilities"
    install -m 0644 "${S}/FvUpdate.xml" "${D}${datadir}/cbsp-boot-utilities/"
}
