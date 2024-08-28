INSERT INTO `REVIEW_GROUP` (`name`, `deleted`) VALUES
('TMS Review Group', 0);

INSERT INTO `REVIEW_GROUP_USERS` (`review_group_id`, `user_id`) VALUES
(1, 1);

UPDATE `TEST_CASE_VERSIONS` set `test_case_status` = 2 where `test_case_status` is null;


Update `TEST_CASE_VERSIONS_AUD` set `PRIORITY` = 'BLOCKER'
where `PRIORITY` = 'HIGH';
Update `TEST_CASE_VERSIONS_AUD` set `PRIORITY` = 'NORMAL'
where `PRIORITY` = 'MEDIUM';
Update `TEST_CASE_VERSIONS_AUD` set `PRIORITY` = 'MINOR'
where `PRIORITY` = 'LOW';
Update `TEST_CASE_VERSIONS_AUD` set `PRIORITY` = 'MINOR'
where `PRIORITY` = 'UNKNOWN';