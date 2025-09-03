# meta-qcom

[![Build on push](https://img.shields.io/github/actions/workflow/status/qualcomm-linux/meta-qcom/push.yml?label=Build%20on%20push)](https://github.com/qualcomm-linux/meta-qcom/actions/workflows/push.yml)
[![Nightly Build](https://img.shields.io/github/actions/workflow/status/qualcomm-linux/meta-qcom/nightly-build.yml?label=Nightly%20Build)](https://github.com/qualcomm-linux/meta-qcom/actions/workflows/nightly-build.yml)

## Introduction

OpenEmbedded/Yocto Project hardware enablement layer for Qualcomm based platforms.

This layer provides additional recipes and machine configuration files for
Qualcomm platforms.

This layer depends on:

```
URI: https://github.com/openembedded/openembedded-core.git
layers: meta
branch: master
revision: HEAD
```

This layer has an optional dependency on meta-oe layer:

```
URI: https://github.com/openembedded/meta-openembedded.git
layers: meta-oe
branch: master
revision: HEAD
```

The dependency is optional, and not strictly required. When meta-oe is enabled
in the build (e.g. it is used in BBLAYERS) then additional recipes from
meta-qcom are added to the metadata. You can refer to meta-qcom/conf/layer.conf
for the implementation details.

## Branches

- **master:** Primary development branch, with focus on upstream support and
  compatibility with the most recent Yocto Project release.
- **all stable branch up until styhead:** Legacy branches maintained by Linaro,
  prior to the migration to [Qualcomm-linux](https://github.com/qualcomm-linux).
- **next:** For testing workflow changes before being merged to master.

## Machine Support

See `conf/machine` for the complete list of supported devices.

## Generic machine support

All contemporary boards are supported by a single qcom-armv8a machine. It can be
used instead of using the per-board configuration file. In order to enable
support for the particular device extend the qcom-armv8a.conf file .

## Quick build

Please refer to the [Yocto Project Reference Manual](https://docs.yoctoproject.org/ref-manual/system-requirements.html)
to set up your Yocto Project build environment.

Please follow the instructions below for a KAS-based build. The KAS tool offers
an easy way to setup bitbake based projects. For more details, visit the
[KAS documentation](https://kas.readthedocs.io/en/latest/index.html).

1. Install kas tool

    ```
    sudo pip3 install kas
    ```

2. Clone meta-qcom layer

    ```
    git clone https://github.com/qualcomm-linux/meta-qcom.git -b master
    ```

3. Build using the KAS configuration for one of the supported boards

    ```
    kas build meta-qcom/ci/rb3gen2-core-kit.yml
    ```

For a manual build without KAS, refer to the [Yocto Project Quick Build](https://docs.yoctoproject.org/brief-yoctoprojectqs/index.html).

## Flash

### Build QDL tool

QDL tool communicates with USB devices of PID:VID `05c6:9008` and uploads a
flash loader, which is then used for flashing images. Follow the steps below
to download and compile QDL for your platform:

1. Clone the QDL repository:

   ```
   git clone https://github.com/linux-msm/qdl
   ```

2. Read the README and install build dependencies (`libxml2-dev` and
   `libusb-1.0-0-dev`). On Debian-based distribution run:

   ```
   sudo apt install libxml2-dev libusb-1.0-0-dev
   ```

3. Build the QDL tool using make:

   ```
   cd qdl
   make
   ```

As QDL tool requires raw USB access, so to able to run it from non-root user
create an appropriate `udev` rule by following steps described in
[Update udev rules](https://docs.qualcomm.com/bundle/publicresource/topics/80-70014-254/flash_images_unregistered.html#update-udev-rules)

### Prepare the Board

#### RB3 Gen 2

Location of all DIP switches, USB debug port and buttons (`F_DL` for instance)
can be found on [RB3 Gen 2 Quick Start Guide](https://docs.qualcomm.com/bundle/publicresource/topics/80-70014-253/ubuntu_host.html).

1. Set up `DIP_SW_0` positions `1` and `2` to `ON`. This enables serial output
   to the debug port.
2. To put the device into EDL mode press and hold the `F_DL` button
   before connecting the power cable.

### Flash images

Make sure that ModemManager is not running, disable it if necessary.

1. Connect the micro USB debug cable to the host. Baud rate should be `115200`.
   Check in `dmesg` how UART shows up (e.g. `/dev/ttyUSB0`):

   ```
   $ sudo dmesg | grep tty
   [217664.921039] usb 3-1.1.4: FTDI Serial Device converter attached to ttyUSB0
   ```

2. Use your favorite serial comminication program to access the console, such
   as minicom, picocom, putty etc. Baud rate should be 115200:

   ```
   picocom -b 115200 /dev/ttyUSB0
   ```

3. Plug in the USB-C cable from the host.

4. Use the QDL tool (built in the previous section) to flash the images:

   ```
   cd build/tmp/deploy/images/rb3gen2-core-kit/core-image-base-rb3gen2-core-kit.rootfs.qcomflash
   qdl --debug prog_firehose_ddr.elf rawprogram*.xml patch*.xml
   ```

   If you have multiple boards connected the host, provide the serial
   number of the board to flash through `--serial` param:

   ```
   qdl --serial=0AA94EFD --debug prog_firehose_ddr.elf rawprogram*.xml patch*.xml
   ```

   Serial can be obtained using `lsusb -v -d 05c6:9008` command.

5. Ensure that the device is booted in Emergency Download (EDL) mode
   (please refer to Quick Start Guide for your board). The process of
   flashing should start automatically:

   ```
   USB: using out-chunk-size of 1048576
   HELLO version: 0x2 compatible: 0x1 max_len: 1024 mode: 0
   READ64 image: 13 offset: 0x0 length: 0x40
   ```

## Contributing

Please submit any patches against the `meta-qcom` layer (branch **master**)
by using the GitHub pull-request feature. Fork the repo, create a branch,
do the work, rebase from upstream, and create the pull request.

For some useful guidelines when submitting patches, please refer to:
[Preparing Changes for Submission](https://docs.yoctoproject.org/dev/contributor-guide/submit-changes.html#preparing-changes-for-submission)

Pull requests will be discussed within the GitHub pull-request infrastructure.

Branch **kirkstone** is not open for direct contributions, please raise an issue
with the suggested change instead.

### Qualcomm Internal

Please make sure to visit go/GitHubBasicsDoc and go/OSSBestPractices before proposing changes.

## Communication

- **GitHub Issues:** [meta-qcom issues](https://github.com/qualcomm-linux/meta-qcom/issues)
- **Pull Requests:** [meta-qcom pull requests](https://github.com/qualcomm-linux/meta-qcom/pulls)

## Maintainer(s)

* Anuj Mittal <anuj.mittal@oss.qualcomm.com>
* Dmitry Baryshkov <dmitry.baryshkov@oss.qualcomm.com>
* Koen Kooi <koen.kooi@oss.qualcomm.com>
* Nicolas Dechesne <nicolas.dechesne@oss.qualcomm.com>
* Ricardo Salveti <ricardo.salveti@oss.qualcomm.com>
* Sourabh Banerjee <sbanerje@qti.qualcomm.com>
* Viswanath Kraleti <viswanath.kraleti@oss.qualcomm.com>

## License

This layer is licensed under the MIT license. Check out [COPYING.MIT](COPYING.MIT)
for more details.
