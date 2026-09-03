CAPSULE_FLASH_TYPE:iq-x7181-evk = "NORUFS"
CAPSULE_ENTRIES:iq-x7181-evk    = "dtb"

# Hamoa keeps the Linux DTB FIT image in SPINOR, with dtb as the active
# partition and dtb_BACKUP a rollback copy written before it.
#
# These are varflags, and varflags take no part in override resolution --
# CAPSULE_ENTRY_dtb[dest_disk]:iq-x7181-evk does not exist. Assigned
# plainly they would apply everywhere, so any other board declaring a "dtb"
# entry would silently inherit these SPINOR destinations. Guard on
# MACHINEOVERRIDES to get the scope the override was meant to give.
QCOM_CAPSULE_DTB_ENTRY_MACHINE ?= "iq-x7181-evk"

python () {
    machine = d.getVar('QCOM_CAPSULE_DTB_ENTRY_MACHINE')
    if machine not in (d.getVar('MACHINEOVERRIDES') or '').split(':'):
        return

    for flag, value in (
        ('binary',           'dtb.bin'),
        ('dest_disk',        'SPINOR'),
        ('dest_partition',   'dtb'),
        ('dest_guid',        '{2A1A52FC-AA0B-401C-A808-5EA0F91068F8}'),
        ('backup_disk',      'SPINOR'),
        ('backup_partition', 'dtb_BACKUP'),
        ('backup_guid',      '{A166F11A-2B39-4FAA-B7E7-F8AA080D0587}'),
    ):
        d.setVarFlag('CAPSULE_ENTRY_dtb', flag, value)
}
