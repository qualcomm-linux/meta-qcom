require trusted-firmware-a-qcom.inc

DEPENDS += "optee-os-qcom-kodiak"

TFA_PLATFORM = "rb3gen2"
QTISECLIB_SOC = "sc7280"
FIP_ELF_ADDR = "0x9fc00000"

SRC_URI += "git://github.com/coreboot/qc_blobs.git;protocol=https;name=qc-blobs;subdir=qc_blobs;branch=main"
SRCREV_qc-blobs = "16207f367ddbf280a0c2c8a3d3ee454a59710a25"
SRCREV_FORMAT .= "_qc-blobs"

LICENSE += "AND LicenseRef-LICENSE.qcom"
LIC_FILES_CHKSUM += "file://${UNPACKDIR}/qc_blobs/${QTISECLIB_SOC}/qtiseclib/LICENSE;md5=fa83f30385e617b56ef0934f13645621"

EXTRA_OEMAKE:append = " QTISECLIB_PATH=${UNPACKDIR}/qc_blobs/${QTISECLIB_SOC}/qtiseclib/libqtisec.a"
