require initramfs-test-image.bb

PACKAGE_INSTALL += "${MACHINE_ESSENTIAL_EXTRA_RRECOMMENDS}"

# exclude 'qairt-sdk-hexagon-vXX'
PACKAGE_INSTALL:remove = "${@" ".join([p for p in d.getVar('MACHINE_ESSENTIAL_EXTRA_RRECOMMENDS').split() if p.find('qairt-sdk-hexagon-v') != -1])}"
