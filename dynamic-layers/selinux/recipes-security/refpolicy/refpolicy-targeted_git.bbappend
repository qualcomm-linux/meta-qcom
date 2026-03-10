FILESEXTRAPATHS:append := "${THISDIR}/${PN}:"

SRC_URI:append = " \
        file://0001-Enabled-ssh_sysadm_login-boolean-flag-to-address-ssh.patch \
        "
