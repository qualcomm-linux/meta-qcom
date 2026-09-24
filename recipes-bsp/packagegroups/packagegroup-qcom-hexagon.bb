SUMMARY = "Hexagon DSP libraries for the Hexagon versions of the machine"

PACKAGE_ARCH = "${MACHINE_ARCH}"

# The Hexagon libraries are only packaged for ARMv8 (aarch64) machines.
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:aarch64 = "(.*)"

inherit packagegroup

PACKAGES = " \
    ${PN}-fastcv \
    ${PN}-qairt \
"

# Machines not listing their Hexagon versions get all of them
def qcom_hexagon_packages(prefix, d):
    archs = d.getVar('QCOM_HEXAGON_ARCHS') or d.getVar('QCOM_HEXAGON_KNOWN_ARCHS') or ''
    return ' '.join(prefix + arch for arch in archs.split())

RRECOMMENDS:${PN}-fastcv = "${@qcom_hexagon_packages('qcom-fastcv-binaries-hexagon-', d)}"
RRECOMMENDS:${PN}-qairt = "${@qcom_hexagon_packages('qairt-sdk-hexagon-', d)}"
