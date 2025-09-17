SUMMARY = "QCOM security opensource packagegroup"
DESCRIPTION = "QCOM security components provide smcinvoke functionalty"

inherit packagegroup

PACKAGES = " \
    ${PN}-tests \
"

RDEPENDS:${PN}-test += " \
    qcbor-bin \
"

