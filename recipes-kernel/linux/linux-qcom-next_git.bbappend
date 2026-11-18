SUMMARY = "Qualcomm Glymur USB4 uC firmware (local, pre-upstream)"
LICENSE = "CLOSED"

USB4_FW_DIR := "${THISDIR}/linux-qcom-next/usb4-fw-glymur"

do_configure[file-checksums] += " \
    ${USB4_FW_DIR}/usb4.bin:True \
    ${USB4_FW_DIR}/usb4_v2.bin:True \
"

do_configure:prepend() {
    install -d ${S}/fw/qcom/glymur
    install -m 0644 ${USB4_FW_DIR}/usb4.bin    ${S}/fw/qcom/glymur/usb4.bin
    install -m 0644 ${USB4_FW_DIR}/usb4_v2.bin ${S}/fw/qcom/glymur/usb4_v2.bin
}