require linux-qcom_6.18.bb

KBUILD_CONFIG_EXTRA:append:aarch64 = " ${S}/arch/arm64/configs/rt.config"
