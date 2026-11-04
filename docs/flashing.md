# Flashing images

## Build QDL tool

QDL tool communicates with USB devices of VID:PID `05c6:9008` and uploads a
flash loader, which is then used for flashing images. Download and build QDL
by following the upstream build instructions:
[Build QDL](https://github.com/linux-msm/qdl#build).

As QDL tool requires raw USB access, to be able to run it as a non-root user
create an appropriate `udev` rule by following steps described in
[Update udev rules](https://docs.qualcomm.com/bundle/publicresource/topics/80-70014-254/flash_images_unregistered.html#update-udev-rules)

## Prepare the Board

### RB3 Gen 2

Location of all DIP switches, USB debug port and buttons (`F_DL` for instance)
can be found on [RB3 Gen 2 Quick Start Guide](https://docs.qualcomm.com/bundle/publicresource/topics/80-70014-253/ubuntu_host.html).

1. Set up `DIP_SW_0` positions `1` and `2` to `ON`. This enables serial output
   to the debug port.
2. To put the device into EDL mode press and hold the `F_DL` button
   before connecting the power cable.

## Flash images

Make sure that ModemManager is not running, disable it if necessary.

1. Connect the micro USB debug cable to the host. Baud rate should be `115200`.
   Check in `dmesg` how UART shows up (e.g. `/dev/ttyUSB0`):

   ```console
   $ sudo dmesg | grep tty
   [217664.921039] usb 3-1.1.4: FTDI Serial Device converter attached to ttyUSB0
   ```

2. Use your favorite serial communication program to access the console, such
   as minicom, picocom, putty etc. Baud rate should be 115200:

   ```bash
   picocom -b 115200 /dev/ttyUSB0
   ```

3. Plug in the USB-C cable from the host.

4. Use the QDL tool (built in the previous section) to flash the images:

   ```bash
   cd build/tmp/deploy/images/rb3gen2-core-kit/core-image-base-rb3gen2-core-kit.rootfs.qcomflash
   qdl --debug prog_firehose_ddr.elf rawprogram*.xml patch*.xml
   ```

   If you have multiple boards connected the host, provide the serial
   number of the board to flash through `--serial` param:

   ```bash
   qdl --serial=0AA94EFD --debug prog_firehose_ddr.elf rawprogram*.xml patch*.xml
   ```

   Serial can be obtained using `lsusb -v -d 05c6:9008` command.

5. Ensure that the device is booted in Emergency Download (EDL) mode
   (please refer to Quick Start Guide for your board). The process of
   flashing should start automatically:

   ```text
   USB: using out-chunk-size of 1048576
   HELLO version: 0x2 compatible: 0x1 max_len: 1024 mode: 0
   READ64 image: 13 offset: 0x0 length: 0x40
   ```

## Board variants

Some machines build one image for several boards that differ only in their
CDT, such as the RB3 Gen 2 core, vision and industrial kits. Each board listed
in `QCOM_FLASH_VARIANTS` gets a `variants/<board>` directory, and the bundle
root flashes the default board, the first one unless
`QCOM_FLASH_VARIANT_DEFAULT` says otherwise. The programmer and rawprogram/patch
XMLs of a board's directory flash the images from the bundle root together
with that board's CDT, so run the usual command from there. The programmer is
`prog_firehose_ddr.elf` unless the board sets another one:

```bash
cd core-image-base-rb3gen2-core-kit.rootfs.qcomflash/variants/vision-kit
qdl --debug prog_firehose_ddr.elf rawprogram*.xml patch*.xml
```

On builds with VIP enabled, each variant directory also has its own signed
digest table in `vip-tables`. The table covers the file paths qdl sends, so
run qdl from the variant directory with the XMLs named as shown:

```bash
qdl --vip-table-path=vip-tables prog_firehose_ddr.elf rawprogram*.xml patch*.xml
```
