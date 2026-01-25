require camera-service.inc

DEPENDS += "camera-common camxlib-kodiak"

# Build kodiak specific binaries
EXTRA_OECMAKE:append = " \
    -DBUILD_CATEGORY=TARGET \
    -DTARGET_BOARD_PLATFORM=qcm6490 \
    -DTARGET_SUFFIX=_kodiak \
"
