#
# Copyright (c) Qualcomm Technologies, Inc. and/or its subsidiaries.
#
# SPDX-License-Identifier: MIT
#

# Packages the Hexagon DSP libraries, which the recipe installs into
# ${datadir}/qcom/<version>, as ${PN}-hexagon-<version>, one package for
# each version the recipe ships libraries for.

PACKAGES_DYNAMIC += "^${PN}-hexagon-.*"

# Runtime dependencies of each of the Hexagon packages
QCOM_HEXAGON_RDEPENDS ?= ""

# The QA settings are read from the recipe, so they have to be set for all
# known versions, not only for the packages which end up being created.
python () {
    for arch in (d.getVar('QCOM_HEXAGON_KNOWN_ARCHS') or '').split():
        pkg = '%s-hexagon-%s' % (d.getVar('PN'), arch)
        # Hexagon binaries: neither the target architecture nor the host file
        # dependencies apply to them.
        d.setVar('SKIP_FILEDEPS:' + pkg, '1')
        d.appendVar('INSANE_SKIP:' + pkg, ' arch libdir ldflags file-rdeps')
}

python populate_packages:prepend() {
    do_split_packages(d, '${datadir}/qcom', r'^(v\d+)$',
                      '${PN}-hexagon-%s', 'Hexagon %s DSP libraries',
                      allow_dirs=True, extra_depends='${QCOM_HEXAGON_RDEPENDS}')
}
