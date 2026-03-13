FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

# --- Enable QRTR support, disable MBIM ---
PACKAGECONFIG:remove = "mbim"
PACKAGECONFIG:append = " qrtr"

# --- Allow QRTR to be enabled by meson ---
EXTRA_OEMESON:remove = "-Dqrtr=false"

# --- Enable Qualcomm SoC plugin (required for QRTR/MM on QCOM) ---
EXTRA_OECONF:append = " --enable-plugin-qcom-soc"

SRC_URI += "file://0001-enable-mhi-support.patch"
