PACKAGECONFIG:append:qcom = " tests"

# Only on ARMv8 Qualcomm machines
PACKAGECONFIG:append:qcom:aarch64 = " fastcv"

DEPENDS:append:qcom:aarch64 = " qcom-fastcv-binaries"