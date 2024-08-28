-- copy OSSRC Releases to Assure Releases
INSERT INTO RELEASES (`external_id`, `name`, `product_id`, `migrated`)
SELECT `external_id`, `name`, 5, `migrated`
FROM RELEASES
WHERE `product_id` = 3
AND (name LIKE '%ENIQ%'
OR name LIKE '%Events%'
OR name LIKE '%Eniq%'
OR name LIKE '%EE%')
ORDER BY id ASC;


-- add Assure Releases currently in use
INSERT INTO RELEASES (`external_id`, `name`, `product_id`, `migrated`)
VALUES
('15B', '15B', 5, FALSE),
('14B', '14B', 5, FALSE),
('4.1.11', '4.1.11', 5, FALSE),
('4.1.10', '4.1.10', 5, FALSE),
('4.1.9', '4.1.9', 5, FALSE),
('Release 14B CSL', 'Release 14B CSL', 5, FALSE);


-- update all tesplans to new assure releases
Update TEST_PLANS tp
SET `release_id` = (SELECT `id` FROM RELEASES
  WHERE `product_id` = 5 AND `external_id` in (SELECT `external_id` FROM `RELEASES` WHERE id = tp.release_id )
)
WHERE `project_id` in (SELECT `id` FROM PROJECTS WHERE `product_id` = 5);