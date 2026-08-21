# A/B slot selection

On platforms whose boot chain is `PBL -> XBL -> UEFI -> systemd-boot`, the
active slot is selected by XBL from the `OsTrialBootStatus` UEFI variable.
GPT slot attributes are not consulted: values written by `qbootctl` are
ignored by XBL, so they cannot switch slots on their own.

XBL reads the variable in `MountRequiredFatPartitions` and mounts the ESP of
the resulting slot:

    ActiveSlot 0 -> gEfiPartTypeSystemPartGuid   (standard ESP GUID)
    ActiveSlot 1 -> gEfiPartTypeSystemPartBGuid

If the variable is absent, XBL falls back to the primary ESP and returns before
the slot logic, so the mechanism stays dormant until something writes it.

## Partition requirement

The slot B ESP must carry the `SystemPartB` type GUID, not the standard ESP
GUID and not an arbitrary one:

| Slot | Partition type GUID                    |
| ---- | -------------------------------------- |
| A    | `C12A7328-F81F-11D2-BA4B-00A0C93EC93B` |
| B    | `C068C133-B430-4CD4-AC8A-BEFDC3794763` |

XBL locates slot B by that exact type GUID. With any other value slot B is
invisible even when `OsTrialBootStatus` selects it.

## Variable layout

`OsTrialBootStatus` (`gEfiGlobalVariableGuid`) is a `UINT64` bitfield:

| Bits  | Field              |
| ----- | ------------------ |
| 0-3   | version            |
| 8-11  | trial boot max     |
| 12-15 | trial boot count   |
| 16    | trial boot enabled |
| 17    | OS update type     |
| 18-19 | active slot        |
| 20    | is slot updated    |
| 21-23 | unbootable slot    |
| 24-26 | NHLOS state        |
| 27    | NHLOS status       |
| 28-30 | HLOS state         |
| 31    | HLOS status        |

Attributes are `NV+BS+RT` (`0x7`). Little-endian, so a persistent slot switch
with trial boot disabled is `01 03 02 00` for A and `01 03 06 00` for B.
Setting bit 16 arms the trial path: `01 03 03 00` for A, `01 03 07 00` for B.
The trial boot count is maintained by firmware and should not be written by the
OS.

On an unfused device the variable can be written directly:

    printf "\x01\x03\x07\x00" > active_b.bin
    efivar -w -n 8be4df61-93ca-11d2-aa0d-00e098032b8c-OsTrialBootStatus \
        -f active_b.bin

## Trial boot and rollback

With bit 16 clear no trial-boot bookkeeping runs, and a slot that fails to
mount simply falls back to the primary ESP without updating the variable. That
fallback resembles a rollback but records nothing.

With bit 16 set, XBL increments the trial boot count each boot. Once the count
reaches the maximum it sets the HLOS state to rollback, clears the count,
switches the active slot and reports the new slot. The rollback is visible to
the OS through the HLOS state and the is-slot-updated bit, which is how a
userspace updater learns that a rollback happened.

## Variable store

Where the variable lives depends on the fuse state:

- Before secure boot is fused, the UEFI variable store is a file on the primary
  ESP. Reads work through efivarfs without mounting anything, because the
  variable is runtime accessible. Writes require the primary ESP to be mounted,
  since the write persists back to that file.
- After fusing, the store moves to RPMB. Linux reaches it through the UEFI
  Secure Application over QSEECOM, which needs
  `CONFIG_QCOM_QSEECOM_UEFISECAPP` in the kernel. Writes then persist to RPMB
  with no mount.
