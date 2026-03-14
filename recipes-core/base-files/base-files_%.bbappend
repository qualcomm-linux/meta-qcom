# Make /var/tmp a persistent real directory, compliant with FHS 3.0:
# https://refspecs.linuxfoundation.org/FHS_3.0/fhs/ch05s15.html
#
# By default, OE-Core's base-files installs /var/tmp as a symlink to
# /var/volatile/tmp, which is backed by a RAM-based tmpfs and does not
# persist across reboots.  This violates FHS and breaks applications
# (e.g. qtee_supplicant) that store reboot-persistent state under /var/tmp.
#
# Removing fs-perms-volatile-tmp.txt causes base-files to create /var/tmp
# as a real directory with mode 1777 instead.  Systemd's 00-create-volatile.conf
# still has "L /var/tmp -> /var/volatile/tmp", but the "L" type (without "+")
# does not replace an existing non-symlink path, so no further override is needed.
FILESYSTEM_PERMS_TABLES:remove:qcom = "files/fs-perms-volatile-tmp.txt"
