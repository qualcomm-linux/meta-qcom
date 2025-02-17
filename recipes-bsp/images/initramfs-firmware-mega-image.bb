DESCRIPTION = "Huge image with all firmware files. This is intended to check for possible conflicts, etc."

PACKAGE_INSTALL = " \
    linux-firmware \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wifi', 'wireless-regdb-static', '', d)} \
"

# Qualcomm Dragonboard / Robotics platforms
PACKAGE_INSTALL += " \
    packagegroup-firmware-dragonboard-apq8074 \
    packagegroup-firmware-dragonboard410c \
    packagegroup-firmware-dragonboard820c \
    packagegroup-firmware-dragonboard845c \
    packagegroup-firmware-rb1 \
    packagegroup-firmware-rb2 \
    packagegroup-firmware-rb3gen2 \
    packagegroup-firmware-rb5 \
"

# Qualcomm HDKs
PACKAGE_INSTALL += " \
    packagegroup-firmware-sm8150-hdk \
    packagegroup-firmware-sm8350-hdk \
    packagegroup-firmware-sm8450-hdk \
    packagegroup-firmware-sm8550-hdk \
    packagegroup-firmware-sm8650-hdk \
"

# Other Qualcomm DevKits
PACKAGE_INSTALL += " \
    packagegroup-firmware-qar2130p \
"

require initramfs-firmware-image.inc
