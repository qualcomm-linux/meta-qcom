DESCRIPTION = "CDT (Configuration Data Table) Firmware for Qualcomm Nord platforms"

SRC_URI = " \
    https://${CDT_ARTIFACTORY}/IQ10/cdt/ride-sx.zip;downloadfilename=nord-ride-sx_${PV}.zip;name=nord-ride-sx \
    "
SRC_URI[nord-ride-sx.sha256sum] = "d95a02eb00ec656f140a6ab7a055020396120f07aaf128134a6b88598c1d2162"

QCOM_CDT_SUBDIR = "nord"

include firmware-qcom-cdt-common.inc
