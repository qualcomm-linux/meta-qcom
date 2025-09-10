SUMMARY = "QCOM Graphics package group"

inherit packagegroup

PROVIDES = "${PACKAGES}"

PACKAGES = "${PN}"

RDEPENDS:${PN} = " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'opengl', 'libglesv1-cm-adreno1', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'vulkan', 'libvulkan-adreno1', '', d)} \
    qcom-adreno-cl \
"

