#!/bin/sh
#
# Copyright (c) 2020-2021 Linaro
#
# SPDX-License-Identifier: MIT
#

JSON="$2"

iter=0
echo "[" > "${JSON}"
for file in $1/bdwlan.*; do
    case `basename "${file}"` in
        *.txt)
            continue
            ;;
        bdwlan.bin)
            file_ext="ff"
            ;;
        bdwlan.*)
            file_ext="$(basename "${file}" | sed -E 's:^.*\.b?0*([0-9a-f]+)$:\1:')"
            ;;
    esac

    iter=$((iter+1))
    [ $iter -ne 1 ] && echo "  }," >> "${JSON}"

    echo "  {" >> "${JSON}"
    echo "          \"data\": \"$file\"," >> "${JSON}"
    echo "          \"names\": [\"bus=snoc,qmi-board-id=${file_ext}\"]" >> "${JSON}"
done

echo "  }" >> "${JSON}"
echo "]" >> "${JSON}"

