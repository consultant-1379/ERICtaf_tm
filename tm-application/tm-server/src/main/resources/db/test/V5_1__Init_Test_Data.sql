-- Test data for local testing environment (H2)

INSERT INTO `REQUIREMENTS` (`id`, `deleted`, `external_id`, `parent_id`, `release_id`, `external_type`, `external_title`, `external_summary`, `project_id`) VALUES
(1, 0, 'CIP-4209', NULL, NULL, 'Epic', 'TAF Performance', 'A replacement resttool and the means to run rest load tests', 2),
(2, 0, 'CIP-4182', 1, NULL, 'Story', 'CIP-4182', 'As a TAF user I want an invesigation in to a reporting mechanism for load test results', 2),
(3, 0, 'CIP-4960', NULL, NULL, 'Story', 'CIP-4960', 'As a TE user I want to be able to manually edit a schedule and execute it', 2),
(4, 0, 'DURACI-2528', NULL, NULL, 'Story', 'DURACI-2528', 'As a TMS User I want to link Test Cases to Test Plans via REST', 3),
(5, 0, 'CIP-4183', 1, NULL, 'Story', 'CIP-4183', 'As a TAF user I want to have User Story without TestCases', 2),
(6, 0, 'CIP-5525', NULL, NULL, 'Story', 'CIP-5525', 'As a TAF user I want test logs copied to some storage area', 2);

INSERT INTO `IMPORTS` (`id`, `created_at`, `result`, `type`, `author_id`, `requirement_id`) VALUES
(1, '2014-05-12 23:43:46', 'SUCCESS', 'FREEMIND', NULL, 1),
(2, '2014-05-23 12:00:48', 'SUCCESS', 'FREEMIND', NULL, 2),
(3, '2014-05-23 12:00:58', 'SUCCESS', 'FREEMIND', NULL, 3),
(4, '2014-06-30 16:01:19', 'SUCCESS', 'FREEMIND', NULL, 4),
(5, '2014-07-01 10:01:19', 'SUCCESS', 'FREEMIND', NULL, 5);

INSERT INTO `TECHNICAL_COMPONENTS` (`id`, `deleted`, `name`) VALUES
(1, 0, 'TAF-Design'),
(2, 0, 'Test'),
(3, 0, 'Component X'),
(4, 0, 'CM CLI'),
(5, 0, 'TORRV'),
(6, 0, 'TOR'),
(7, 0, 'asdasd'),
(8, 0, 'Clitool'),
(9, 0, 'HTTP Tool'),
(10, 0, 'A1');

INSERT INTO `TEST_CASES` (`id`, `deleted`, `tc_id`, `description`, `priority`, `title`, `type`, `technical_component_id`) VALUES
(1, 0, '66a9bbfe-08b7-4267-8c25-8d2c6d3917a8', NULL, 'MEDIUM', 'Test', 'FUNCTIONAL', 1),
(2, 0, 'a62366f0-6703-4e38-944c-b1a34432dcdf', NULL, 'MEDIUM', 'sdfsfsdfsdfs', 'FUNCTIONAL', 2),
(3, 0, '5b18c890-ccaa-4d93-a282-e1b644b842d7', 'Test', 'HIGH', 'Test', 'FUNCTIONAL', 3),
(4, 0, '7a0793c1-143d-4e15-8063-78e0e78280a4', 'dsjfslfjskjfldk sjflsjfldkj', 'HIGH', 'My Test Case', 'FUNCTIONAL', 4),
(5, 0, 'ddc198c3-d228-49e0-83b8-3278a23ba246', NULL, 'HIGH', 'TORRV TEST: Add a node using CM CLI', 'WORKFLOW', 5) ,
(6, 0, '3898865c-312f-4195-96d3-138cc26849e3', 'Add a Node using CM CLI', 'HIGH', 'TORF-507 CM Engineer Adds a Node using CM CLI', 'ROBUSTNESS', 6),
(7, 0, '2d131049-ae66-40a8-a0ea-b5ac9521d859', 'qwe', 'HIGH', 'Sample Test Case', 'FUNCTIONAL', 7),
(8, 0, '1d7938e9-2f60-4d13-a20a-905aed30e128', 'asdasd', 'MEDIUM', 'TORRV test case', 'PERFORMANCE', 8),
(9, 0, 'CIP-3517', 'test dm', 'HIGH', 'test dm', 'FUNCTIONAL', 9),
(10, 0, 'CIP-2638_Func_1', 'This is a test .', 'LOW', 'CLI Test Case', 'FUNCTIONAL', 10),
(11, 0, 'CIP-2638_Func_2', 'dskkk DEMO', 'MEDIUM', 'efjefj', 'FUNCTIONAL', 1),
(12, 0, 'CIP-939', 'test1', 'HIGH', 'test1', 'FUNCTIONAL', 2),
(13, 0, 'test1', 'test1 description', 'HIGH', 'test', 'FUNCTIONAL', 3),
(14, 0, 'test', 'test description', 'HIGH', 'test', 'WORKFLOW', 4),
(15, 0, 'CIP-2638_Func_2_temp_12345', 'Description', 'LOW', 'This is a test', 'FUNCTIONAL', 5),
(16, 0, 'CIP-2638_Func_3_Temp_876', 'Description', 'LOW', 'Title', 'SECURITY', 6),
(17, 0, 'CIP-2099_Func_23', 'Description', 'MEDIUM', 'Title', 'FUNCTIONAL', 7),
(18, 0, 'CIP-2638_Func_3', 'Description', 'MEDIUM', 'Title', 'ROBUSTNESS', 8),
(19, 0, 'CIP-939_Func111', 'ddd', 'MEDIUM', 'Test', 'FUNCTIONAL', 9),
(20, 1, 'OSS-28475_Func_1', 'Select ONRM Root in Subnetwork and SGSN tab selected in status view and validate SGSN present in status view with seg_cs', 'HIGH', 'Launch Status View with SGSN selected in C topology view and check that SGSN view is enabled', 'FUNCTIONAL', 10),
(21, 0, 'DURACI-2528_Func_1', 'ddd', 'HIGH', 'Test', 'FUNCTIONAL', 2);

