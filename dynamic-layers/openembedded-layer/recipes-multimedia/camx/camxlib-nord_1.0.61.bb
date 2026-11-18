PLATFORM = "nord"
PBT_BUILD_DATE = "261113"

require common.inc

SRC_URI[camxlib.sha256sum] = "09e10fc94c7421aea72daab536f6d38cdd0ba7cb74779cbeaf6149bd31880c82"
SRC_URI[camx.sha256sum] = "b181095684c95175857315dcc56dcbe2bbc1aba693a025d165c4e2c506feb3b3"
SRC_URI[chicdk.sha256sum] = "b724bada6b22e6f3d9059e2be6dbbf4c9cddcc3e0514debf97b174ff089b762d"
SRC_URI[camxcommon.sha256sum] = "d8837b8edc415d06b50bf6340a2ab5699a6ef0c57a42b0529e3d1570a58411fc"
SRC_URI[camxtest.sha256sum] = "3a423f068c790bd20628e8b2dc55514ec441f549cdc926f323d813cb8860d4ac"

DEPENDS += "opencv userspace-resource-manager"
