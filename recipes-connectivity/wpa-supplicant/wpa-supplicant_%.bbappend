do_configure:append:qcom() {
        if ${@ bb.utils.contains('PACKAGECONFIG', 'openssl', 'true', 'false', d) }; then
                sed -i -e 's/^#\(CONFIG_OWE=y\)/\1/' wpa_supplicant/.config
        fi

        sed -i -e 's/^#\(CONFIG_IEEE80211BE=y\)/\1/' \
               -e 's/^#\(CONFIG_MBO=y\)/\1/' wpa_supplicant/.config
}