INSERT INTO `IMPORT_TEST_CASES` (`import_id`, `test_case_id`) VALUES
(2, 1),
(2, 2),
(3, 3),
(2, 4),
(2, 5),
(3, 6),
(1, 7),
(2, 8),
(3, 9),
(1, 10),
(2, 11),
(3, 12),
(2, 13),
(2, 14),
(3, 15),
(2, 16),
(2, 17),
(3, 18),
(2, 19),
(2, 20),
(4, 21);

INSERT INTO `REQUIREMENT_TEST_CASES` (`requirement_id`, `test_case_id`) VALUES
(2, 1),
(2, 2),
(3, 3),
(2, 4),
(2, 5),
(3, 6),
(2, 7),
(2, 8),
(3, 9),
(2, 10),
(2, 11),
(3, 12),
(2, 13),
(2, 14),
(3, 15),
(2, 16),
(2, 17),
(3, 18),
(2, 19),
(2, 20),
(4, 21);

INSERT INTO `SCOPE_TEST_CASES` (`scope_id`, `test_case_id`) VALUES
(1, 1),
(2, 2),
(3, 3),
(1, 4),
(2, 5),
(3, 6),
(1, 7),
(2, 8),
(3, 9),
(1, 10),
(2, 11),
(3, 12),
(1, 13),
(2, 14),
(3, 15),
(1, 16),
(2, 17),
(3, 18),
(1, 19),
(2, 20),
(1, 21),
(2, 21);


