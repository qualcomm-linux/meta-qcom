SUMMARY = "Rust core libraries for UEFI targets"
DESCRIPTION = "Builds the Rust core, alloc and compiler_builtins libraries \
for the UEFI target matching TARGET_ARCH and installs them into the native \
sysroot, where rust-native looks for them when building UEFI applications."
HOMEPAGE = "http://www.rust-lang.org"
SECTION = "devel"
LICENSE = "Unicode-3.0 AND (Apache-2.0 OR MIT)"
LIC_FILES_CHKSUM = "file://../COPYRIGHT;md5=11a3899825f4376896e438c8c753f8dc"

PN = "libcore-uefi-rs-cross-${TARGET_ARCH}"
BPN = "libcore-uefi-rs"
PV = "${@d.getVar('RUSTVERSION').replace('%', '')}"

require recipes-devtools/rust/rust-source.inc
require recipes-devtools/rust/common-source.inc

S = "${RUSTSRC}/library"
B = "${WORKDIR}/build"

INHIBIT_DEFAULT_DEPS = "1"
DEPENDS = "rust-native cargo-native"

inherit cross

RUST_UEFI_TARGET = "${@'i686' if d.getVar('TARGET_ARCH') in ['i386', 'i486', 'i586', 'i686'] else d.getVar('TARGET_ARCH')}-unknown-uefi"

python () {
    if d.getVar('TARGET_ARCH') not in ['aarch64', 'x86_64', 'i386', 'i486', 'i586', 'i686']:
        raise bb.parse.SkipRecipe("Rust has no UEFI target for %s" % d.getVar('TARGET_ARCH'))
}

# rustc only searches its own sysroot, not the cross target directory.
RUST_UEFI_LIBDIR = "${exec_prefix}/lib/rustlib/${RUST_UEFI_TARGET}/lib"

CARGO_HOME = "${WORKDIR}/cargo_home"

# The compiler_builtins build script is linked for the build host.
do_configure() {
    mkdir -p ${CARGO_HOME}
    cat > ${WORKDIR}/build-rust-ccld <<EOF
#!/bin/sh
exec ${BUILD_CCLD} ${BUILD_LDFLAGS} "\$@"
EOF
    chmod +x ${WORKDIR}/build-rust-ccld
    cat > ${CARGO_HOME}/config.toml <<EOF
[target.$(rustc -vV | sed -n 's/^host: //p')]
linker = "${WORKDIR}/build-rust-ccld"
EOF
}

do_compile() {
    export RUSTC_BOOTSTRAP=1
    export CARGO_HOME=${CARGO_HOME}
    export RUSTFLAGS="-Cembed-bitcode=yes -Zforce-unstable-if-unmarked"

    # Run from the library workspace, its .cargo/config.toml points cargo
    # at the vendored dependencies.
    cd ${S}
    cargo build -v --frozen --release --target ${RUST_UEFI_TARGET} \
        --target-dir ${B} -p alloc --features compiler-builtins-mem
}

do_install() {
    install -d ${D}${RUST_UEFI_LIBDIR}
    install -m 0644 ${B}/${RUST_UEFI_TARGET}/release/deps/*.rlib \
        ${D}${RUST_UEFI_LIBDIR}
}
