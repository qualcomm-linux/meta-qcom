SUMMARY = "Packages for the OP-TEE support"

inherit packagegroup

RRECOMMENDS:${PN} = " \
    ${@bb.utils.contains('MACHINE_FEATURES', 'optee', 'optee-client', '', d)} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'optee', 'optee-test', '', d)} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'optee', 'optee-os-ta', '', d)} \
"
