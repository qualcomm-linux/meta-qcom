SUMMARY = "Packages for the OP-TEE support"

inherit packagegroup

RRECOMMENDS:${PN} = " \
    optee-client \
    optee-test \
    optee-os-ta \
"