INSERT INTO `TEST_FIELDS` (`id`, `deleted`, `field`, `name`, `value`, `test_case_id`) VALUES
	(1, 0, 'STRING', 'CONTEXT', 'REST', 1),
	(2, 0, 'STRING', 'PRE', 'Precondition', 1),
	(3, 0, 'STRING', 'CONTEXT', 'REST', 2),
	(4, 0, 'STRING', 'PRE', 'Precondition', 2),
	(5, 0, 'STRING', 'CONTEXT', 'REST', 3),
	(6, 0, 'STRING', 'PRE', 'Precondition', 3),
	(7, 0, 'STRING', 'CONTEXT', 'REST', 4),
	(8, 0, 'STRING', 'PRE', 'Precondition', 4),
	(9, 0, 'STRING', 'CONTEXT', 'REST', 5),
	(10, 0, 'STRING', 'PRE', 'Precondition', 5),
	(11, 0, 'STRING', 'CONTEXT', 'REST', 6),
	(12, 0, 'STRING', 'PRE', 'Precondition', 6),
	(13, 0, 'STRING', 'CONTEXT', 'REST', 7),
	(14, 0, 'STRING', 'PRE', 'Precondition', 7),
	(15, 0, 'STRING', 'CONTEXT', 'REST', 8),
	(16, 0, 'STRING', 'PRE', 'Precondition', 8),
	(17, 0, 'STRING', 'CONTEXT', 'REST', 9),
	(18, 0, 'STRING', 'PRE', 'Precondition', 9),
	(19, 0, 'STRING', 'CONTEXT', 'REST', 10),
	(20, 0, 'STRING', 'PRE', 'Precondition', 10),
	(21, 0, 'STRING', 'CONTEXT', 'REST', 11),
	(22, 0, 'STRING', 'PRE', 'Precondition', 11),
	(23, 0, 'STRING', 'CONTEXT', 'REST', 12),
	(24, 0, 'STRING', 'PRE', 'Precondition', 12),
	(25, 0, 'STRING', 'CONTEXT', 'REST', 13),
	(26, 0, 'STRING', 'PRE', 'Precondition', 13),
	(27, 0, 'STRING', 'CONTEXT', 'REST', 14),
	(28, 0, 'STRING', 'PRE', 'Precondition', 14),
	(29, 0, 'STRING', 'CONTEXT', 'REST', 15),
	(30, 0, 'STRING', 'PRE', 'Precondition', 15),
	(31, 0, 'STRING', 'CONTEXT', 'REST', 16),
	(32, 0, 'STRING', 'PRE', 'Precondition', 16),
	(33, 0, 'STRING', 'CONTEXT', 'REST', 17),
	(34, 0, 'STRING', 'PRE', 'Precondition', 17),
	(35, 0, 'STRING', 'CONTEXT', 'REST', 18),
	(36, 0, 'STRING', 'PRE', 'Precondition', 18),
	(37, 0, 'STRING', 'CONTEXT', 'REST', 19),
	(38, 0, 'STRING', 'PRE', 'Precondition', 19),
	(39, 0, 'STRING', 'CONTEXT', 'REST', 20),
	(40, 0, 'STRING', 'PRE', 'Precondition', 20),
	(41, 0, 'STRING', 'CONTEXT', 'REST', 21),
	(42, 0, 'STRING', 'PRE', 'Precondition', 21);

INSERT INTO `USERS` (`id`, `external_id`) VALUES
  (1, 'taf');

INSERT INTO `USER_PROFILES` (`id`, `user_id`, `project_id`) VALUES
  (1, 1, null);

INSERT INTO `TEST_STEPS` (`id`, `deleted`, `execute`, `sequence_order`, `test_case_id`) VALUES
	(1, 0, 'Test Step', 1, 1),
	(2, 0, 'Test Step', 2, 1),
	(3, 0, 'Test Step', 1, 2),
	(4, 0, 'Test Step 1', 2, 2),
	(5, 0, 'Test Step', 1, 3),
	(6, 0, 'Access the 14B TOR CPI Confluence Page for instructions to add node MOIs via the CLI.', 2, 3),
	(7, 0, 'Enter the Create Node command line detail.Ã‚Â ', 1, 4),
	(8, 0, 'CM Engineer logs on to TOR Launcher', 2, 4),
	(9, 0, 'Log in', 1, 5),
	(10, 0, 'qwew', 2, 5),
	(11, 0, 'Test Step', 1, 6),
	(12, 0, 'Test Step', 2, 6),
	(13, 0, 'Enter a Command', 1,  7),
	(14, 0, 'run comand', 2,  7),
	(15, 0, 'Test Step1', 1,  8),
	(16, 0, 'Test Step1', 2,  8),
	(17, 0, 'Test Step 1', 1,  9),
	(18, 0, 'Test Step', 2,  9),
	(19, 0, 'Test Step', 1,  10),
	(20, 0, 'Test Step', 2,  10),
	(21, 0, 'Test Step 12', 2,  11),
	(22, 0, 'Test Step', 1,  11),
	(23, 0, 'dddd', 1,  12),
	(24, 0, 'Seg CS command 1', 2,  12),
	(25, 0, 'Seg CS command 1', 1,  13),
	(26, 0, 'Seg CS command 2', 2,  13),
	(27, 0, 'Seg CS command 1', 1,  14),
	(28, 0, 'Seg CS command 2', 2,  14),
	(29, 0, 'Seg CS command 1', 1,  15),
	(30, 0, 'Seg CS command 2', 2,  15),
	(31, 0, 'Seg CS command 1', 1,  16),
	(32, 0, 'Seg CS command 2', 2,  16),
	(33, 0, 'Seg CS command 1', 1,  17),
	(34, 0, 'Seg CS command 2', 2,  17),
	(35, 0, 'Seg CS command 1', 1,  18),
	(36, 0, 'Seg CS command 2', 2,  18),
	(37, 0, 'Seg CS command 1', 1,  19),
	(38, 0, 'Seg CS command 2', 2,  19),
	(39, 0, 'Seg CS command 1', 1,  20),
	(40, 0, 'Seg CS command 2', 2,  20),
	(41, 0, 'Test Step 1', 1,  21),
	(42, 0, 'Test Step 2', 2,  21),
	(43, 0, 'Test Step 3', 3,  21),
	(44, 0, 'Test Step 4', 4,  21);

