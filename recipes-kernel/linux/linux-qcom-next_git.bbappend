FILESEXTRAPATHS:prepend := "${THISDIR}/linux-qcom-next:"
SRC_URI:append:ipq9574-rdp433 = " file://configs/ipq9574-rdp433.cfg file://0001-arm64-dts-qcom-ipq9574-Add-the-fallback-compatible-f.patch file://0002-Squashfs-Accept-mtd-names-for-mounting.patch"
