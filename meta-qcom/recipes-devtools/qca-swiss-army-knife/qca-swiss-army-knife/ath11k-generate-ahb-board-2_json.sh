#!/bin/sh
#
# Copyright (c) 2024 Linaro
#
# SPDX-License-Identifier: MIT
#

JSON="$2"

iter=0
echo "[" > "${JSON}"
echo "  {" >> "${JSON}"
echo "    \"board\": [" >> "${JSON}"
for file in $1/bdwlan.elf $1/bdwlan.e* ; do
    iter=$((iter+1))
    [ $iter -ne 1 ] && echo "      }," >> "${JSON}"

    echo "      {" >> "${JSON}"
    echo "          \"data\": \"$file\"," >> "${JSON}"
    if [ `basename $file` = "bdwlan.elf" ]; then
        file_ext="255"
    else
        file_ext="$(( $(basename "${file}" | sed -E 's:^.*\.e?([0-9a-f]*)$:0x\1:') ))"
    fi
    echo "          \"names\": [\"bus=ahb,qmi-chip-id=1,qmi-board-id=${file_ext}\"]" >> "${JSON}"
done

echo "      }" >> "${JSON}"

if [ -r $1/regdb.bin ] ; then
echo "    ]," >> "${JSON}"
echo "    \"regdb\": [" >> "${JSON}"
iter=0
for file in $1/bdwlan.elf $1/bdwlan.e* ; do
    iter=$((iter+1))
    [ $iter -ne 1 ] && echo "      }," >> "${JSON}"

    echo "      {" >> "${JSON}"
    echo "        \"data\": \"$1/regdb.bin\"," >> "${JSON}"
    if [ `basename $file` = "bdwlan.elf" ]; then
        file_ext="255"
    else
        file_ext="$(( $(basename "${file}" | sed -E 's:^.*\.e?([0-9a-f]*)$:0x\1:') ))"
    fi
    echo "          \"names\": [\"bus=ahb,qmi-chip-id=1,qmi-board-id=${file_ext}\"]" >> "${JSON}"
done
echo "      }" >> "${JSON}"
fi

echo "    ]" >> "${JSON}"
echo "  }" >> "${JSON}"
echo "]" >> "${JSON}"

