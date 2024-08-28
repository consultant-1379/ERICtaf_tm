SELECT `id` INTO @ENM_ID FROM `PRODUCTS` WHERE `name` = 'ENM';
SELECT `id` INTO @OSSRC_ID FROM `PRODUCTS` WHERE `name` = 'OSS-RC';
SELECT `id` INTO @ASSURE_ID FROM `PRODUCTS` WHERE `name` = 'Assure';
SELECT `id` INTO @DE_ID FROM `PRODUCTS` WHERE `name` = 'DE';
SELECT `id` INTO @EIFFEL_ID FROM `PRODUCTS` WHERE `name` = 'Eiffel';

INSERT INTO `PRODUCT_FEATURES` (`name`, `product_id`) VALUES
  ('FM', @OSSRC_ID),
  ('PM', @OSSRC_ID),
  ('CM', @OSSRC_ID),
  ('CoreNetwork', @OSSRC_ID),
  ('Platform', @OSSRC_ID),
  ('Deployment', @OSSRC_ID),
  ('SwManagment', @OSSRC_ID),
  ('TAF', @DE_ID),
  ('CI-AXIS', @DE_ID),
  ('SimulationsSolutions', @DE_ID),
  ('FM', @ENM_ID),
  ('CM', @ENM_ID),
  ('PM', @ENM_ID),
  ('Security', @ENM_ID),
  ('SHM', @ENM_ID),
  ('Deployment', @ENM_ID),
  ('Monitoring & TroubleShooting', @ENM_ID),
  ('Portfolio', @ENM_ID);

-- ENM FM Technical Components
SELECT `id` INTO @ENM_FM_ID FROM `PRODUCT_FEATURES` WHERE `name` = 'FM' AND `product_id` = @ENM_ID;

INSERT INTO `TECHNICAL_COMPONENTS` (`name`, `product_id`, `product_feature_id`) VALUES
  ('CPP MA', @ENM_ID, @ENM_FM_ID),
  ('SNMP MA', @ENM_ID, @ENM_FM_ID),
  ('BNSI NBI', @ENM_ID, @ENM_FM_ID),
  ('CORBA ALARM IRP NBI', @ENM_ID, @ENM_FM_ID),
  ('FMX', @ENM_ID, @ENM_FM_ID),
  ('UI', @ENM_ID, @ENM_FM_ID),
  ('FM Service', @ENM_ID, @ENM_FM_ID);

-- ENM CM Technical Components
SELECT `id` INTO @ENM_CM_ID FROM `PRODUCT_FEATURES` WHERE `name` = 'CM' AND `product_id` = @ENM_ID;

INSERT INTO `TECHNICAL_COMPONENTS` (`name`, `product_id`, `product_feature_id`) VALUES
  ('Auto ID Management', @ENM_ID, @ENM_CM_ID),
  ('Element Management', @ENM_ID, @ENM_CM_ID),
  ('Amos Node Check', @ENM_ID, @ENM_CM_ID),
  ('Amos', @ENM_ID, @ENM_CM_ID),
  ('AP', @ENM_ID, @ENM_CM_ID),
  ('Import', @ENM_ID, @ENM_CM_ID),
  ('Export', @ENM_ID, @ENM_CM_ID),
  ('CM NBI', @ENM_ID, @ENM_CM_ID),
  ('Python Client Scripting', @ENM_ID, @ENM_CM_ID),
  ('CM Modelling', @ENM_ID, @ENM_CM_ID),
  ('Revocation', @ENM_ID, @ENM_CM_ID),
  ('Add Node', @ENM_ID, @ENM_CM_ID),
  ('CM CRUD Operations', @ENM_ID, @ENM_CM_ID),
  ('Notification Handling', @ENM_ID, @ENM_CM_ID),
  ('SW Sync - Upgrade Independence', @ENM_ID, @ENM_CM_ID),
  ('Heartbeat - Synchronisation', @ENM_ID, @ENM_CM_ID),
  ('Non Live Configuration Handling', @ENM_ID, @ENM_CM_ID);

-- ENM PM Technical Components
SELECT `id` INTO @ENM_PM_ID FROM `PRODUCT_FEATURES` WHERE `name` = 'PM' AND `product_id` = @ENM_ID;

INSERT INTO `TECHNICAL_COMPONENTS` (`name`, `product_id`, `product_feature_id`) VALUES
  ('PMIC: EBS-M', @ENM_ID, @ENM_PM_ID),
  ('PMIC: EBM', @ENM_ID, @ENM_PM_ID),
  ('PMIC: UeTrace', @ENM_ID, @ENM_PM_ID),
  ('PMIC: CTUM', @ENM_ID, @ENM_PM_ID),
  ('PMIC: Statistical Subscription', @ENM_ID, @ENM_PM_ID),
  ('PMIC: Cell Trace Subscription', @ENM_ID, @ENM_PM_ID),
  ('PMIC: General', @ENM_ID, @ENM_PM_ID),
  ('PMIC: Cell Trace Decoder', @ENM_ID, @ENM_PM_ID),
  ('PMIC: Continuous Cell Trace', @ENM_ID, @ENM_PM_ID);

