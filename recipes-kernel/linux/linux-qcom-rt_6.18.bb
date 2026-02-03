require linux-qcom_6.18.bb

FILESEXTRAPATHS:prepend := "${THISDIR}/linux-qcom-6.18:"

KBUILD_CONFIG_EXTRA:append:aarch64 = " ${S}/arch/arm64/configs/rt.config"
