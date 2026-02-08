require camera-service.inc

DEPENDS += "camera-metadata gtest protobuf-native protobuf-c protobuf-c-native"

#Builds common libs used by client & server
EXTRA_OECMAKE += "-DBUILD_CATEGORY=COMMON"
