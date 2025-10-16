
# while building, make refpolicy to pick selinux tools like: setfiles
# from recipe-sysroot sbin dir, not from host machine, in refpolicy make files
# 'tc_sbindir' configured to "/sbin" by default using weak assignment.
EXTRA_OEMAKE += "tc_sbindir=${STAGING_DIR_NATIVE}${base_sbindir_native}"

# while building, make refpolicy to pick selinux tools from recipe-sysroot usr/sbin
#dir, not from host machine, in refpolicy make files 'tc_sbindir' configured to
#"/usr/sbin" by default using weak assignment.
EXTRA_OEMAKE += "tc_usrsbindir=${STAGING_SBINDIR_NATIVE}"

# Overridden version of prepare_policy_store from upstream meta-selinux.
# The loop in the below function is checking for HLL_TYPE(.pp) files in
# case of both MONOLITHIC and MODULAR designs, HLL_TYPE(.pp) files will be
# generated only when MODULAR design is choosen. So, the POLICY_MONOLITHIC
# check, fixes the issue.
prepare_policy_store() {
    oe_runmake 'DESTDIR=${D}' 'prefix=${D}${prefix}' install
    POL_PRIORITY=100
    POL_SRC=${D}${datadir}/selinux/${POLICY_NAME}
    POL_STORE=${D}${localstatedir}/lib/selinux/${POLICY_NAME}
    POL_ACTIVE_MODS=${POL_STORE}/active/modules/${POL_PRIORITY}

    # Prepare to create policy store
    mkdir -p ${POL_STORE}
    mkdir -p ${POL_ACTIVE_MODS}

    # Get hll type from suffix on base policy module
    HLL_TYPE=$(echo ${POL_SRC}/base.* | awk -F . '{if (NF>1) {print $NF}}')
    HLL_BIN=${STAGING_DIR_NATIVE}${prefix}/libexec/selinux/hll/${HLL_TYPE}

    if [ "${POLICY_MONOLITHIC}" != "y" ]; then
        for i in ${POL_SRC}/*.${HLL_TYPE}; do
            MOD_NAME=$(basename $i | sed "s/\.${HLL_TYPE}$//")
            MOD_DIR=${POL_ACTIVE_MODS}/${MOD_NAME}
            mkdir -p ${MOD_DIR}
            echo -n "${HLL_TYPE}" > ${MOD_DIR}/lang_ext
            if ! bzip2 -t $i >/dev/null 2>&1; then
                ${HLL_BIN} $i | bzip2 --stdout > ${MOD_DIR}/cil
                bzip2 -f $i && mv -f $i.bz2 $i
            else
                bunzip2 --stdout $i | \
                    ${HLL_BIN} | \
                    bzip2 --stdout > ${MOD_DIR}/cil
            fi
            cp $i ${MOD_DIR}/hll
        done
    fi
}