-- ENM Security Technical Components
SELECT `id` INTO @ENM_SECURITY_ID FROM `PRODUCT_FEATURES` WHERE `name` = 'Security' AND `product_id` = @ENM_ID;

INSERT INTO `TECHNICAL_COMPONENTS` (`name`, `product_id`, `product_feature_id`) VALUES
  ('Identity and Access Administration', @ENM_ID, @ENM_SECURITY_ID),
  ('System Security Configuration', @ENM_ID, @ENM_SECURITY_ID),
  ('Single Sign On', @ENM_ID, @ENM_SECURITY_ID),
  ('Access Control', @ENM_ID, @ENM_SECURITY_ID),
  ('Load Balancer', @ENM_ID, @ENM_SECURITY_ID),
  ('HTTP - HTTPS Gateway', @ENM_ID, @ENM_SECURITY_ID),
  ('Node Security', @ENM_ID, @ENM_SECURITY_ID),
  ('CredManager CLI', @ENM_ID, @ENM_SECURITY_ID),
  ('CredManager Service', @ENM_ID, @ENM_SECURITY_ID),
  ('PKI Manager', @ENM_ID, @ENM_SECURITY_ID),
  ('PKI Manager UI', @ENM_ID, @ENM_SECURITY_ID),
  ('PKI Web CLI', @ENM_ID, @ENM_SECURITY_ID),
  ('PKI Core', @ENM_ID, @ENM_SECURITY_ID),
  ('NSCS_SL2', @ENM_ID, @ENM_SECURITY_ID);

-- ENM SHM Technical Components
SELECT `id` INTO @ENM_SHM_ID FROM `PRODUCT_FEATURES` WHERE `name` = 'SHM' AND `product_id` = @ENM_ID;

INSERT INTO `TECHNICAL_COMPONENTS` (`name`, `product_id`, `product_feature_id`) VALUES
  ('SHM CLI', @ENM_ID, @ENM_SHM_ID),
  ('SHM UI', @ENM_ID, @ENM_SHM_ID),
  ('CPP Inventory Sync', @ENM_ID, @ENM_SHM_ID),
  ('Inventory view (CPP and ECIM nodes)', @ENM_ID, @ENM_SHM_ID),
  ('SHM Jobs (CPP, ECIM)', @ENM_ID, @ENM_SHM_ID),
  ('SHM Import functionality (Software Packages, License Key Files)', @ENM_ID, @ENM_SHM_ID),
  ('3gpp Inventory Export', @ENM_ID, @ENM_SHM_ID);

-- ENM Deployment Technical Components
SELECT `id` INTO @ENM_DEPLOYMENT_ID FROM `PRODUCT_FEATURES` WHERE `name` = 'Deployment' AND `product_id` = @ENM_ID;

INSERT INTO `TECHNICAL_COMPONENTS` (`name`, `product_id`, `product_feature_id`) VALUES
  ('ENM Installation', @ENM_ID, @ENM_DEPLOYMENT_ID),
  ('ENM Upgrade and Rollback', @ENM_ID, @ENM_DEPLOYMENT_ID),
  ('System Administration Activities', @ENM_ID, @ENM_DEPLOYMENT_ID),
  ('Backup and Restore', @ENM_ID, @ENM_DEPLOYMENT_ID),
  ('Licensing', @ENM_ID, @ENM_DEPLOYMENT_ID),
  ('Migration', @ENM_ID, @ENM_DEPLOYMENT_ID),
  ('System Wide Tests', @ENM_ID, @ENM_DEPLOYMENT_ID),
  ('LITP', @ENM_ID, @ENM_DEPLOYMENT_ID);

-- ENM Monitoring & Troubleshooting Technical Components
SELECT `id` INTO @ENM_MT_ID FROM `PRODUCT_FEATURES` WHERE `name` = 'Monitoring & TroubleShooting' AND `product_id` = @ENM_ID;

INSERT INTO `TECHNICAL_COMPONENTS` (`name`, `product_id`, `product_feature_id`) VALUES
  ('NHM', @ENM_ID, @ENM_MT_ID),
  ('NHC', @ENM_ID, @ENM_MT_ID),
  ('EniqIntegration', @ENM_ID, @ENM_MT_ID);

-- ENM Portfolio Technical Components
SELECT `id` INTO @ENM_PORTFOLIO_ID FROM `PRODUCT_FEATURES` WHERE `name` = 'Portfolio' AND `product_id` = @ENM_ID;

