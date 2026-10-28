SUMMARY = "Packages for the OP-TEE support"

inherit packagegroup

OPTEE_OS_TA_PACKAGE = ""
OPTEE_OS_TA_PACKAGE:qcm6490 = "optee-os-qcom-kodiak-ta"
OPTEE_OS_TA_PACKAGE:qcs9100 = "optee-os-qcom-lemans-ta"

RRECOMMENDS:${PN} = " \
    ${@bb.utils.contains('MACHINE_FEATURES', 'optee', 'optee-os-tadevkit-qcom ${OPTEE_OS_TA_PACKAGE} optee-client optee-test', '', d)} \
"
