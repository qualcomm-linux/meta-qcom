SUMMARY = "QCOM security opensource packagegroup"
DESCRIPTION = "QCOM security components provide smcinvoke functionalty"

inherit packagegroup

PACKAGES = " \
    ${PN} \
    ${PN}-tests \
"

RDEPENDS:${PN} += " \
    minkipc \
"

RDEPENDS:${PN}-test += " \
    qcomtee-bin \
    qcbor-bin \
"

