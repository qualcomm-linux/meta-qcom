PLATFORM = "lemans"
PBT_BUILD_DATE = "260209.1"

require common.inc

SRC_URI[camxlib.sha256sum]     = "91ed3aa7d401d3656c6288f3023cfeca69934f0b43bf232549501442055354f3"
SRC_URI[camx.sha256sum]        = "9d7cd0163306d5653bb55138cebe157a2b05ff07d565f20095a40adc831b35ca"
SRC_URI[chicdk.sha256sum]      = "6902884b9c998d5deac0c36aa78669ed308b0797cf715429bec7630848e49102"
SRC_URI[camxcommon.sha256sum]  = "9f3bf3d5f5e0db9bc4c8b07d6fa7442e47aba654f1ebcde8e9edb74fce5a0820"

do_install:append() {
    install -d ${D}${sysconfdir}/camera/test/NHX/

    cp -r ${S}/etc/camera/test/NHX/*.json ${D}${sysconfdir}/camera/test/NHX/
}

RPROVIDES:${PN} = "camxlib-monaco"
PACKAGE_BEFORE_PN += "camx-nhx"

FILES:camx-nhx = "\
    ${bindir}/nhx.sh \
    ${sysconfdir}/camera/test/NHX/ \
"
