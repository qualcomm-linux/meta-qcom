SUMMARY = "Qualcomm MinkIPC applications and library"
DESCRIPTION = " \
MINK ('Mink is Not a Kernel') is a capability-based security framework, \
which is a synchronous message passing facility based on the Object-Capability model, \
designed to facilitate secure communication between different domains. \
qteesupplicant service is designed for invocation dispatch and handling callbacks. \
"
HOMEPAGE = "https://github.com/qualcomm/minkipc.git"
SECTION = "devel"

LICENSE = "BSD-3-Clause & BSD-2-Clause & GPL-2.0-only"

inherit cmake systemd pkgconfig

SRC_URI = "git://github.com/qualcomm/minkipc.git;branch=main;protocol=https;tag=v${PV};name=minkipc \
           git://github.com/OP-TEE/optee_client.git;branch=master;protocol=https;tag=4.0.0;name=opteec;destsuffix=${BPN}-${PV}/optee-client/optee_client \
           git://github.com/OP-TEE/optee_test.git;branch=master;protocol=https;tag=4.0.0;name=opteet;destsuffix=${BPN}-${PV}/optee-test/optee_test \
           "

LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=2b1366ebba1ebd9ae25ad19626bbca93 \
                    file://optee-client/optee_client/LICENSE;md5=69663ab153298557a59c67a60a743e5b \
                    file://optee-test/optee_test/LICENSE.md;md5=daa2bcccc666345ab8940aab1315a4fa \
                    file://${COMMON_LICENSE_DIR}/BSD-2-Clause;md5=cb641bc04cda31daea161b1bc15da69f \
                    file://${COMMON_LICENSE_DIR}/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6"

SRCREV_minkipc = "8d711bd367c40f259d7988a02bcda73b65868750"
SRCREV_opteec = "acb0885c117e73cb6c5c9b1dd9054cb3f93507ee"
SRCREV_opteet = "1c3d6be5eaa6174e3dbabf60928d15628e39b994"

SRCREV_FORMAT = "minkipc_opteec_opteet"

DEPENDS += "qcbor qcomtee mink-idl-compiler-native glib-2.0"

EXTRA_OECMAKE = " \
    -DBUILD_UNITTEST=ON \
    -DMINKIDLC_BIN_DIR=${STAGING_BINDIR_NATIVE} \
    -DSYSTEMD_UNIT_DIR=${systemd_unitdir}/system \
    -DUDEV_DIR=${nonarch_libdir}/udev/rules.d \
    -DMINKIPC_LIBEXEC_DIR=${base_bindir} \
"

PACKAGE_BEFORE_PN += "${PN}-qteesupplicant"

SYSTEMD_PACKAGES = "${PN}-qteesupplicant"
SYSTEMD_SERVICE:${PN}-qteesupplicant = "qteesupplicant.service sfsconfig.service"

FILES:${PN}-qteesupplicant = "${bindir}/qtee_supplicant \
                              ${nonarch_libdir}/udev/rules.d/99-qcomtee-udev.rules \
                              ${base_bindir}/sfs_config \
"

RDEPENDS:${PN}-qteesupplicant = "${PN} mount-tee-partition"

