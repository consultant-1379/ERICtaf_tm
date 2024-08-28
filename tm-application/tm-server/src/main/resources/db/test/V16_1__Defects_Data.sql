INSERT INTO `DEFECTS` (`id`, `deleted`, `external_id`, `external_title`, `external_summary`, `project_id`) VALUES (1, 0, 'DURACI-3078', 'DURACI-3078', 'Description in the Requirement Field.', NULL);
INSERT INTO `DEFECTS` (`id`, `deleted`, `external_id`, `external_title`, `external_summary`, `project_id`) VALUES (2, 0, 'TORF-1234', 'TORF-1234', 'Defaul Bug form Migration Data sql file.', NULL);
INSERT INTO `DEFECTS` (`id`, `deleted`, `external_id`, `external_title`, `external_summary`, `project_id`) VALUES (3, 0, 'DURACI-3083', 'DURACI-3083', 'Requirement IDs dissapear after switching page.', NULL);
INSERT INTO `DEFECTS` (`id`, `deleted`, `external_id`, `external_title`, `external_summary`, `project_id`) VALUES (4, 0, 'DURACI-2975', 'DURACI-2975', 'Accordion icon for Test Steps is not intuitive for users to use.', NULL);
INSERT INTO `DEFECTS` (`id`, `deleted`, `external_id`, `external_title`, `external_summary`, `project_id`) VALUES (5, 0, 'DURACI-2980', 'DURACI-2980', 'Very user friendly TM System', NULL);
INSERT INTO `DEFECTS` (`id`, `deleted`, `external_id`, `external_title`, `external_summary`, `project_id`) VALUES (6, 0, 'DURACI-3634', 'DURACI-3634', 'Functional test job finishes with success when grid fails', NULL);
INSERT INTO `DEFECTS` (`id`, `deleted`, `external_id`, `external_title`, `external_summary`, `project_id`) VALUES (7, 0, 'DURACI-3873', 'DURACI-3873', 'TestPlan create page, fields are not cleared', NULL);


INSERT INTO `DEFECTS_TEST_EXECUTIONS` (`defect_id`, `test_execution_id`) VALUES (1, 1);
INSERT INTO `DEFECTS_TEST_EXECUTIONS` (`defect_id`, `test_execution_id`) VALUES (2, 2);
INSERT INTO `DEFECTS_TEST_EXECUTIONS` (`defect_id`, `test_execution_id`) VALUES (3, 3);
INSERT INTO `DEFECTS_TEST_EXECUTIONS` (`defect_id`, `test_execution_id`) VALUES (4, 4);
INSERT INTO `DEFECTS_TEST_EXECUTIONS` (`defect_id`, `test_execution_id`) VALUES (5, 5);
INSERT INTO `DEFECTS_TEST_EXECUTIONS` (`defect_id`, `test_execution_id`) VALUES (5, 6);

INSERT INTO `DEFECTS_TEST_EXECUTIONS` (`defect_id`, `test_execution_id`) VALUES (1, 7);
INSERT INTO `DEFECTS_TEST_EXECUTIONS` (`defect_id`, `test_execution_id`) VALUES (2, 7);
INSERT INTO `DEFECTS_TEST_EXECUTIONS` (`defect_id`, `test_execution_id`) VALUES (3, 7);

INSERT INTO `DEFECTS_TEST_EXECUTIONS` (`defect_id`, `test_execution_id`) VALUES (6, 15);
INSERT INTO `DEFECTS_TEST_EXECUTIONS` (`defect_id`, `test_execution_id`) VALUES (7, 15);
