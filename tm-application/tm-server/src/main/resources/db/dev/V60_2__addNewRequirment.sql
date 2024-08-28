UPDATE `DROPS` SET `default_drop` = TRUE WHERE `name` = 'Other';

INSERT INTO `PROJECTS` (`name`, `external_id`, `product_id`, `deleted`) VALUES
('Continuous Integration Support', 'CIS', 6, 0);

-- requirement for DE option
INSERT INTO `REQUIREMENTS` (`deleted`, `external_id`, `parent_id`, `external_type`, `external_label`, `external_summary`, `project_id`) VALUES
(0, 'CIS-1100', NULL, 'Story', 'CIS-1100', 'As a TAF user I want support for my taf testware', 38);