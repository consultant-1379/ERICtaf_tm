-- Insert additional projects used in AVS test cases
INSERT INTO `PROJECTS` (`name`, `external_id`) VALUES
('TOR Release Verification', 'TORRV'),
('OSS-RC RV Automation', 'ORA'),
('WLRCM', 'WLRCM'),
('OSS WASS', 'OW'),
('SON Viz CI transformation', 'SVCT'),
('DEFT-FRT-CentralRegression', 'DEFTFC' ),
('BSIM-AUTOMATION', 'BSIMAUTO'),
('TAF Training and Testing', 'TAFTEST'),
('PLM ASSURE', 'PLMEEQV');

-- Attaches existing requirements to projects based on external id value
-- Some of the test cases will not be mapped
UPDATE REQUIREMENTS R
SET R.project_id = 
	(SELECT id 
	 FROM PROJECTS P 
	 WHERE P.external_id = SUBSTRING(R.external_id, 1, LOCATE('-', R.external_id) - 1)
	 );