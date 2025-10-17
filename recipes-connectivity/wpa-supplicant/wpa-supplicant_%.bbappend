do_configure:append:qcom() {
        sed -i -e 's/^#\(CONFIG_IEEE80211BE=y\)/\1/' \
               -e 's/^#\(CONFIG_MBO=y\)/\1/' \
               -e 's/^#\(CONFIG_OWE=y\)/\1/' wpa_supplicant/.config
}
