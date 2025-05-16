REMOVE_BOARDS = " \
    qcom-sa8775p-ride \
    qcom-qcs8300-ride \
    thundercomm-rb3gen2 \
"
REMOVE_PACKAGES = " \
    adsp \
    cdsp \
    gdsp \
"

python() {
    pkgs = d.getVar('PACKAGES').split()
    newpkgs = []
    for board in d.getVar('REMOVE_BOARDS').split():
        for pkg in d.getVar('REMOVE_PACKAGES').split():
            name = 'hexagon-dsp-binaries-%s-%s' % (board, pkg)
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
                files = d.getVar('INSANE_SKIP:%s' % name)
                d.setVar('INSANE_SKIP:%s' % x_name, files)
    newpkgs.extend(pkgs)
    d.setVar('PACKAGES', ' '.join(newpkgs))
}

