#!/bin/sh
# Copyright (c) 2025 Qualcomm Innovation Center, Inc. All rights reserved.
# SPDX-License-Identifier: BSD-3-Clause-Clear

# Function to format BD-Address and Set it
set_bda() {
    # Read the serial number
    serial_number=$(cat /sys/devices/soc0/serial_number)

    # Convert to hexadecimal
    hex_serial_number=$(printf '%x\n' $serial_number)

    # Format the serial number
    formatted_serial_number=$(echo "${hex_serial_number}" | sed 's/../&:/g;s/:$//')

    # Append qualcomm general prefix BDA value 22:22
    BDA=22:22:${formatted_serial_number:0:14}

    #After FW download completed wait for some time
    sleep 1

    # Pass the formatted serial number to btmgmt tool
    # Start btmgmt and send commands
    {
        echo "public-addr $BDA"
        sleep 1
    } | btmgmt
}


# Function to check BD Address and wait until it becomes configured or unconfigured
validate_and_set_bda() {
    while true; do
        # Get the BD Address
        hciconfig_output=$(hciconfig)

        bd_address=$(echo "$hciconfig_output" | grep 'BD Address' | awk '{print $3}')
        unconfigured=$(echo "$hciconfig_output" | grep -o 'DOWN RAW')
        configured=$(echo "$hciconfig_output" | grep -o 'DOWN')

        # Check if the BD Address is 00:00:00:00:00:00
        if [[ "$bd_address" == "00:00:00:00:00:00" ]]; then
            sleep 1
        elif  [[ "$unconfigured" == "DOWN RAW" ]]; then
           break
        elif [[ "$configured" == "DOWN" ]]; then
           echo "BD-Address already configured!!"
           return 0
        else
           break
        fi
    done

    # Call the set_bda function
    set_bda
}



# Run bluetoothctl show and capture the output
bluetoothctl_output=$(bluetoothctl show)

# Check if the BD Address is valid
if echo "$bluetoothctl_output" | grep -q "No default controller available"; then
    # Call the validate_and_set_bda function
    validate_and_set_bda
fi
