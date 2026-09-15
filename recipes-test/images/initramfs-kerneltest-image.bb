require initramfs-test-image.bb

PACKAGE_INSTALL += "${MACHINE_ESSENTIAL_EXTRA_RRECOMMENDS}"

python __anonymous () {
  meer = d.getVar('MACHINE_ESSENTIAL_EXTRA_RRECOMMENDS') or ''
  for p in meer.split():
    # exclude qairt-sdk-hexagon-vXX
    if 'qairt-sdk-hexagon-v' in p:
      d.setVar('PACKAGE_INSTALL:remove', p)
}
