REMOVE_SOCS = " \
    qcm6490 \
    qcs8300 \
    sa8775p \
"
REMOVE_PACKAGES = " \
    audio \
    compute \
    generalpurpose \
"

python() {
    pkgs = d.getVar('PACKAGES').split()
    newpkgs = []
    for soc in d.getVar('REMOVE_SOCS').split():
        for pkg in d.getVar('REMOVE_PACKAGES').split():
            name = 'linux-firmware-qcom-%s-%s' % (soc, pkg)
            x_name = 'x-' + name
            if name in pkgs:
                pkgs.remove(name)
                newpkgs.append(x_name)
                files = d.getVar('FILES:%s' % name, False)
                d.setVar('FILES:%s' % x_name, files)
                files = d.getVar('LICENSE:%s' % name)
                d.setVar('LICENSE:%s' % x_name, files)
                files = d.getVar('RDEPENDS:%s' % name)
                d.setVar('RDEPENDS:%s' % x_name, files)
    newpkgs.extend(pkgs)
    d.setVar('PACKAGES', ' '.join(newpkgs))
}

python populate_packages:append() {
    newpkgs = d.getVar('RRECOMMENDS:linux-firmware').split()
    newpkgs = filter(lambda x: not x.startswith('x-'), newpkgs)
    d.setVar('RRECOMMENDS:linux-firmware', ' '.join(newpkgs))
}
