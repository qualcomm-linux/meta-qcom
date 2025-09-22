SUMMARY = "QCOM security opensource packagegroup"

inherit packagegroup

PACKAGES = "${PN}"

RDEPENDS:${PN} += " \
    minkipc \
    qcomtee \
    qcbor \
"
