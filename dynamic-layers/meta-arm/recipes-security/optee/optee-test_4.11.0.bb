require recipes-security/optee/optee-test.inc

# v4.11.0
SRCREV = "179740b43809069ee567d58ebd8e0f85ef8168e2"

LIC_FILES_CHKSUM = "file://LICENSE.md;md5=a8fa504109e4cd7ea575bc49ea4be560 \
                    file://LICENSE-BSD;md5=dca16d6efa93b55d0fd662ae5cd6feeb \
                    file://LICENSE-GPL;md5=10e86b5d2a6cb0e2b9dcfdd26a9ac58d \
                   "

# Include ffa_spmc test group if the SPMC test is enabled.
# Supported after op-tee v3.20
EXTRA_OEMAKE:append = "${@bb.utils.contains('MACHINE_FEATURES', 'optee-spmc-test', \
                                        ' CFG_SPMC_TESTS=y CFG_SECURE_PARTITION=y', '' , d)}"

RDEPENDS:${PN} += "${@bb.utils.contains('MACHINE_FEATURES', 'optee-spmc-test', \
                                              ' arm-ffa-user', '' , d)}"

# run-ptest ships with the meta-arm recipes
FILESEXTRAPATHS:prepend := "${@os.path.dirname(bb.utils.which(d.getVar('BBPATH'), 'recipes-security/optee/optee-test/run-ptest'))}:"

# Don't affect other SoCs and distros that also include meta-qcom.
# optee.inc sets COMPATIBLE_MACHINE per machine, so skip the recipe instead.
python () {
    if 'qcom' not in d.getVar('MACHINEOVERRIDES').split(':'):
        raise bb.parse.SkipRecipe("only for qcom machines")
}
