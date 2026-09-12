SUMMARY = "Qualcomm AI Runtime SDK"
DESCRIPTION = " \
Qualcomm's AI Runtime SDK provides the tools, runtimes, and model‑execution engines needed \
to run Machine Learning models efficiently on Qualcomm devices. \
It integrates the components to enable efficient inference on CPU, GPU, and NPU/HTP accelerators, \
while also supporting the development of ML applications. \
"
HOMEPAGE = "https://docs.qualcomm.com/doc/80-63442-10/topic/general_overview.html"

LICENSE = "LicenseRef-qcom-ai-stack"
LIC_FILES_CHKSUM = "file://LICENSE.pdf;md5=878b885995f453e328edbcd5a1302306"
NO_GENERIC_LICENSE[qcom-ai-stack] = "LICENSE.pdf"

# The zip file is quite large ~2.4 GB
# Tt's better to increase the default tries from 2 and timeout from 100.
FETCHCMD_wget = "wget --tries=5 --timeout=1000"

SRC_URI = "https://softwarecenter.qualcomm.com/api/download/software/sdks/Qualcomm_AI_Runtime_Community/All/${PV}/v${PV}.zip"
SRC_URI[sha256sum] = "a346ea0e2c8631b46d57261a4969994cd9cc34124a8355bbc7b08b2c8bd859a5"

S = "${UNPACKDIR}/qairt/${PV}"

# libGenieService.so has a hard DT_NEEDED on libsystemd.so.0 and cannot
# function without it. Restrict the recipe to systemd-based distros.
inherit features_check
REQUIRED_DISTRO_FEATURES = "systemd"

# We need host tools during the build:
#  - patchelf-native: to patch DT_NEEDED entries in prebuilt .so files
#  - binutils-native: for readelf to inspect NEEDED
#  - systemd: for libsystemd.so.0 dependency of libGenieService.so
DEPENDS = "patchelf-native binutils-native systemd"

# The SDK ships multiple toolchain-specific lib directories with names
# like "aarch64-oe-linux-gcc8.2", "aarch64-oe-linux-gcc9.3", etc.
# This helper picks the directory whose GCC major version best matches the
# build compiler, falling back to the nearest lower available version.
def platform_dir(d):
    sdk_lib_dir = d.getVar("S", True) + "/lib/"
    if os.path.exists(sdk_lib_dir) and os.path.isdir(sdk_lib_dir):
        dir_prefix = "aarch64-oe-linux-gcc"
        gccversion = d.getVar("GCCVERSION", True).strip('%').split('.')[0]
        gccversion = int(gccversion)
        MIN_GCC_VERSION = 8

        for version in range (gccversion, MIN_GCC_VERSION - 1, -1):
            version = str(version)
            pf_dir = dir_prefix + version
            for folder in os.listdir(sdk_lib_dir):
                if folder.startswith(pf_dir):
                    pf_dir += "*"
                    return pf_dir

PLATFORM_DIR = "${@platform_dir(d)}"

do_compile[noexec] = "1"

# We currently install and test it only on ARMv8 (aarch64) machines.
# PLATFORM_DIR resolution also assumes aarch64.
# Therefore, builds for other architectures are excluded for now.
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:aarch64 = "(.*)"

