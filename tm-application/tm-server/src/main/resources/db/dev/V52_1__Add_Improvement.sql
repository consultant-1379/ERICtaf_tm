REPLACE INTO `REQUIREMENTS` (`deleted`, `external_id`, `parent_id`, `release_id`, `external_type`, `external_title`, `external_summary`, `project_id`, `external_statusname`) VALUES
  (0, 'CIP-1234_1', NULL, 1, 'Improvement', 'CIP-1234_1', 'As a user I want to add Improvements to TMS', 2, 'Resolved'),
  (0, 'CIP-1234_2', NULL, 1, 'Improvement', 'CIP-1234_2', 'As a user I want to add even more Improvements to TMS', 2, 'Open');

UPDATE `REQUIREMENTS` SET `parent_id` = (SELECT t.id FROM (SELECT id, external_id FROM `REQUIREMENTS`)t WHERE t.external_id ='CIP-4209') WHERE external_id = 'CIP-1234_1';
UPDATE `REQUIREMENTS` SET `parent_id` = (SELECT t.id FROM (SELECT id, external_id FROM `REQUIREMENTS`)t WHERE t.external_id ='CIP-4209') WHERE external_id = 'CIP-1234_2';