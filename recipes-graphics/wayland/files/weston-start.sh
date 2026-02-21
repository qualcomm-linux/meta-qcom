#!/bin/sh
# Weston startup script
# Dynamically detects DRM cards and configures additional-devices parameter

# Initialize options
opts="--modules=systemd-notify.so"
additional_cards=""
first_kms_card=""
kms_cards=""

# Build a sorted list of KMS-capable cards by their platform path
# This ensures consistent ordering regardless of probe order
for path_link in /dev/dri/by-path/platform-*-card; do
    # Skip if not a symlink
    [ -L "$path_link" ] || continue

    # Resolve symlink to actual device
    card=$(readlink -f "$path_link")
    [ -n "$card" ] || continue

    card_name=$(basename "$card")
    [ -n "$card_name" ] || continue

    # Check if this card has KMS capability by looking for connectors
    has_kms=0
    for connector in /sys/class/drm/${card_name}-*; do
        if [ -d "$connector" ]; then
            has_kms=1
            break
        fi
    done

    # Only process KMS-capable cards
    if [ "$has_kms" -eq 1 ]; then
        # Extract platform address for sorting
        platform_addr=$(echo "$path_link" | sed 's/.*platform-\([0-9a-f]*\).*/\1/')

        # Verify we extracted a valid address
        if [ -n "$platform_addr" ]; then
            kms_cards="$kms_cards $platform_addr:$card_name"
        fi
    fi
done

# Sort by platform address to get consistent ordering
sorted_cards=$(echo "$kms_cards" | tr ' ' '\n' | grep -v '^$' | sort | cut -d: -f2)

# First card in sorted list is primary (Weston will auto-select it)
# Remaining cards are added to additional-devices
for card_name in $sorted_cards; do
    if [ -z "$first_kms_card" ]; then
        # Mark first card as primary (we skip adding it)
        first_kms_card="$card_name"
    else
        # Add subsequent cards to additional-devices list
        if [ -z "$additional_cards" ]; then
            additional_cards="$card_name"
        else
            additional_cards="$additional_cards,$card_name"
        fi
    fi
done

# Add additional-devices parameter if we found any
if [ -n "$additional_cards" ]; then
    opts="$opts --additional-devices=$additional_cards"
fi

# Execute weston with the constructed options
exec @bindir@/weston $opts
