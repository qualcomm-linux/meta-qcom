do_install:append:qcom () {

  install -v -m 0644 \
      ${S}/src/config/wireplumber.conf.d.examples/bluetooth.conf \
      ${D}${datadir}/wireplumber/wireplumber.conf.d/

  sed -i '/^[[:space:]]*main[[:space:]]*=[[:space:]]*{/,/^[[:space:]]*}/{
    /policy\.standard[[:space:]]*=/a\
  monitor.bluez.seat-monitoring = disabled
  }' ${D}${datadir}/wireplumber/wireplumber.conf
}
