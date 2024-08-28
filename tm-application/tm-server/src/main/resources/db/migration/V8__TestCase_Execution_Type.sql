-- add test case execution type field
ALTER TABLE `TEST_CASES` ADD COLUMN `execution_type` INTEGER;
ALTER TABLE `TEST_CASES_AUD` ADD COLUMN `execution_type` INTEGER;