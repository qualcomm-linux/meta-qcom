SUMMARY = "Prebuilt Qualcomm sensors libraries and test applications"
DESCRIPTION = "Prebuilt core binaries required for sensor enablement and hardware \
sensor data access. These prebuilt binaries also include test applications to \
validate sensor services functionality through the Sensinghub Interface."
LICENSE = "LICENSE.qcom-2"
LIC_FILES_CHKSUM = "file://LICENSE.qcom-2;md5=f33ba334514c4dfabc6ab7377babb377"

PBT_BUILD_DATE = "260514.1"
SRC_URI = "https://qartifactory-edge.qualcomm.com/artifactory/qsc_releases/software/chip/component/sensors.lnx.0.0/${PBT_BUILD_DATE}/prebuilt_yocto/qcom-sensors-prebuilts_${PV}_armv8a.tar.gz"
SRC_URI[sha256sum] = "507652592b326bfeb1b31c4c37f61a5173439bd3dffa7ded72b675f834ea11bb"

S = "${UNPACKDIR}"

inherit systemd

# This package is currently only used and tested on ARMv8 (aarch64) machines.
# Therefore, builds for other architectures are not necessary and are explicitly excluded.
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:aarch64 = "(.*)"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}${bindir}
    install -d ${D}${libdir}
    install -d ${D}${libdir}/pkgconfig
    install -d ${D}${includedir}
    install -d ${D}${sysconfdir}/sensors/config
    install -d ${D}${sysconfdir}/sensors/registry
    install -d ${D}${systemd_system_unitdir}

    # Install binaries
    install -m 0755 ${S}/usr/bin/* ${D}${bindir}/

    # Install library
    oe_libinstall -C ${S}/usr/lib -so libsensinghubapiprop ${D}${libdir}
    oe_libinstall -C ${S}/usr/lib -so libQshQmiIDL ${D}${libdir}
    oe_libinstall -C ${S}/usr/lib -so libQshSession ${D}${libdir}
    oe_libinstall -C ${S}/usr/lib -so libsnsdiaglog ${D}${libdir}
    oe_libinstall -C ${S}/usr/lib -so libUSTANative ${D}${libdir}
    oe_libinstall -C ${S}/usr/lib -so libSEESalt ${D}${libdir}
    oe_libinstall -C ${S}/usr/lib -so libsns_direct_channel_stub ${D}${libdir}
    oe_libinstall -C ${S}/usr/lib -so libsns_remote_proc_state_stub ${D}${libdir}

    # Install registry and Service files
    install -m 0644 ${S}/etc/sensors/sns_reg_config ${D}${sysconfdir}/sensors/
    install -m 0644 ${S}/etc/sensors/config/* ${D}${sysconfdir}/sensors/config/
    install -m 0644 ${S}/etc/sensors/registry/sns_reg_version ${D}${sysconfdir}/sensors/registry/
    install -m 0644 ${S}${systemd_system_unitdir}/sscrpcd.service ${D}${systemd_system_unitdir}/sscrpcd.service

    # Install pkgconfig
    install -m 0644 ${S}/usr/lib/pkgconfig/*.pc ${D}${libdir}/pkgconfig/

    # Install headers
    install -m 0644 ${S}/usr/include/*.h ${D}${includedir}/
}

SYSTEMD_SERVICE:${PN} = "sscrpcd.service"

RDEPENDS:${PN} += " \
    fastrpc \
    glib-2.0 \
    libabsl-base \
    libabsl-borrowed-fixup-buffer \
    libabsl-city \
    libabsl-civil-time \
    libabsl-cord \
    libabsl-cord-internal \
    libabsl-cordz-functions \
    libabsl-cordz-handle \
    libabsl-cordz-info \
    libabsl-crc-cord-state \
    libabsl-crc-cpu-detect \
    libabsl-crc-internal \
    libabsl-crc32c \
    libabsl-debugging-internal \
    libabsl-decode-rust-punycode \
    libabsl-demangle-internal \
    libabsl-demangle-rust \
    libabsl-die-if-null \
    libabsl-examine-stack \
    libabsl-exponential-biased \
    libabsl-flags-commandlineflag \
    libabsl-flags-commandlineflag-internal \
    libabsl-flags-config \
    libabsl-flags-internal \
    libabsl-flags-marshalling \
    libabsl-flags-private-handle-accessor \
    libabsl-flags-program-name \
    libabsl-flags-reflection \
    libabsl-graphcycles-internal \
    libabsl-hash \
    libabsl-hashtablez-sampler \
    libabsl-int128 \
    libabsl-kernel-timeout-internal \
    libabsl-leak-check \
    libabsl-log-entry \
    libabsl-log-globals \
    libabsl-log-initialize \
    libabsl-log-internal-check-op \
    libabsl-log-internal-conditions \
    libabsl-log-internal-fnmatch \
    libabsl-log-internal-format \
    libabsl-log-internal-globals \
    libabsl-log-internal-log-sink-set \
    libabsl-log-internal-message \
    libabsl-log-internal-nullguard \
    libabsl-log-internal-proto \
    libabsl-log-internal-structured-proto \
    libabsl-log-severity \
    libabsl-log-sink \
    libabsl-malloc-internal \
    libabsl-random-distributions \
    libabsl-random-internal-entropy-pool \
    libabsl-random-internal-platform \
    libabsl-random-internal-randen \
    libabsl-random-internal-randen-hwaes \
    libabsl-random-internal-randen-hwaes-impl \
    libabsl-random-internal-randen-slow \
    libabsl-random-internal-seed-material \
    libabsl-random-seed-gen-exception \
    libabsl-random-seed-sequences \
    libabsl-raw-hash-set \
    libabsl-raw-logging-internal \
    libabsl-spinlock-wait \
    libabsl-stacktrace \
    libabsl-status \
    libabsl-statusor \
    libabsl-str-format-internal \
    libabsl-strerror \
    libabsl-strings \
    libabsl-strings-internal \
    libabsl-symbolize \
    libabsl-synchronization \
    libabsl-throw-delegate \
    libabsl-time \
    libabsl-time-zone \
    libabsl-tracing-internal \
    libabsl-utf8-for-code-point \
    libabsl-vlog-config-internal \
    libdiag \
    protobuf \
    qmi-framework \
    sensinghub \
"
