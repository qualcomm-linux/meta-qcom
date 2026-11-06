DESCRIPTION = "CDT (Configuration Data Table) Firmware for Qualcomm IQ-X7181 SOM-DB5830 (Hamoa COMe) platform"

SRC_URI = " \
    https://${CDT_ARTIFACTORY}/X1E80100/cdt/IQ-X.1.1-COMe-CDT.tar.gz;downloadfilename=cdt-iq-x7181-som-db5830_${PV}.tar.gz;name=cdt-iq-x7181-som-db5830 \
    "
SRC_URI[cdt-iq-x7181-som-db5830.sha256sum] = "e59a3c572e91a11492e7d54f8186e80a06fb999d4a5c738111866efc7db0c85d"

QCOM_CDT_SUBDIR = "iq-x7181/spinor"

include firmware-qcom-cdt-common.inc

# The CDT file name does not contain "cdt", so the common deploy glob misses it
do_deploy() {
    install -d ${DEPLOYDIR}/${QCOM_CDT_SUBDIR}
    install -m 0644 ${UNPACKDIR}/IQ-X.1.1-COMe-CDT/HAMOA_COMe_0.1.0.bin ${DEPLOYDIR}/${QCOM_CDT_SUBDIR}
}
