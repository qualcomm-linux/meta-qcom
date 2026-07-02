#
# Copyright (c) Qualcomm Technologies, Inc. and/or its subsidiaries.
#
# SPDX-License-Identifier: MIT
#
# If specified, NHLOS_LICENSE and NHLOS_LICENSE_MD5 specify the licence file
# (and the checksum) for the NHLOS binaries.

NHLOS_LICENSE ??= ""
NHLOS_LICENSE_MD5 ??= ""

# LICENSE needs to be set directly as base.bbclass verified it before the
# anonymous python function has a chance to kick in.
LICENSE = "${@ 'NHLOS-${BPN}' if d.getVar('NHLOS_LICENSE') else 'CLOSED'}"

def handle_nhlos_license(d, pkgs):
    if d.getVar("NHLOS_LICENSE"):
        pn = d.getVar('BPN')
        d.setVarFlag("NO_GENERIC_LICENSE", f"NHLOS-{pn}", '${NHLOS_LICENSE}')
        d.appendVar('SRC_URI', ' file://${NHLOS_LICENSE}')
        d.appendVar('LIC_FILES_CHKSUM', ' file://${NHLOS_LICENSE};md5=${NHLOS_LICENSE_MD5}')
        d.appendVar('PACKAGE_BEFORE_PN', ' ${PN}-license')

        for pkg in pkgs:
            # Depend on the main package to get the license file
            d.appendVar("RDEPENDS:" + pkg, " ${PN}-license")

install_nhlos_license() {
    if [ -n "${NHLOS_LICENSE}" ] ; then
        install -d ${D}${datadir}/${BPN}
        install -m 0644 ${NHLOS_LICENSE} ${D}${datadir}/${BPN}
    fi
}

FILES:${PN}-license = "${@ '${datadir}/${BPN}' if d.getVar('NHLOS_LICENSE') else ''}"
