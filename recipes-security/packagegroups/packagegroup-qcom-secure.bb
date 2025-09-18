SUMMARY = "QCOM secure propreitary packagegroup"

inherit packagegroup

PACKAGE_ARCH = "${MACHINE_ARCH}"

PACKAGES = "${PN}"

PROVIDES = "${PACKAGES}"

RDEPENDS:${PN} += " \
  qcomtee-git \
  qcbor-git \
"
