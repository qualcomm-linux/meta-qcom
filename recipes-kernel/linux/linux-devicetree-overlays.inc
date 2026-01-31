do_compile:append() {
        if [ -n "${KERNEL_DTC_FLAGS}" ]; then
                export DTC_FLAGS="${KERNEL_DTC_FLAGS}"
        fi

        for dtbof in ${KERNEL_DEVICETREE_OVERLAYS}; do
                dtbo=`normalize_dtb "$dtbof"`
                oe_runmake $dtbo CC="${KERNEL_CC} $cc_extra " LD="${KERNEL_LD}" OBJCOPY="${KERNEL_OBJCOPY}" STRIP="${KERNEL_STRIP}" ${KERNEL_EXTRA_ARGS}
        done
}
