# meta-qcom

![latest build](https://github.com/qualcomm-linux/meta-qcom/actions/workflows/push.yml/badge.svg)
![daily builds](https://github.com/qualcomm-linux/meta-qcom/actions/workflows/daily.yml/badge.svg)

## Introduction

OpenEmbedded/Yocto Project layer for Qualcomm based platforms.

This layer depends on:

```
URI: https://github.com/openembedded/openembedded-core.git
layers: meta
branch: master
revision: HEAD
```

This layers has an optional dependency on meta-oe layer:

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

## Device support

All contemporary boards are supported by a single qcom-armv8a machine. Please
use it instead of using the per-board configuration file. In order to enable
support for the particular device extend the qcom-armv8a.conf file .


## Quick build

If you're new to the Yocto Project, you might want to read the ![Yocto Project
Quick Build](https://docs.yoctoproject.org/brief-yoctoprojectqs/index.html) 
document in order to setup your Yocto Project build environment. 

From inside ```poky``` folder, clone this repository:

```
git clone git://git.yoctoproject.org/meta-qcom
```

Now, from inside ```poky``` folder initialize your build folder:

```
source oe-init-build-env build/qcom-armv8a
```

The script will cd into the newly created ```build/qcom-armv8a``` folder.
From within this folder, add meta-qcom layer:

```
bitbake-layers add-layer ../../meta-qcom
```

Change ```MACHINE``` variable into ```conf/local.conf``` to:

```
MACHINE ?= "qcom-armv8a"
```

Finally, build your image:

```
bitbake core-image-minimal
```

## Contributing

If you want to contribute changes, you can send Github pull requests at
https://github.com/Linaro/meta-qcom/pulls.

You can discuss about this layer, on `#linaro` on Libera Chat IRC network.

## Reporting issues

Please report any issue on https://github.com/Linaro/meta-qcom/issues

## Maintainer(s)

Dmitry Baryshkov <dmitry.baryshkov@linaro.org>
