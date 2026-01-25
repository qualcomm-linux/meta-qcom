require camera-service.inc

DEPENDS += "camera-common"

# Build client libraries
EXTRA_OECMAKE += "-DBUILD_CATEGORY=CLIENT"
