SUMMARY = "Protocol Buffers - structured data serialisation mechanism, backport for CamX"
DESCRIPTION = "Protocol Buffers are a way of encoding structured data in an \
efficient yet extensible format. Google uses Protocol Buffers for almost \
all of its internal RPC protocols and file formats. This is the version backported \
for the use inside Qualcomm layer"

BBCLASSEXTEND = "native nativesdk"

# By default just pull in normal protobuf
DEPENDS = "protobuf"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

PACKAGES += "${PN}-compiler"
ALLOW_EMPTY:${PN}-compiler = "1"
RDEPENDS:${PN}-compiler = "protobuf-compiler"
