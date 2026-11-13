PLATFORM = "nord"
PBT_BUILD_DATE = "261104"

require common.inc

SRC_URI[camxlib.sha256sum] = "f3878d5a0df3dc427ca4fec9831bc3e2678370a54810d425983adbac9953ee5f"
SRC_URI[camx.sha256sum] = "588df7b52f22a6b4b3f3227bfab1389f662da72afc9e97d0e0ae8d4213e9130a"
SRC_URI[chicdk.sha256sum] = "f9e8f72aec1a0df64b0483e40729f9a2669522a83baaa39fde19f4d9b207815a"
SRC_URI[camxcommon.sha256sum] = "947d6399403e0d57c282aa0cb6653359a8c639414743cbcb89912f34b6574a63"
SRC_URI[camxtest.sha256sum] = "60141369a9fd74d3776a2efc89762e4e8dd8f4ec7fb7e4d515890316821f32e8"

DEPENDS += "opencv userspace-resource-manager"
