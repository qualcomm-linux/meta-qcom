require linux-qcom-next_git.bb

FILESEXTRAPATHS:prepend := "${THISDIR}/linux-qcom-next:"

KBUILD_CONFIG_EXTRA:append:aarch64 = " ${S}/arch/arm64/configs/rt.config"
