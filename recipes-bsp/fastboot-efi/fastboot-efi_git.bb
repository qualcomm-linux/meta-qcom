SUMMARY = "Fastboot protocol implementation as a standalone UEFI application"
DESCRIPTION = "fastboot.efi implements the fastboot boot, continue and reboot \
commands as an EFI application, allowing kernels, Android boot images and \
UKIs to be booted over USB on devices whose firmware lacks fastboot support."
HOMEPAGE = "https://github.com/qualcomm/fastboot.efi"

LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=2998c54c288b081076c9af987bdf4838"

SRC_URI = "git://github.com/qualcomm/fastboot.efi.git;branch=main;protocol=https"
SRCREV = "b19f5861648dd355c75939b62ae041c5f184750a"
PV = "1.0+git"

inherit cargo cargo-update-recipe-crates deploy

# Regenerate after a SRCREV bump: bitbake -c update_crates fastboot-efi
require ${BPN}-crates.inc

COMPATIBLE_HOST = "aarch64.*-linux"

RUST_UEFI_TARGET = "${TARGET_ARCH}-unknown-uefi"

# core and alloc for UEFI come from libcore-uefi-rs-cross instead of
# libstd-rs, which is built for the Linux target.
RUSTLIB_DEP = ""
RUSTLIB = ""
DEPENDS += "libcore-uefi-rs-cross-${TARGET_ARCH} lld-native"

CARGO_TARGET_SUBDIR = "${RUST_UEFI_TARGET}/${BUILD_DIR}"
CARGO_BUILD_FLAGS = "-v --frozen --target ${RUST_UEFI_TARGET} --profile=${CARGO_PROFILE} \
    --manifest-path=${CARGO_MANIFEST_PATH}"

do_configure:append() {
    cat >> ${CARGO_HOME}/config.toml <<EOF

[target.${RUST_UEFI_TARGET}]
linker = "lld-link"
EOF
}

do_install() {
    install -D -m 0644 ${B}/target/${CARGO_TARGET_SUBDIR}/fastboot.efi \
        ${D}${datadir}/${BPN}/fastboot.efi
}

do_deploy() {
    install -m 0644 ${D}${datadir}/${BPN}/fastboot.efi ${DEPLOYDIR}/fastboot.efi
}
addtask deploy after do_install before do_build

FILES:${PN} = "${datadir}/${BPN}"

INSANE_SKIP:${PN} = "arch"
