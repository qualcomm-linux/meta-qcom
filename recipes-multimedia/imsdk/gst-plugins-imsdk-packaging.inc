# Extends the GStreamer packaging to handle IMSDK plugins.
# For plugins that provide runtime modules, all module .so files under
# <plugin>/modules/ are grouped into a separate per-plugin modules package.

require recipes-multimedia/gstreamer/gstreamer1.0-plugins-packaging.inc

PACKAGESPLITFUNCS =+ "split_imsdk_module_packages"
PACKAGESPLITFUNCS += "set_imsdk_oss_metapkg_rdepends"

python split_imsdk_module_packages () {
    imsdk_root = d.expand('${libdir}/imsdk')

    # Qualcomm runtime modules for plugin
    do_split_packages(d, imsdk_root, r'([^/]+)/modules/[^/]+\.so', d.expand('${PN}-%s-modules'), 'IMSDK runtime modules for %s plugin', recursive=True, extra_depends='', match_path=True)
}

# List of plugin packages to exclude from OSS meta package
# Add package names (without version/architecture suffix) to this list
# Example: to exclude gst-plugins-imsdk-qtimlqnn, add 'qtimlqnn' to the list
IMSDK_OSS_METAPKG_EXCLUDE ?= "qtimlqnn qtimlsnpe qtiqmmfsrc qtismartvencbin"

python set_imsdk_oss_metapkg_rdepends () {
    import os
    import oe.utils

    # Go through all generated packages (excluding the main package and
    # the -meta package itself) and add them to the -meta package as RDEPENDS.
    # Also exclude packages specified in IMSDK_OSS_METAPKG_EXCLUDE.

    pn = d.getVar('PN')
    metapkg =  pn + '-oss-meta'
    d.setVar('ALLOW_EMPTY:' + metapkg, "1")
    d.setVar('FILES:' + metapkg, "")
    exclude = [ pn, pn + '-meta', pn + '-oss-meta' ]

    # Add user-defined plugin exclusions to the exclude list
    exclude_plugins = d.getVar('IMSDK_OSS_METAPKG_EXCLUDE') or ""
    if exclude_plugins:
        for plugin in exclude_plugins.split():
            # Add packages like gst-plugins-imsdk-<plugin>
            exclude.append(pn + '-' + plugin)

    metapkg_rdepends = []
    pkgdest = d.getVar('PKGDEST')
    for pkg in oe.utils.packages_filter_out_system(d):
        if pkg not in exclude and pkg not in metapkg_rdepends:
            # See if the package is empty by looking at the contents of its PKGDEST subdirectory.
            # If this subdirectory is empty, then the package is.
            # Empty packages do not get added to the meta package's RDEPENDS
            pkgdir = os.path.join(pkgdest, pkg)
            if os.path.exists(pkgdir):
                dir_contents = os.listdir(pkgdir) or []
            else:
                dir_contents = []
            is_empty = len(dir_contents) == 0
            if not is_empty:
                metapkg_rdepends.append(pkg)
    d.setVar('RDEPENDS:' + metapkg, ' '.join(metapkg_rdepends))
    d.setVar('DESCRIPTION:' + metapkg, pn + ' meta package')
}

PACKAGES += "${PN}-oss-meta"
