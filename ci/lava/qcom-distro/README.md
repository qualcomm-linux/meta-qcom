# Test setup

`ci/lava` directory contains directories with names corresponding to build MACHINE names.
New directories should only be added after the build is available for a given MACHINE.
All files in the `ci/lava/<MACHINE>` directory should have `.yaml` extension.
Each file should be a valid LAVA job template.

LAVA templates are used to create test jobs during the CI runs in this repository.
This happens for the following triggers:
 - pull_request
 - push
 - cron (nightly build)

# LAVA job templates

Job templates can use the following variables:
 - `DEVICE_TYPE`: name of the LAVA device type or alias. Full list can be found on [LAVA master web interface](https://lava.infra.foundries.io/scheduler/device_types)
 - `GITHUB_SHA`: Commit ID corresponding to the github action trigger
 - `BUILD_FILE_NAME`: Name of the build artifact to be downloaded. It's constructed as: `qcom-multimedia-image-${DEVICE_TYPE}.rootfs.qcomflash.tar.gz`
 - `BUILD_DOWNLOAD_URL`: URL where the build artifacts can be found. This variable is constructed as: `${{inputs.url}}/${DEVICE_TYPE}/${BUILD_FILE_NAME}` where `{{inputs.url}}` comes from the build action.
 - `GITHUB_RUN_ID`: ID of the current Github run.

After variable substitution the file should form a valid LAVA job definition.

# Template validation

Template validation is performed as github action using `lavasoftware/lava-server` container.
Version of the container will be kept in sync with the LAVA server running the test jobs.
Validation script, `schemacheck.py`, uses LAVA code from `lava_common.schemas.validate` method and PyYAML parser.

## Local template validation

Before checking the schema locally, all variables mentioned above need to be substituted.
Templates can be validated using `lavasoftware/lava-server` container.

    docker run --rm -v $PWD:/home/ lavasoftware/lava-server:latest python3 /home/ci/schemacheck.py /home/ci/lava
