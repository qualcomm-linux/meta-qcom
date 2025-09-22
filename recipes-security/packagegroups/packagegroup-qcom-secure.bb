SUMMARY = "QCOM secure propreitary packagegroup"

inherit packagegroup

PACKAGE_ARCH = "${MACHINE_ARCH}"

PACKAGES = "${PN}"

PROVIDES = "${PACKAGES}"

RDEPENDS:${PN} += " \
  minkipc-git \
  qcomtee-git \
  qcbor-git \
"
