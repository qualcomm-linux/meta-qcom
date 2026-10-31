PLATFORM = "nord"
PBT_BUILD_DATE = "261026"

require common.inc

SRC_URI[camxlib.sha256sum] = "25d8fb0ce63cf934cba7f2944196eab96e00cf92ad8ac33701de6f29662d1efb"
SRC_URI[camx.sha256sum] = "d9365e3d93d9e4439d44e37b2d2121b8960a48d71207758599c4c4c43377c78f"
SRC_URI[chicdk.sha256sum] = "99a37dc09ccec8fefae3702d5deb0258c3af8f9a00e1e5655721ba60d59b32c4"
SRC_URI[camxcommon.sha256sum] = "0ebee7374c9d2275aa6346b3eac2794d8619968d95471ba85ae72b40530a49d7"
SRC_URI[camxtest.sha256sum] = "757f612ee964d702aba774537b8af037a2f51c9c29a8bc03ae1e99071f3feab6"

DEPENDS += "opencv userspace-resource-manager"
