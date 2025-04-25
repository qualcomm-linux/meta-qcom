# meta-axiom-ci

## Introduction

OpenEmbedded/Yocto Project layer for using the Qualcomm internal Axiom CI on Qualcomm based platforms.

The official documentation describes Axiom as such:
> Axiom is an enterprise-level integrated automation system and a unified source of truth for test data. It covers all aspects of the test process including planning, execution, reporting and telematics.

This layer extends a small set of image recipes to add the tools and configs needed to run on the Axiom CI.

This layer depends on:

```
URI: https://github.com/openembedded/openembedded-core.git
layers: meta
branch: master
revision: HEAD
```

```
URI: https://github.com/openembedded/meta-openembedded.git
layers: meta-oe, meta-networking
branch: master
revision: HEAD
```


