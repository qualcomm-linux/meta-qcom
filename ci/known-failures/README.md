# Known failures

Each file lists the test failures that are known and accepted for one
distro tested by `.github/workflows/test.yml`. The test job summary
reports a listed failure as a known failure and counts it as a pass.
A listed test that passes is reported as an unexpected pass and counted
as a failure, so remove the entry once its issue is fixed.

The top-level keys are LAVA device types, as shown in the summary table
(for example `hamoa-iot-evk` for iq-x7181-evk), or `*` for all devices.
Each one holds a list of test case names, either bare or as a mapping:

```yaml
hamoa-iot-evk:
  - test: fastrpc_test
    comment: https://github.com/<org>/<repo>/issues/<number>
    kernels:
      - linux-qcom-6.18
  - WiFi_OnOff
```

`comment` should link the issue the failure is tracked in; it is shown in
the known failures table of the summary. `kernels` restricts the entry to
the listed kernel flavours, as passed in `kernel` by `test.yml`
(`linux-qcom-next` or `linux-qcom-6.18`); without it the entry applies to
every kernel.
