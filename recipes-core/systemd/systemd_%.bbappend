FILESEXTRAPATHS:prepend:qcom := "${THISDIR}/${PN}:"

SRC_URI:append:qcom = " \
    file://0001-boot-stub-honor-PE-SectionAlignment-when-loading-inn.patch \
    file://99-dma-heap.rules \
"

# Create a group dmaheap and add this group to /dev/dma_heap/system through
# dma-heap rules.
GROUPADD_PARAM:udev:append:qcom = "; -r dmaheap"

do_install:append:qcom() {
    install -d ${D}${nonarch_libdir}/udev/rules.d
    install -m 0644 ${UNPACKDIR}/99-dma-heap.rules \
        ${D}${nonarch_libdir}/udev/rules.d/
}

FILES:${PN}-udev-rules:append:qcom = " ${nonarch_libdir}/udev/rules.d/99-dma-heap.rules"

# Install the machine-specific raw partition rules whenever udev is installed.
RRECOMMENDS:udev:append:qcom = " qcom-raw-partitions-udev-rules"