INSERT INTO `VERIFY_STEPS` (`id`, `deleted`, `verify_step`, `test_step_id`, `SEQUENCE_ORDER`) VALUES
	(1, 0, 'Verify Step 1.1', 1, 1),
	(2, 0, 'Verify Step', 2, 1),
	(3, 0, 'sadfsdfsdf', 3, 1),
	(4, 0, 'Verify Step', 4, 1),
	(5, 0, 'Verify Step 2', 5, 1),
	(6, 0, 'Verify Step 1', 6, 1),
	(7, 0, 'Verify Step', 7, 1),
	(8, 0, 'Instruction given on how to add a node to TOR.', 8, 1),
	(9, 0, 'When the ManagedObject has been created it will be displayed back to the user along with all attributes.', 9, 1),
	(10, 0, 'TOR Launcher successfully deploys.', 10, 1),
	(11, 0, 'Verify Step', 11, 1),
	(12, 0, 'qwe', 12, 1),
	(13, 0, 'Verify Step', 13, 1),
	(14, 0, 'Verify Step', 14, 1),
	(15, 0, 'Verify Step 1', 15, 1),
	(16, 0, 'Get the Expected Result', 16, 1),
	(17, 0, 'get result', 17, 1),
	(18, 0, 'Verify Step1', 18, 1),
	(19, 0, 'Verify Step1', 19, 1),
	(20, 0, 'Verify Step 1', 20, 1),
	(21, 0, 'Verify Step', 21, 1),
	(22, 0, 'Verify Step', 22, 1),
	(23, 0, 'Verify Step', 23, 1),
	(24, 0, 'Verify Step 33', 24, 1),
	(25, 0, 'Verify Step 1.1', 25, 1),
	(26, 0, 'Verify Step 2.1', 26, 1),
	(27, 0, 'dddd', 27, 1),
	(28, 0, 'EXITCODE AS EXPECTED', 28, 1),
	(29, 0, 'EXITCODE AS EXPECTED', 29, 1),
	(30, 0, 'EXITCODE AS EXPECTED', 30, 1),
	(31, 0, 'EXITCODE AS EXPECTED', 31, 1),
	(32, 0, 'EXITCODE AS EXPECTED', 32, 1),
	(33, 0, 'EXITCODE AS EXPECTED', 33, 1),
	(34, 0, 'EXITCODE AS EXPECTED', 34, 1),
	(35, 0, 'EXITCODE AS EXPECTED', 35, 1),
	(36, 0, 'EXITCODE AS EXPECTED', 36, 1),
	(37, 0, 'EXITCODE AS EXPECTED', 37, 1),
	(38, 0, 'EXITCODE AS EXPECTED', 38, 1),
	(39, 0, 'EXITCODE AS EXPECTED', 39, 1),
	(40, 0, 'EXITCODE AS EXPECTED', 40, 1),
	(41, 0, 'Verify step 1.1', 41, 1),
	(42, 0, 'Verify step 1.2', 41, 2),
	(43, 0, 'Verify step 1.3', 41, 3),
	(44, 0, 'Verify step 1.4', 41, 4),
	(45, 0, 'Verify step 2.1', 42, 1),
	(46, 0, 'Verify step 2.2', 42, 2),
	(47, 0, 'Verify step 2.3', 42, 3),
	(48, 0, 'Verify step 3.1', 43, 1),
	(49, 0, 'Verify Step 1.2', 1, 2),
	(50, 0, 'Verify Step 1.2', 25, 2);
