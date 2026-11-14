PLATFORM = "nord"
PBT_BUILD_DATE = "261104"

require common.inc

SRC_URI[camxlib.sha256sum] = "20669a71e4d9e4df2e326451acf5aa82a5aaee8d60376c3f5e1605f76e24dfc0"
SRC_URI[camx.sha256sum] = "6c8f845b363076b771cbd59252cba4f140fe66d0dfbd15e92e07fd3db648ad83"
SRC_URI[chicdk.sha256sum] = "6cf9213d7b54f70ce0afc30da596a0a831bec7b00c531e3f56b3ce494e604603"
SRC_URI[camxcommon.sha256sum] = "9e96afa52e5b26cf98c581fcf9ceb586343c65c9990ff67de00a32d7413ed690"
SRC_URI[camxtest.sha256sum] = "83932362b17f6ca8a9a61247a40771eb95e6ff9ae21837a69586020b13a7ce35"

DEPENDS += "opencv userspace-resource-manager"
