DESCRIPTION = "CDT (Configuration Data Table) Firmware for Qualcomm QCS615 platform"

SRC_URI = " \
    https://${CDT_ARTIFACTORY}/QCS615/cdt/ADP_AIR_SA6155P_V2.zip;downloadfilename=cdt-qcs615-adp-air_${PV}.zip;name=qcs615-adp-air \
    https://${CDT_ARTIFACTORY}/QCS615/cdt/EVK_SA6150P.zip;downloadfilename=cdt-iq-615-evk_${PV}.zip;name=iq-615-evk \
    "
SRC_URI[qcs615-adp-air.sha256sum] = "37d99eb113e286400bce0d70aa12a74d05f93d01f045bf67e7a46b3c606c8fd0"
SRC_URI[iq-615-evk.sha256sum] = "e8ea707a27090e4338c709cabdad46159c06cd91e7c2fc5348f5d98db7bbb802"

QCOM_CDT_SUBDIR = "qcs615"
QCOM_CDT_SUBDIR_ALIASES = "qcs615-talos"

do_deploy:append() {
    for subdir in ${QCOM_CDT_SUBDIR_ALIASES}; do
        [ -z "$subdir" ] && continue
        install -d ${DEPLOYDIR}/$subdir
        cp -a ${DEPLOYDIR}/${QCOM_CDT_SUBDIR}/. ${DEPLOYDIR}/$subdir/
    done
}

include firmware-qcom-cdt-common.inc
