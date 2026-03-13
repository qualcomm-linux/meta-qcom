# Native recipe that builds the EDK2 BaseTools host utilities required for
# UEFI capsule generation on Qualcomm platforms:
#
#   GenFfs  - packages firmware files into Firmware File System (FFS) sections
#   GenFv   - assembles a Firmware Volume (FV) from FFS files
#
# Also installs GenerateCapsule.py and the Common/ Python library so that
# firmware-qcom-capsule can consume them without fetching edk2 a second time.
#
# Only the three sub-directories needed by GenFfs/GenFv are built:
#   Common, BrotliCompress, GenFfs, GenFv
# VfrCompile (UEFI-forms compiler) and other BaseTools are intentionally
# skipped - they are not needed for capsule generation and VfrCompile
# requires the Pccts/antlr parser-generator which fails in a native build.

DESCRIPTION = "EDK2 BaseTools host utilities (GenFfs, GenFv) for UEFI capsule generation"
HOMEPAGE    = "https://github.com/tianocore/edk2"
LICENSE     = "BSD-2-Clause-Patent"
LIC_FILES_CHKSUM = "file://License.txt;md5=2b415520383f7964e96700ae12b4570a"

# ---------------------------------------------------------------------------
# Sources
# ---------------------------------------------------------------------------

# EDK2 HEAD commit used by capsule_setup.py to build GenFfs/GenFv and supply
# GenerateCapsule.py + Common/ library.
# NOTE: We do NOT fetch submodules automatically.
#       Only BaseTools/Source/C/BrotliCompress/brotli is required for the
#       BaseTools make; it is declared explicitly below (SRCREV_brotli).
SRCREV_edk2   = "1fe2504afb6ed8a991d5cfe2e3cf1d39afb47b95"

# brotli - sole submodule needed by BaseTools/Source/C (BrotliCompress tool).
# Pinned to the commit that the edk2 tree above declares in .gitmodules.
SRCREV_brotli = "e230f474b87134e8c6c85b630084c612057f253e"

SRCREV_FORMAT = "edk2_brotli"

SRC_URI = " \
    git://github.com/tianocore/edk2.git;protocol=https;nobranch=1;name=edk2;destsuffix=edk2 \
    git://github.com/google/brotli.git;protocol=https;nobranch=1;name=brotli;destsuffix=edk2/BaseTools/Source/C/BrotliCompress/brotli \
"

S = "${UNPACKDIR}/edk2"

inherit native

do_configure[noexec] = "1"

do_compile() {
    # Clean any stale objects from a prior failed attempt before building,
    # then build only the sub-directories required by GenFfs and GenFv in
    # dependency order.
    BASE_C="${S}/BaseTools/Source/C"

    for d in Common BrotliCompress GenFfs GenFv; do
        make -C "${BASE_C}/${d}" clean 2>/dev/null || true
    done
    rm -f "${BASE_C}/bin/GenFfs" "${BASE_C}/bin/GenFv"

    make -C "${BASE_C}/Common"         -j"${BB_NUMBER_THREADS}"
    make -C "${BASE_C}/BrotliCompress" -j"${BB_NUMBER_THREADS}"
    make -C "${BASE_C}/GenFfs"         -j"${BB_NUMBER_THREADS}"
    make -C "${BASE_C}/GenFv"          -j"${BB_NUMBER_THREADS}"
}

do_install() {
    # Install GenFfs and GenFv to bindir so firmware-qcom-capsule can
    # reference them via ${STAGING_BINDIR_NATIVE}
    install -d "${D}${bindir}"
    install -m 0755 "${S}/BaseTools/Source/C/bin/GenFfs" "${D}${bindir}/"
    install -m 0755 "${S}/BaseTools/Source/C/bin/GenFv"  "${D}${bindir}/"

    # Install GenerateCapsule.py and Common/ Python library so that
    # firmware-qcom-capsule does not need its own edk2 fetch
    install -d "${D}${datadir}/edk2-basetools"
    install -m 0644 \
        "${S}/BaseTools/Source/Python/Capsule/GenerateCapsule.py" \
        "${D}${datadir}/edk2-basetools/"
    cp -r "${S}/BaseTools/Source/Python/Common" \
        "${D}${datadir}/edk2-basetools/"
}
