PLATFORM = "lemans"
PBT_BUILD_DATE = "260203"

require common.inc

SRC_URI[camxlib.sha256sum]     = "2e6a20530de17ffc506d20ca69149a02a2fd50c18990b3646f42c32f6fe42a7d"
SRC_URI[camx.sha256sum]        = "38de86b36cfe58a37273e95e4ccea7d3243226f439fc79f7abd7a9103f7bee34"
SRC_URI[chicdk.sha256sum]      = "91bb40b2fdc3e9816d99956efb2746b50f064e3b51661247bb28fc2d32045ef0"
SRC_URI[camxcommon.sha256sum]  = "891c78465fb5ffb61a9dd48ba694b18c2523065a87749d1c173bba1cf663c46c"

do_install:append() {
    install -d ${D}${sysconfdir}/camera/test/NHX/

    cp -r ${S}/etc/camera/test/NHX/NHX.YUV_NV12_Prev_MaxRes.json \
        ${D}${sysconfdir}/camera/test/NHX/
}

RPROVIDES:${PN} = "camxlib-monaco"
PACKAGE_BEFORE_PN += "camx-nhx"

FILES:camx-nhx = "\
    ${bindir}/nhx.sh \
    ${sysconfdir}/camera/test/NHX/ \
"
