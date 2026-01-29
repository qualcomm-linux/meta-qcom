SUMMARY = "OP-TEE Client API"
DESCRIPTION = "Open Portable Trusted Execution Environment - Normal World Client side of the TEE"
HOMEPAGE = "https://www.op-tee.org/"

LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=69663ab153298557a59c67a60a743e5b"

inherit systemd cmake pkgconfig

DEPENDS += "minkipc util-linux"
EXTRA_OEMAKE += "PKG_CONFIG=pkg-config"

#v4.0.0
SRCREV = "acb0885c117e73cb6c5c9b1dd9054cb3f93507ee"
SRC_URI = "git://github.com/OP-TEE/optee_client.git;branch=master;protocol=https \
           file://0002-libckteec-Link-ckteec-library-to-minkteec-for-Qualco.patch \
           file://0001-libckteec-Add-tee-trace-files-until-minkteec-exports.patch \
           "

UPSTREAM_CHECK_GITTAGREGEX = "^(?P<pver>\d+(\.\d+)+)$"

EXTRA_OECMAKE = " \
    -DCMAKE_POLICY_VERSION_MINIMUM=3.5 \
    -DBUILD_SHARED_LIBS=ON \
    -DCFG_USE_PKGCONFIG=ON \
"

EXTRA_OECMAKE:append:toolchain-clang = "-DCFG_WERROR=0"

# We only want to install and package the libckteec library
# so remove everything else. Also the correct tee_client_api.h
# is already provided by libminkteec, and so we don't need it
# from libteec.
do_install:append() {
    rm -rf ${D}${sbindir}
    rm ${D}${includedir}/tee_client_api.h
    rm ${D}${libdir}/libteeacl.so.*
    rm ${D}${libdir}/libseteec.so.*
    rm ${D}${libdir}/libteec.so.1.*
}

FILES:${PN}:remove = " \
    ${includedir}/tee_client_api.h \
    ${sbindir}/tee-supplicant \
    ${libdir}/libteeacl.so.* \
    ${libdir}/libseteec.so.* \
    ${libdir}/libteec.so.1.* \
"
