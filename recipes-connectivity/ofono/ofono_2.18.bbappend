do_install:append:qcom () {
  sed -i '/<\/busconfig>/i\
  <policy user="\pipewire\">\
    <allow send_destination=\"org.ofono\"/>\
  </policy>' ${D}${sysconfdir}/dbus-1/system.d/ofono.conf
}
