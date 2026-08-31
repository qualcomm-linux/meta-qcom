CAPSULE_FLASH_TYPE:iq-x7181-evk = "NORUFS"
CAPSULE_ENTRIES:iq-x7181-evk    = "dtb"

# Hamoa stores the Linux DTB FIT image in SPINOR.  The firmware uses a
# main/backup model: dtb is always the active partition; dtb_BACKUP holds a
# rollback copy that is overwritten by <Backup> before dtb is updated.
CAPSULE_ENTRY_dtb[binary]           = "dtb.bin"
CAPSULE_ENTRY_dtb[dest_disk]        = "SPINOR"
CAPSULE_ENTRY_dtb[dest_partition]   = "dtb"
CAPSULE_ENTRY_dtb[dest_guid]        = "{2A1A52FC-AA0B-401C-A808-5EA0F91068F8}"
CAPSULE_ENTRY_dtb[backup_disk]      = "SPINOR"
CAPSULE_ENTRY_dtb[backup_partition] = "dtb_BACKUP"
CAPSULE_ENTRY_dtb[backup_guid]      = "{A166F11A-2B39-4FAA-B7E7-F8AA080D0587}"
