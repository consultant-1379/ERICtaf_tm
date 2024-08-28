INSERT INTO `PRODUCTS` (`external_id`, `name`) VALUES ('DE', 'DE');

-- Setup other fields values
INSERT INTO `PRODUCT_FEATURES` (`name`, `product_id`) VALUES
  ('Other', 2),
  ('Other', 3),
  ('Other', 4),
  ('Other', 5),
  ('Other', 6);

INSERT INTO `DROPS` (`name`, `product_id`) VALUES
  ('Other', 2),
  ('Other', 3),
  ('Other', 4),
  ('Other', 5),
  ('Other', 6);

INSERT INTO `TECHNICAL_COMPONENTS` (`name`, `product_id`, `product_feature_id`) VALUES
  ('Other', 2, (SELECT id FROM PRODUCT_FEATURES WHERE name = 'Other' AND product_id = 2)),
  ('Other', 3, (SELECT id FROM PRODUCT_FEATURES WHERE name = 'Other' AND product_id = 3)),
  ('Other', 4, (SELECT id FROM PRODUCT_FEATURES WHERE name = 'Other' AND product_id = 4)),
  ('Other', 5, (SELECT id FROM PRODUCT_FEATURES WHERE name = 'Other' AND product_id = 5)),
  ('Other', 6, (SELECT id FROM PRODUCT_FEATURES WHERE name = 'Other' AND product_id = 6));

-- Update drop ids
UPDATE `TEST_PLANS` t SET `drop_id` =
(SELECT id FROM `DROPS` WHERE name = 'Other' AND product_id =
(SELECT product_id FROM `PROJECTS` where id = t.project_id))
WHERE t.project_id is not null;

UPDATE `TEST_PLANS` t SET `drop_id` =
(SELECT id FROM `DROPS` WHERE name = 'Other' AND product_id = 2)
WHERE project_id is null;

-- Update feature ids
UPDATE `TEST_PLANS` t SET `product_feature_id` =
(SELECT id FROM `PRODUCT_FEATURES` WHERE name = 'Other' AND product_id =
(SELECT product_id FROM `PROJECTS` where id = t.project_id))
WHERE project_id is not null;

UPDATE `TEST_PLANS` t SET `product_feature_id` =
(SELECT id FROM `PRODUCT_FEATURES` WHERE name = 'Other' AND product_id = 2)
WHERE project_id is null;

CREATE UNIQUE INDEX `UK_PRODUCT_FEATURES` ON `PRODUCT_FEATURES` (`name` ASC, `product_id` ASC);
CREATE UNIQUE INDEX `UK_DROPS` ON `DROPS` (`name` ASC, `product_id` ASC);

ALTER TABLE `TEST_PLANS` ADD CONSTRAINT `FK_TEST_PLANS_PRODUCT_FEATURE` FOREIGN KEY (`product_feature_id`) REFERENCES `PRODUCT_FEATURES` (`id`);
ALTER TABLE `TEST_PLANS` ADD CONSTRAINT `FK_TEST_PLANS_DROP` FOREIGN KEY (`drop_id`) REFERENCES `DROPS` (`id`);
