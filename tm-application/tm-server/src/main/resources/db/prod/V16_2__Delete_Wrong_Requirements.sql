delete from REQUIREMENTS where external_type is null and not(id in (select requirement_id from REQUIREMENT_TEST_CASES));