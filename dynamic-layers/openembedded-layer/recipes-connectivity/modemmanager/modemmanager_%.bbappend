PACKAGECONFIG:remove:qcom = "mbim"
PACKAGECONFIG:append:qcom = " qrtr"
EXTRA_OEMESON:remove:qcom = "-Dqrtr=false"
