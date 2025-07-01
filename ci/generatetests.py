import logging
import os
import sys
import yaml

from argparse import ArgumentParser, FileType
from tuxlava.jobs import Job

logger = logging.getLogger()


class Secrets:
    pass


if __name__ == "__main__":
    parser = ArgumentParser()
    parser.add_argument("--device", required=True, help="Device type in LAVA")
    parser.add_argument("--os", required=True, help="OS build (debian/qcom/poky)")
    parser.add_argument("--config", required=True, help="Config file in YAML format", type=FileType('r'))
    parser.add_argument("--build-url", required=True, help="Base URL (without path) pointing to the downloads location")

    args = parser.parse_args()

    conf = yaml.safe_load(args.config)
    # find device node in config
    device = conf['plan'].get(args.device)
    if not device:
        # device not found in config
        logger.warning(f"Device {args.device} not found in config")
        sys.exit(0)

    # find OS for the device
    build = device.get(args.os)
    if not build:
        # device not found in config
        logger.warning(f"OS {args.os} not found for {args.device}")
        sys.exit(0)


    for job in build.get("jobs"):
        job_name = job.get("name")
        tests = [d["name"] for d in job.get("tests")]
        rootfs = args.build_url + "/" + job.get("download")  # fixme: use urljoin
        parameters = {}
        for test in job.get("tests"):
            parameters.update(test.get("parameters", {}))

        secret_keys = job.get("secrets")

        if secret_keys:
            secrets = Secrets()
            for sec_key in secret_keys:
                setattr(secrets, sec_key, os.environ.get(sec_key))
            params = {}
            for key, value in parameters.items():
                # update value from secrect
                value = value.format(secrets=secrets)
                params.update({key: value})
            parameters = params

        header_secrets = {}
        for header in job.get("headers"):
            header_secrets.update(header)
        visibility = job.get("visibility", "public")
        def_arguments = {
            "device": f"flasher-{args.os}-{args.device}",
            "parameters": parameters,
            "rootfs": rootfs,
            "tests": tests,
            "visibility": visibility
        }
        if header_secrets:
            def_arguments.update({"secrets": header_secrets})
        j = Job(**def_arguments)
        j.initialize()
        with open(f"{args.device}-{args.os}-{job_name}.yaml", "w") as deffile:
            deffile.write(j.render())
