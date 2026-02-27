# dependency recipes, srt, libsrtp, and libnice are provided by meta-oe layer
PACKAGECONFIG:append:qcom = " sctp srt srtp webrtc"
