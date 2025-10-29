SRC_URI += "https://github.com/coreboot/qc_blobs/raw/refs/heads/master/sc7280/qtiseclib/libqtisec.a;name=qtiseclib"
SRC_URI[qtiseclib.sha256sum] = "6860dda0701c8709530608cc0e5a61b76484ae16cb673ba9a23510cf4b3d57bf"

COMPATIBLE_MACHINE = "qcm6490"
TFA_PLATFORM = "rb3gen2"
TFA_BUILD_TARGET = "bl2 fip"
EXTRA_OEMAKE:append = " \
    QTISECLIB_PATH=${UNPACKDIR}/libqtisec.a \
   "