do_install() {
    install -d ${D}${includedir}
    install -d ${D}${libdir}
    install -d ${D}${datadir}/qcom/qcs615/Qualcomm/QCS615-RIDE/dsp/cdsp
    install -d ${D}${datadir}/qcom/qcm6490/Thundercomm/RB3gen2/dsp/cdsp
    install -d ${D}${datadir}/qcom/sa8775p/Qualcomm/SA8775P-RIDE/dsp/cdsp
    install -d ${D}${datadir}/qcom/sa8775p/Qualcomm/SA8775P-RIDE/dsp/cdsp1
    install -d ${D}${datadir}/qcom/qcs8300/Qualcomm/QCS8300-RIDE/dsp/cdsp
    install -d ${D}${datadir}/qcom/x1e80100/Qualcomm/Hamoa-IoT-EVK/dsp/cdsp
    install -d ${D}${datadir}/qcom/shikra/Qualcomm/Shikra-CQS-EVK/dsp/cdsp
    install -d ${D}${bindir}

    cp -r ${S}/include/* ${D}${includedir}
    cp -r ${S}/lib/${PLATFORM_DIR}/* ${D}${libdir}

    # These installation paths for the Hexagon libraries were decided based on
    # the recommendations from FastRPC team and taking FastCV as a reference.
    # They may change later, so keep the PACKAGES entries generic.
    cp -r ${S}/lib/hexagon-v66/unsigned/* ${D}${datadir}/qcom/qcs615/Qualcomm/QCS615-RIDE/dsp/cdsp
    cp -r ${S}/lib/hexagon-v68/unsigned/* ${D}${datadir}/qcom/qcm6490/Thundercomm/RB3gen2/dsp/cdsp
    cp -r ${S}/lib/hexagon-v73/unsigned/* ${D}${datadir}/qcom/sa8775p/Qualcomm/SA8775P-RIDE/dsp/cdsp
    cp -r ${S}/lib/hexagon-v75/unsigned/* ${D}${datadir}/qcom/qcs8300/Qualcomm/QCS8300-RIDE/dsp/cdsp

    # Shikra CQS-EVK uses the same v66 binaries as QCS615-RIDE.
    for lib in ${D}${datadir}/qcom/qcs615/Qualcomm/QCS615-RIDE/dsp/cdsp/*; do \
        ln -s ${datadir}/qcom/qcs615/Qualcomm/QCS615-RIDE/dsp/cdsp/$(basename $lib) \
        ${D}${datadir}/qcom/shikra/Qualcomm/Shikra-CQS-EVK/dsp/cdsp/$(basename $lib); \
    done

    # Hamoa IoT EVK uses the same v73 binaries as SA8775P-RIDE.
    for lib in ${D}${datadir}/qcom/sa8775p/Qualcomm/SA8775P-RIDE/dsp/cdsp/*; do \
        ln -s ${datadir}/qcom/sa8775p/Qualcomm/SA8775P-RIDE/dsp/cdsp/$(basename $lib) \
        ${D}${datadir}/qcom/x1e80100/Qualcomm/Hamoa-IoT-EVK/dsp/cdsp/$(basename $lib); \
    done

    # SA8775P-RIDE cdsp1 is an alias for cdsp used by some FastRPC clients.
    for lib in ${D}${datadir}/qcom/sa8775p/Qualcomm/SA8775P-RIDE/dsp/cdsp/*; do \
        ln -s ../cdsp/$(basename $lib) \
        ${D}${datadir}/qcom/sa8775p/Qualcomm/SA8775P-RIDE/dsp/cdsp1/$(basename $lib); \
    done

    cp -r ${S}/bin/${PLATFORM_DIR}/* ${D}${bindir}
}

# Some shared libraries depend on the unversioned 'libcdsprpc.so',
# while the provider (fastrpc) exports the versioned SONAME 'libcdsprpc.so.1'.
# Rewrite DT_NEEDED from libcdsprpc.so to libcdsprpc.so.1 for affected libraries
# to avoid packaging/runtime dependency mismatches.
#
# Can be dropped once fixed upstream (planned in QAIRT SDK v2.45)
do_patch_qairt_needed_soname() {
    for so in ${D}${libdir}/lib*.so; do
        if readelf -d "$so" 2>/dev/null | grep -q "NEEDED.*libcdsprpc\.so"; then
            patchelf --replace-needed libcdsprpc.so "libcdsprpc.so.1" "$so"
        fi
    done
}

addtask patch_qairt_needed_soname after do_install before do_package

# SDK ships already-stripped proprietary binaries
# which need not be re-striped or split into debug symbols.
# So, disable strip, debug split and mute 'already-stripped' warnings.
INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INSANE_SKIP:${PN} += "already-stripped"

# The SDK ships unversioned .so files, so keep them in the main package
# and treat plain .so as the library (avoid -dev splitting).
SOLIBS = ".so"
FILES_SOLIBSDEV = ""

PACKAGES += "\
    ${PN}-hexagon-v66 \
    ${PN}-hexagon-v68 \
    ${PN}-hexagon-v73 \
    ${PN}-hexagon-v75 \
"

FILES:${PN}-hexagon-v66 += "${datadir}/qcom/qcs615/Qualcomm/QCS615-RIDE/dsp/cdsp"
FILES:${PN}-hexagon-v66 += "${datadir}/qcom/shikra/Qualcomm/Shikra-CQS-EVK/dsp/cdsp"
FILES:${PN}-hexagon-v68 += "${datadir}/qcom/qcm6490/Thundercomm/RB3gen2/dsp/cdsp"
FILES:${PN}-hexagon-v73 += "${datadir}/qcom/sa8775p/Qualcomm/SA8775P-RIDE/dsp/cdsp"
FILES:${PN}-hexagon-v73 += "${datadir}/qcom/sa8775p/Qualcomm/SA8775P-RIDE/dsp/cdsp1"
FILES:${PN}-hexagon-v73 += "${datadir}/qcom/x1e80100/Qualcomm/Hamoa-IoT-EVK/dsp/cdsp"
FILES:${PN}-hexagon-v75 += "${datadir}/qcom/qcs8300/Qualcomm/QCS8300-RIDE/dsp/cdsp"

RDEPENDS:${PN} += "fastrpc"
RRECOMMENDS:${PN} += "${PN}-hexagon-v66 ${PN}-hexagon-v68 ${PN}-hexagon-v73 ${PN}-hexagon-v75"

# File based runtime depends are not applicable for Hexagon libraries.
SKIP_FILEDEPS:${PN}-hexagon-v66 = "1"
SKIP_FILEDEPS:${PN}-hexagon-v68 = "1"
SKIP_FILEDEPS:${PN}-hexagon-v73 = "1"
SKIP_FILEDEPS:${PN}-hexagon-v75 = "1"

# Skip QA checks that don’t apply to prebuilt Hexagon DSP/HTP libraries.
INSANE_SKIP:${PN}-hexagon-v66 += "arch libdir ldflags file-rdeps"
INSANE_SKIP:${PN}-hexagon-v68 += "arch libdir ldflags file-rdeps"
INSANE_SKIP:${PN}-hexagon-v73 += "arch libdir ldflags file-rdeps"
INSANE_SKIP:${PN}-hexagon-v75 += "arch libdir ldflags file-rdeps"

# Hexagon libraries include .so symlinks but are runtime artifacts.
INSANE_SKIP:${PN}-hexagon-v66 += "dev-so"
INSANE_SKIP:${PN}-hexagon-v73 += "dev-so"
