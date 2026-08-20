SUMMARY = "Qualcomm Device Tree Editor (QDTE)"
DESCRIPTION = "QDTE is a Tkinter-based Device Tree editor with a --nogui mode for \
scripted modification of DTB payloads"
HOMEPAGE = "https://github.com/qualcomm/DTE"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=57272fa9cc740c745feb331231cca6f2"

SRC_URI = " \
    git://github.com/qualcomm/DTE.git;protocol=https;branch=main \
    file://0001-controller-make-non_hlos_parser-import-optional.patch \
    file://0002-run-stub-tkinter-for-nogui-mode.patch \
    file://0003-controller-guard-quts2-lookup-on-Linux.patch \
    file://0004-dtwrapper-support-upstream-pyfdt-s-zero-arg-to_dtb.patch \
    file://0005-dtwrapper-accept-hex-oct-bin-literals-in-FdtProperty.patch \
"
SRCREV = "7e3e493250c6a69a83e4f25dcb37d120e77f9dd8"

S = "${UNPACKDIR}/${BPN}-${PV}"

inherit native python3native

# Runtime imports needed for --nogui mode (after patches applied):
#   run.py        -> six (tkinter stubbed by 0002)
#   controller.py -> non_hlos_parser stub (provided by 0001)
#   dtwrapper.py  -> pyfdt
DEPENDS += " \
    python3-six-native \
    python3-pyfdt-native \
"

do_configure[noexec] = "1"
do_compile[noexec]   = "1"

# QDTE has no install target. Stage the source tree under ${datadir} and
# drop a thin wrapper into ${bindir} so consumers can invoke `qdte` from
# PATH.
do_install() {
    install -d "${D}${datadir}/qdte"
    cp -r "${S}"/* "${D}${datadir}/qdte/"

    install -d "${D}${bindir}"
    cat > "${D}${bindir}/qdte" <<EOF
#!/bin/sh
# QDTE wrapper -- runs the upstream run.py with PYTHONPATH set so its
# sibling modules resolve, and propagates all CLI args.
QDTE_DIR="${datadir}/qdte"
export PYTHONPATH="\${QDTE_DIR}\${PYTHONPATH:+:\$PYTHONPATH}"

# Several QDTE code paths shell out via subprocess as \`python <script>\`
# (see assemble.py, Autocmd.py, version_2_assemble.py, XBLConfig/*.py).
# Yocto native sysroots ship only python3, so create an ephemeral PATH
# shim with a \`python\` symlink to the same interpreter that's running
# us. The shim is cleaned up on exit and never written into the
# sysroot bindir, so other recipes are unaffected.
SHIM_DIR=\$(mktemp -d "\${TMPDIR:-/tmp}/qdte-pyshim.XXXXXX")
trap 'rm -rf "\$SHIM_DIR"' EXIT
ln -s "\$(command -v python3)" "\$SHIM_DIR/python"
export PATH="\$SHIM_DIR:\$PATH"

exec python3 "\${QDTE_DIR}/run.py" "\$@"
EOF
    chmod 0755 "${D}${bindir}/qdte"
}

FILES:${PN} += "${datadir}/qdte"
