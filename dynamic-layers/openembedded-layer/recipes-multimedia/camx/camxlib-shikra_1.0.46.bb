PLATFORM = "shikra"
PBT_BUILD_DATE = "260911"

require common.inc

SRC_URI[camxlib.sha256sum] = "e090945d93a9de0fb64470480a87cb63c8773caab9bbf17212b31605f7d6417b"
SRC_URI[camx.sha256sum] = "ef372d5cb3a3f5303d13799e3f866c3f882127fc7bf4d9d80e1d2e131410f7b1"
SRC_URI[chicdk.sha256sum] = "f1ad3e91ce9902bd1135bb36f626741dcae20d5cf0fbafd8e6c6cec2678afa33"
SRC_URI[camxcommon.sha256sum] = "9ce4b129b55c2d81b478e7666dc95377339ab3ffa83a47b2ff2bad9d861d2f97"
SRC_URI[camxtest.sha256sum] = "5cbb58e6d3a2cd7565c68a6a660272ba80951570b881c57d1d276ada1d016f33"

DEPENDS += "sensinghub qcom-sensors-binaries qmi-framework"
DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'opencl', 'virtual/libopencl1', '', d)}"
DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'opengl', 'virtual/egl virtual/libgles2', '', d)}"
