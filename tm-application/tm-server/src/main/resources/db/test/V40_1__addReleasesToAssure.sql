-- add old Assure Releases
INSERT INTO RELEASES (`external_id`, `name`, `product_id`, `migrated`)
VALUES
('1.0.1', '1.0.1', 5, FALSE),
('1.0.2', '1.0.2', 5, FALSE);

-- update old Assure testplans to new assure releases for dev
Update TEST_PLANS tp
SET `release_id` = (SELECT `id` FROM RELEASES
  WHERE `product_id` = 5 AND `external_id` = '1.0.1'
)
WHERE `project_id` in (SELECT `id` FROM PROJECTS WHERE `product_id` = 5);