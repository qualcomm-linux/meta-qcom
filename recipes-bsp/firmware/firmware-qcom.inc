inherit allarch

FILES:${PN} += "${nonarch_base_libdir}/firmware/"

INSANE_SKIP:${PN} += "arch already-stripped"

# Default settings
# package name part in linux-firmware-qcom-...-audio
FW_QCOM_NAME ?= "unset"
# Subdir inside /lib/firmware/qcom. Typically it is equal to FW_QCOM_NAME but might differ in complex cases (like C630 Yoga).
FW_QCOM_SUBDIR ?= "${FW_QCOM_NAME}"
FW_QCOM_BASE_PATH = "${nonarch_base_libdir}/firmware/qcom"
FW_QCOM_PATH = "${FW_QCOM_BASE_PATH}/${FW_QCOM_SUBDIR}"

SPLIT_FIRMWARE_PACKAGES ?= ""

PACKAGE_BEFORE_PN += "${SPLIT_FIRMWARE_PACKAGES}"
# RRECOMMEND all non-split packages split from this recipe. Split firmware files re usually replaced with the squashed ones.
RRECOMMENDS:${PN} += "${@ ' '.join(filter(lambda p: not p.endswith('-split'), d.getVar('SPLIT_FIRMWARE_PACKAGES').split())) }"

# Default settings for several split packages
FILES:linux-firmware-qcom-${FW_QCOM_NAME}-adreno = "${FW_QCOM_PATH}/*_zap.mbn"
FILES:linux-firmware-qcom-${FW_QCOM_NAME}-audio = "${FW_QCOM_PATH}/adsp.mbn ${FW_QCOM_PATH}/adsp_dtb.mbn ${FW_QCOM_PATH}/adsp*.jsn ${FW_QCOM_PATH}/battmgr.jsn"
FILES:linux-firmware-qcom-${FW_QCOM_NAME}-audio-split = "${FW_QCOM_PATH}/adsp.mdt ${FW_QCOM_PATH}/adsp.b*"
FILES:linux-firmware-qcom-${FW_QCOM_NAME}-compute = "${FW_QCOM_PATH}/cdsp.mbn ${FW_QCOM_PATH}/cdsp_dtb.mbn ${FW_QCOM_PATH}/cdsp*.jsn"
FILES:linux-firmware-qcom-${FW_QCOM_NAME}-compute-split = "${FW_QCOM_PATH}/cdsp.mdt ${FW_QCOM_PATH}/cdsp.b*"
FILES:linux-firmware-qcom-${FW_QCOM_NAME}-dsps = "${FW_QCOM_PATH}/dsps.mbn"
FILES:linux-firmware-qcom-${FW_QCOM_NAME}-gss = "${FW_QCOM_PATH}/gss.mbn"
FILES:linux-firmware-qcom-${FW_QCOM_NAME}-ipa = "${FW_QCOM_PATH}/ipa_fws.mbn ${FW_QCOM_PATH}/ipa_fws.elf"
FILES:linux-firmware-qcom-${FW_QCOM_NAME}-modem = "${FW_QCOM_PATH}/mba.mbn ${FW_QCOM_PATH}/modem.mbn ${FW_QCOM_PATH}/modem*.jsn"
FILES:linux-firmware-qcom-${FW_QCOM_NAME}-modem-split = "${FW_QCOM_PATH}/modem.mdt ${FW_QCOM_PATH}/modem.b*"
FILES:linux-firmware-qcom-${FW_QCOM_NAME}-q6 = "${FW_QCOM_PATH}/q6.mbn"
FILES:linux-firmware-qcom-${FW_QCOM_NAME}-sensors = "${FW_QCOM_PATH}/slpi.mbn ${FW_QCOM_PATH}/slpi*.jsn"
FILES:linux-firmware-qcom-${FW_QCOM_NAME}-sensors-split = "${FW_QCOM_PATH}/slpi.mdt ${FW_QCOM_PATH}/slpi.b*"
FILES:linux-firmware-qcom-${FW_QCOM_NAME}-venus = "${FW_QCOM_PATH}/venus.mbn ${FW_QCOM_PATH}/vidc*"
FILES:linux-firmware-qcom-${FW_QCOM_NAME}-wifi = "${FW_QCOM_PATH}/wcnss.mbn ${FW_QCOM_PATH}/wlanmdsp.mbn"
FILES:linux-firmware-qcom-${FW_QCOM_NAME}-wifi-split = "${FW_QCOM_PATH}/wcnss.mdt ${FW_QCOM_PATH}/wcnss.b*"

python() {
    pn = d.getVar("PN")
    insanes = d.getVar("INSANE_SKIP:%s" % pn)
    for pkg in d.getVar("SPLIT_FIRMWARE_PACKAGES").split():
        # Depend on the main package to get the license file
        d.appendVar("RDEPENDS:" + pkg, " " + pn)
        # and append the INSANE_SKIP of the main package to pass QA
        d.appendVar("INSANE_SKIP:" + pkg, " " + insanes)
        # If it's a package with the split frmware, depend on non-split files (for jsn files, etc)
        if pkg.endswith("-split"):
            d.appendVar("RDEPENDS:" + pkg, " " + pkg[:-6])
    if d.getVar("FW_QCOM_NAME") == "unset" and d.getVar("SPLIT_FIRMWARE_PACKAGES") != "":
        bb.error("%s: split firmware-qcom packages engaged, but FW_QCOM_NAME is not defined" % pn)
}

INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_DEFAULT_DEPS = "1"
