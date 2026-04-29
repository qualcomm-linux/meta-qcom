require linux-yocto-qcom.inc

SRC_URI:append:qcom = " \
    file://workarounds/f553aff9a3ab245e722349cc617bcdfe778c69af.patch \
    file://monaco-evk-dts/0001-arm64-dts-qcom-monaco-evk-camera-Add-DT-overlay.patch \
    file://hamoa-iot-evk-dts/0001-arm64-dts-qcom-hamoa-iot-evk-camera-imx577-Add-DT-ov.patch \
    file://generic-drivers/scm/0001-firmware-qcom_scm-Rename-peripheral-as-pas_id.patch \
    file://generic-drivers/scm/0002-firmware-qcom_scm-Introduce-PAS-context-allocator-he.patch \
    file://generic-drivers/remoteproc/0003-remoteproc-pas-Replace-metadata-context-with-PAS-con.patch \
    file://generic-drivers/mdtloader/0004-soc-qcom-mdtloader-Add-PAS-context-aware-qcom_mdt_pa.patch \
    file://generic-drivers/mdtloader/0005-soc-qcom-mdtloader-Remove-qcom_mdt_pas_init-from-exp.patch \
    file://generic-drivers/scm/0006-firmware-qcom_scm-Add-a-prep-version-of-auth_and_res.patch \
    file://generic-drivers/scm/0007-firmware-qcom_scm-Refactor-qcom_scm_pas_init_image.patch \
    file://generic-drivers/scm/0008-firmware-qcom_scm-Add-SHM-bridge-handling-for-PAS-wh.patch \
    file://generic-drivers/scm/0009-firmware-qcom_scm-Add-qcom_scm_pas_get_rsc_table-to-.patch \
    file://generic-drivers/remoteproc/0010-remoteproc-pas-Extend-parse_fw-callback-to-fetch-res.patch \
    file://generic-drivers/remoteproc/0011-remoteproc-qcom-pas-Enable-Secure-PAS-support-with-I.patch \
    file://generic-drivers/remoteproc/0012-FROMLIST-remoteproc-qcom-pas-Map-unmap-subsystem-reg.patch \
"
