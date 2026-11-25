DESCRIPTION = "CDT (Configuration Data Table) Firmware for Qualcomm QCS8300 platform"

SRC_URI = " \
    https://${CDT_ARTIFACTORY}/QCS8300/cdt/ride-sx.zip;downloadfilename=cdt-qcs8300-ride-sx_${PV}.zip;name=qcs8300-ride-sx \
    https://${CDT_ARTIFACTORY}/QCS8300/cdt/qcs8275-iq-8275-evk-pro-sku.zip;downloadfilename=cdt-iq8275-evk-pro-sku_${PV}.zip;name=cdt-iq8275-evk-pro-sku \
    https://${CDT_ARTIFACTORY}/QCS8300/cdt/qcs8275-iq-8275-evk.zip;downloadfilename=cdt-iq8275-evk_${PV}.zip;name=cdt-iq8275-evk \
    "
SRC_URI[qcs8300-ride-sx.sha256sum] = "d7fc667372b28383a36d586333097d84b9d9c104f4dd1845d33904e2d6b39f80"
SRC_URI[cdt-iq8275-evk-pro-sku.sha256sum] = "cbe2009c8ef7dbacd716141bf01b8e1b26788c4a4f3145e60fe3b4a6b3aabc04"
SRC_URI[cdt-iq8275-evk.sha256sum] = "070a61e6442316234b72af203c03a2cab82a1a5b8822f0af0d3fcca294ffda55"

QCOM_CDT_SUBDIR = "qcs8300"

include firmware-qcom-cdt-common.inc
