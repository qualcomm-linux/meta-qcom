PACKAGECONFIG:append = " ml messaging sample-apps"

EXTRA_OECMAKE:append = " -DCMAKE_INSTALL_CONFIGDIR=${sysconfdir}"
