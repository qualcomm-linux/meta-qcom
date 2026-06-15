FILESEXTRAPATHS:prepend:qcom := "${THISDIR}/${PN}:"

SRC_URI:append:qcom = " \
    file://0001-boot-stub-honor-PE-SectionAlignment-when-loading-inn.patch \
"
