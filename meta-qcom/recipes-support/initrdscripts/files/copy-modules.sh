#!/bin/sh
# Copyright (C) 2022 Linaro Ltd.
# SPDX-License-Identifier: MIT

copy_modules_enabled() {
	[ -n "${bootparam_copy_modules}" -a -d /lib/modules/`uname -r` ]
}

copy_modules_run() {
	if [ -n "$ROOTFS_DIR" ]; then
		rm -rf $ROOTFS_DIR/lib/modules/`uname -r`
		mkdir -p $ROOTFS_DIR/lib/modules
		cp -a /lib/modules/`uname -r` $ROOTFS_DIR/lib/modules
	else
		debug "No rootfs has been set"
	fi
}
