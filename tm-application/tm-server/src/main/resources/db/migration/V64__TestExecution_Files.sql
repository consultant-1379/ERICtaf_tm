ALTER TABLE `FILE_STORAGE` DROP FOREIGN KEY `FK_TEST_CASES_FILE_STORAGE`;

ALTER TABLE `FILE_STORAGE` ADD COLUMN `category` VARCHAR(255) DEFAULT NULL AFTER `filename` ;

ALTER TABLE `FILE_STORAGE_AUD` ADD COLUMN `category` VARCHAR(255) DEFAULT NULL AFTER `filename` ;

UPDATE `FILE_STORAGE` SET `category` = 'TEST_CASE_FILE';