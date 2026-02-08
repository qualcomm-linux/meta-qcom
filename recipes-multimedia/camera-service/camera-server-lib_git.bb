require camera-service.inc

DEPENDS += "camera-common camxlib-lemans"

# Build target dependent libs
EXTRA_OECMAKE += "-DBUILD_CATEGORY=TARGET"
