-- Populate products table
INSERT INTO `PRODUCTS` (`id`, `external_id`, `name`) VALUES (5, 'Assure', 'Assure');

-- Link ASSURE projects
UPDATE `PROJECTS`
SET `product_id` = 5
WHERE `external_id` IN ('EQEV', 'PLMEEQV');
