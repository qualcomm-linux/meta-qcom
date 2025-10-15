
# while building, make refpolicy to pick selinux tools like: setfiles
# from recipe-sysroot sbin dir, not from host machine, in refpolicy make files
# 'tc_sbindir' configured to "/sbin" by default using weak assignment.
EXTRA_OEMAKE += "tc_sbindir=${STAGING_DIR_NATIVE}${base_sbindir_native}"

# while building, make refpolicy to pick selinux tools from recipe-sysroot usr/sbin
#dir, not from host machine, in refpolicy make files 'tc_sbindir' configured to
#"/usr/sbin" by default using weak assignment.
EXTRA_OEMAKE += "tc_usrsbindir=${STAGING_SBINDIR_NATIVE}"
