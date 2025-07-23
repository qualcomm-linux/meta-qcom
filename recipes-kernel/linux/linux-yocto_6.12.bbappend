
SRC_URI:append:qcom = " \
    file://generic-drivers/0001-FROMLIST-dma-heap-Add-proper-kref-handling-on-dma-bu.patch \
    file://generic-drivers/0002-FROMLIST-dma-heap-Provide-accessors-so-that-in-kerne.patch \
    file://qcm6490-board-dts/0001-FROMLIST-arm64-dts-qcom-qcm6490-idp-Update-protected.patch \
    file://qcm6490-board-dts/0003-BACKPORT-FROMLIST-arm64-dts-qcom-qcs6490-rb3gen2-Upd.patch \
    file://qcm6490-board-dts/0001-PENDING-arm64-dts-qcom-qcm6490-Add-UFS-nodes-for-IDP.patch \
    file://qcm6490-board-dts/0002-PENDING-arm64-dts-qcom-Add-UFS-nodes-for-qcs6490-rb3.patch \
    file://workarounds/0001-QCLINUX-arm64-dts-qcom-qcm6490-disable-sdhc1-for-ufs.patch \
    file://workarounds/0001-PENDING-arm64-dts-qcom-Remove-voltage-vote-support-f.patch \
    file://workarounds/0002-PENDING-arm64-dts-qcom-Remove-voltage-vote-support-f.patch \
    file://workarounds/0001-PENDING-arm64-dts-qcm6490-Remove-voltage-voting-for-.patch \
    file://workarounds/0002-PENDING-arm64-dts-qcs6490-rb3-Remove-voltage-voting-.patch \
    file://generic-drivers/0001-Bluetooth-qca-Expand-firmware-name-to-load-specific-.patch \
    file://generic-drivers/0001-dt-bindings-net-bluetooth-qualcomm-document-WCN3950.patch \
    file://generic-drivers/0003-Bluetooth-qca-add-WCN3950-support.patch \
    file://generic-drivers/0001-Bluetooth-qca-fix-NV-variant-for-one-of-WCN3950-SoCs.patch \
    file://qrb2210-board-dts/0005-arm64-dts-qcom-qcm2290-add-UART3-device.patch \
    file://qrb2210-board-dts/0006-arm64-dts-qcom-qrb2210-rb1-add-Bluetooth-support.patch \
"
