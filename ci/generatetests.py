# Copyright (c) 2025 Qualcomm Innovation Center, Inc. All rights reserved.
# SPDX-License-Identifier: MIT

import logging
import os
import sys

from argparse import ArgumentParser, FileType
from jinja2 import Environment, FileSystemLoader

logger = logging.getLogger()


class Secrets:
    pass


if __name__ == "__main__":
    parser = ArgumentParser()
    parser.add_argument("--device", required=True, help="Device type in LAVA")
    parser.add_argument("--os", required=True, help="OS build (debian/qcom/poky)")
    parser.add_argument("--build-url", required=True, help="Base URL (without path) pointing to the downloads location")
    parser.add_argument("--templates", required=True, help="Base path to the templates directory")

    args = parser.parse_args()
    context = os.environ
    context.update({"BUILD_DOWNLOAD_URL": args.build_url})
    context.update({"DEVICE_TYPE": args.device})

    job_template_dir = os.path.join(args.templates, args.os, args.device)
    if not os.path.isdir(job_template_dir):
        logger.info(f"No templates found for: {args.os}/{args.device}")
        sys.exit(0)

    environment = Environment(loader=FileSystemLoader(job_template_dir))
    # get list of all templates
    with os.scandir(job_template_dir) as direntry:
        for entry in direntry:
            if entry.name.endswith('yaml') and entry.is_file():
                template = environment.get_template(entry.name)
                lava_job = template.render(context)
                with open(f"{args.device}-{args.os}-{entry.name}", "w") as lava_job_file:
                    lava_job_file.write(lava_job)

