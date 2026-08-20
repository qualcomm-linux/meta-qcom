FILESEXTRAPATHS:prepend:qcom := "${THISDIR}/systemd:"

SRC_URI:append:qcom = " \
    file://0001-boot-stub-honor-PE-SectionAlignment-when-loading-inn.patch \
    file://0002-boot-downgrade-EFI_MEMORY_ATTRIBUTE_PROTOCOL-warning.patch \
"
