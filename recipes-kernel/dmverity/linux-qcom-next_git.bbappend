require ${@bb.utils.contains('IMAGE_CLASSES', 'dm-verity-img', 'dm-verity-kernel-conf.inc', '', d)}
