PLATFORM = "talos"
PBT_BUILD_DATE = "260708"

require common.inc

SRC_URI[camxlib.sha256sum] = "eb95b7b3f733e189cd08df674d63604f7a1961c3985d537b7ec74eda48fcaa57"
SRC_URI[camx.sha256sum] = "391d247d2350b48febe598cbf0cf63e4b70fe30c9a6a827def3eedf2ddaa2fdb"
SRC_URI[chicdk.sha256sum] = "094e78c486d5c199590a7b74b264d5d2363f1c7383b14aba031294b3bc18db2e"
SRC_URI[camxcommon.sha256sum] = "50771e2bc3c68c861b631001d9d2bcbc2b6af997fdd16b9ae84226232bd40982"

DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'opencl', 'virtual/libopencl1', '', d)}"
DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'opengl', 'virtual/egl virtual/libgles2', '', d)}"

do_install:append() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'opengl opencl', 'false', 'true', d)}; then
        rm -f ${D}${libdir}/camx/${PLATFORM}/camera/components/libiwarp*
        rm -f ${D}${libdir}/camx/${PLATFORM}/camera/components/libhidrx*
    fi
}
