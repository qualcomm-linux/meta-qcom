DESCRIPTION = "Huge image with all firmware files. This is intended to check for possible conflicts, etc."

PACKAGE_INSTALL = " \
    linux-firmware \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wifi', 'wireless-regdb-static', '', d)} \
"

# Qualcomm Dragonboard / Robotics platforms
PACKAGE_INSTALL += " \
    packagegroup-dragonboard410c-firmware \
    packagegroup-dragonboard820c-firmware \
    packagegroup-dragonboard845c-firmware \
    packagegroup-hamoa-iot-evk-firmware \
    packagegroup-purwa-iot-evk-firmware \
    packagegroup-iq-8275-evk-firmware \
    packagegroup-iq-9075-evk-firmware \
    packagegroup-qcs615-ride-firmware \
    packagegroup-qcs8300-ride-firmware \
    packagegroup-rb1-firmware \
    packagegroup-rb2-firmware \
    packagegroup-rb3gen2-firmware \
    packagegroup-rb5-firmware \
    packagegroup-sa8775p-ride-firmware \
    packagegroup-dragonboard820c-hexagon-dsp-binaries \
    packagegroup-dragonboard845c-hexagon-dsp-binaries \
    packagegroup-hamoa-iot-evk-hexagon-dsp-binaries \
    packagegroup-purwa-iot-evk-hexagon-dsp-binaries \
    packagegroup-iq-8275-evk-hexagon-dsp-binaries \
    packagegroup-iq-9075-evk-hexagon-dsp-binaries \
    packagegroup-qcs615-ride-hexagon-dsp-binaries \
    packagegroup-qcs8300-ride-hexagon-dsp-binaries \
    packagegroup-rb1-hexagon-dsp-binaries \
    packagegroup-rb2-hexagon-dsp-binaries \
    packagegroup-rb3gen2-hexagon-dsp-binaries \
    packagegroup-rb5-hexagon-dsp-binaries \
    packagegroup-sa8775p-ride-hexagon-dsp-binaries \
"

require initramfs-firmware-image.inc
