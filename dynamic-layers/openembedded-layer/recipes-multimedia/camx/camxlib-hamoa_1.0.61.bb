PLATFORM = "hamoa"
PBT_BUILD_DATE = "261113"

require common.inc

SRC_URI[camxlib.sha256sum] = "5a561ecd813aed456417167c2dc03eaf82a3cd5b3e252f3e8fe40863ab71f15d"
SRC_URI[camx.sha256sum] = "df35a5691af4c7c4a1b939c3a61749990998dd7202b614325077bf3603d560b8"
SRC_URI[chicdk.sha256sum] = "c2d6b28e0202a59a66ae873c31ded7e7dc35b6c1ebbfbb1f6453ba08781912ad"
SRC_URI[camxcommon.sha256sum] = "2e6d3adcc1e24a5e755f17952ebf1abe7bb195eee59b564a443fce3b5b7263b2"
SRC_URI[camxtest.sha256sum] = "79eae0fc8f3f8a35e5682de37a1c669ad3b4a0afb84a1f9bac38563654c6dbc5"

DEPENDS += "opencv userspace-resource-manager"

CAMXLIB_SKEL_SOCS = "x1e80100"
require camxlib-skel.inc