INSERT INTO `TECHNICAL_COMPONENTS` (`name`, `product_id`, `product_feature_id`) VALUES
  ('ENIQ-S', @ENM_ID, @ENM_PORTFOLIO_ID),
  ('ENIQ-E', @ENM_ID, @ENM_PORTFOLIO_ID),
  ('SON', @ENM_ID, @ENM_PORTFOLIO_ID),
  ('OSSRC', @ENM_ID, @ENM_PORTFOLIO_ID),
  ('OMBS', @ENM_ID, @ENM_PORTFOLIO_ID);

-- TAF
SELECT `id` INTO @DE_TAF_ID FROM `PRODUCT_FEATURES` WHERE `name` = 'TAF' AND `product_id` = @DE_ID;

INSERT INTO `TECHNICAL_COMPONENTS` (`name`, `product_id`, `product_feature_id`) VALUES
  ('TAF - UI', @DE_ID, @DE_TAF_ID),
  ('TAF - Core', @DE_ID, @DE_TAF_ID),
  ('TAF - CLI', @DE_ID, @DE_TAF_ID),
  ('TAF - Scenarios', @DE_ID, @DE_TAF_ID),
  ('TAF - Scheduler', @DE_ID, @DE_TAF_ID),
  ('TAF - Test Executor', @DE_ID, @DE_TAF_ID),
  ('TAF - Test Data Management', @DE_ID, @DE_TAF_ID),
  ('TAF - Test Registry', @DE_ID, @DE_TAF_ID),
  ('TAF - Test Management System', @DE_ID, @DE_TAF_ID),
  ('TAF - Test Verdict Service', @DE_ID, @DE_TAF_ID);

-- NetSim
SELECT `id` INTO @DE_NETSIM_ID FROM `PRODUCT_FEATURES` WHERE `name` = 'SimulationsSolutions' AND `product_id` = @DE_ID;

INSERT INTO `TECHNICAL_COMPONENTS` (`name`, `product_id`, `product_feature_id`) VALUES
  ('NetSim', @DE_ID, @DE_NETSIM_ID);

-- CI AXIS
SELECT `id` INTO @DE_AXIS_ID FROM `PRODUCT_FEATURES` WHERE `name` = 'CI-AXIS' AND `product_id` = @DE_ID;

INSERT INTO `TECHNICAL_COMPONENTS` (`name`, `product_id`, `product_feature_id`) VALUES
  ('CI Portal', @DE_ID, @DE_AXIS_ID);

-- Update OSS-RC Technical Components with Product Feature id
SELECT `id` INTO @OSSRC_FM_ID FROM `PRODUCT_FEATURES` WHERE `name` = 'FM' AND `product_id` = @OSSRC_ID;
SELECT `id` INTO @OSSRC_PM_ID FROM `PRODUCT_FEATURES` WHERE `name` = 'PM' AND `product_id` = @OSSRC_ID;
SELECT `id` INTO @OSSRC_CM_ID FROM `PRODUCT_FEATURES` WHERE `name` = 'CM' AND `product_id` = @OSSRC_ID;
SELECT `id` INTO @OSSRC_DEPLOYMENT_ID FROM `PRODUCT_FEATURES` WHERE `name` = 'Deployment' AND `product_id` = @OSSRC_ID;

UPDATE `TECHNICAL_COMPONENTS` SET `product_feature_id` = @OSSRC_FM_ID WHERE `name` LIKE 'FM-%';
UPDATE `TECHNICAL_COMPONENTS` SET `product_feature_id` = @OSSRC_PM_ID WHERE `name` LIKE 'PM-%';
UPDATE `TECHNICAL_COMPONENTS` SET `product_feature_id` = @OSSRC_CM_ID WHERE `name` LIKE 'CM-%';
UPDATE `TECHNICAL_COMPONENTS` SET `product_feature_id` = @OSSRC_DEPLOYMENT_ID WHERE `name` LIKE 'Deployment-%';
UPDATE `TECHNICAL_COMPONENTS` SET `product_feature_id` = (SELECT `id` FROM `PRODUCT_FEATURES` WHERE `name` = 'CoreNetwork') WHERE `name` LIKE 'CoreNetwork-%';
UPDATE `TECHNICAL_COMPONENTS` SET `product_feature_id` = (SELECT `id` FROM `PRODUCT_FEATURES` WHERE `name` = 'Platform') WHERE `name` LIKE 'Platform-%';
UPDATE `TECHNICAL_COMPONENTS` SET `product_feature_id` = (SELECT `id` FROM `PRODUCT_FEATURES` WHERE `name` = 'SwManagment') WHERE `name` LIKE 'SwManagment-%';
