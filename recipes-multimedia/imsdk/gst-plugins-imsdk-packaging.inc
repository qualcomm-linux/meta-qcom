# Extends the GStreamer packaging to handle IMSDK plugins.
# For plugins that provide runtime modules, all module .so files under
# <plugin>/modules/ are grouped into a separate per-plugin modules package.

require recipes-multimedia/gstreamer/gstreamer1.0-plugins-packaging.inc

PACKAGESPLITFUNCS =+ "split_imsdk_module_packages"

python split_imsdk_module_packages () {
    imsdk_root = d.expand('${libdir}/imsdk')

    # Qualcomm runtime modules for plugin
    do_split_packages(d, imsdk_root, r'([^/]+)/modules/[^/]+\.so', d.expand('${PN}-%s-modules'), 'IMSDK runtime modules for %s plugin', recursive=True, extra_depends='', match_path=True)
}

PACKAGES += "${PN}-configs"

FILES:${PN}-configs = "${sysconfdir}/configs/"
