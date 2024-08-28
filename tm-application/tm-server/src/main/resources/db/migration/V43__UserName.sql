ALTER TABLE `USERS` ADD COLUMN `external_username` TEXT NULL DEFAULT NULL;
ALTER TABLE `USERS_AUD` ADD COLUMN `external_username` TEXT NULL DEFAULT NULL;


UPDATE USERS set `external_username` = concat(`external_name`, ' ', external_surname)
WHERE `external_name` IS NOT NULL ;
