# Machine specific configurations

MACHINE_OPTEE_TEST_REQUIRE ?= ""
MACHINE_OPTEE_TEST_REQUIRE:qcm6490 = "optee-test-qcm6490.inc"

require ${MACHINE_OPTEE_TEST_REQUIRE}
