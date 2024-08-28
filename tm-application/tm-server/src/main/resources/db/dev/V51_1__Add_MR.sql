REPLACE INTO `REQUIREMENTS` (`deleted`, `external_id`, `parent_id`, `release_id`, `external_type`, `external_title`, `external_summary`, `project_id`, `external_statusname`) VALUES
  (0, 'CIP-12554', NULL, 1, 'MR', 'CIP-12554', 'As a user I want to add MRs to TMS', 2, 'Resolved');

UPDATE `REQUIREMENTS` SET `parent_id` = (SELECT t.id FROM (SELECT id, external_id FROM `REQUIREMENTS`)t WHERE t.external_id ='CIP-12554') WHERE external_id = 'CIP-4209';