PLATFORM = "talos"
PBT_BUILD_DATE = "260814"

require common.inc

SRC_URI[camxlib.sha256sum] = "d29605a4aa4c104435efe029ee5cfcb41e26c83144341dd24d7634eb572388f2"
SRC_URI[camx.sha256sum] = "f06fe9e79ba59fdeeef16f3ce041abb3ce306d01933f63604e0e92f4aabd3440"
SRC_URI[chicdk.sha256sum] = "e2c276f278ccdb38cb1ccea120e7a84417b671760e566d5234c45933abee12a7"
SRC_URI[camxcommon.sha256sum] = "e048dd98f96807ff34dd54651b8ba045a057d6c1d0aa49cfc658ff256f79441f"

RRECOMMENDS:${PN} += "${@bb.utils.contains('DISTRO_FEATURES', 'opencl', 'virtual-opencl-icd', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'opengl', 'libglvnd', '', d)}"
