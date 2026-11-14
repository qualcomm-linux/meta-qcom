# LK is built with gcc-arm-none-eabi-native from meta-arm-toolchain. Require
# this from the LK recipes and from anything that DEPENDS on them.

python () {
    if 'arm-toolchain' not in d.getVar('BBFILE_COLLECTIONS').split():
        raise bb.parse.SkipRecipe("LK requires the meta-arm-toolchain layer")
}
