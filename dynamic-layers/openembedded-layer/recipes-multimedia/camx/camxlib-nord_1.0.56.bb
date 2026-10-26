PLATFORM = "nord"
PBT_BUILD_DATE = "261026"

require common.inc

SRC_URI[camxlib.sha256sum] = "61c1c768397b2099e411fb769bd8906ee47b79f0ce7c177e849a47bb693cf389"
SRC_URI[camx.sha256sum] = "e6e3fb01dadd5682067ce0f92e44b93fb0f40743a41def6d318ebf96bb1313ef"
SRC_URI[chicdk.sha256sum] = "f706e83e0c262b5753a0f6359b845630952c3f41bd72cbe4cbef2b3c2a942b06"
SRC_URI[camxcommon.sha256sum] = "82db078c319176d5ea929bfc6d12d4319ae32cf0eb3abbd8ad5be98106f001ea"
SRC_URI[camxtest.sha256sum] = "1947aa2bf066c84384f721eb1946d5d219a7856bc50463753edbede31cbf581a"

DEPENDS += "opencv userspace-resource-manager"
