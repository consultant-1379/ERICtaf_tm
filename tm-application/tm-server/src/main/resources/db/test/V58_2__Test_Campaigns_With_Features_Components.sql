SELECT `id` INTO @ENM_DROP_1_0_ID FROM `DROPS` WHERE `name` = '1.0.ENM';
SELECT `id` INTO @ENM_DROP_16_3_ID FROM `DROPS` WHERE `name` = '16.3';
SELECT `id` INTO @OSS_DROP_1_0_ID FROM `DROPS` WHERE `name` = '1.0.OSS';
SELECT `id` INTO @OSS_DROP_2_0_ID FROM `DROPS` WHERE `name` = '2.0.OSS';

SELECT `id` INTO @ENM_FM_ID FROM `PRODUCT_FEATURES` WHERE `name` = 'FM' AND `product_id` = @ENM_ID;
SELECT `id` INTO @ENM_CM_ID FROM `PRODUCT_FEATURES` WHERE `name` = 'CM' AND `product_id` = @ENM_ID;
SELECT `id` INTO @ENM_PM_ID FROM `PRODUCT_FEATURES` WHERE `name` = 'PM' AND `product_id` = @ENM_ID;

SELECT `id` INTO @OSSRC_CM_ID FROM `PRODUCT_FEATURES` WHERE `name` = 'CM' AND `product_id` = @OSSRC_ID;
SELECT `id` INTO @OSSRC_DEPLOYMENT_ID FROM `PRODUCT_FEATURES` WHERE `name` = 'Deployment' AND `product_id` = @OSSRC_ID;

INSERT INTO `TEST_PLANS` (`id`,`name`, `description`, `project_id`, `environment`, `drop_id`, `product_feature_id`) VALUES
  (11, 'ENM Test Plan #1', 'ENM Test plan #1', NULL, 'test', @ENM_DROP_1_0_ID, @ENM_FM_ID),
  (12, 'ENM Test Plan #2', 'ENM Test plan #2', NULL, 'test', @ENM_DROP_1_0_ID, @ENM_FM_ID),
  (13, 'ENM Test Plan #3', 'ENM Test plan #3', NULL, 'test', @ENM_DROP_1_0_ID, @ENM_CM_ID),
  (14, 'ENM Test Plan #4', 'ENM Test plan #4', NULL, 'test', @ENM_DROP_16_3_ID, @ENM_CM_ID),
  (15, 'ENM Test Plan #5', 'ENM Test plan #5', NULL, 'test', @ENM_DROP_16_3_ID, @ENM_PM_ID),
  (16, 'OSS-RC Test Plan #1', 'OSS-RC Test Plan #1', NULL, 'test', @OSS_DROP_1_0_ID, @OSSRC_CM_ID),
  (17, 'OSS-RC Test Plan #2', 'OSS-RC Test Plan #2', NULL, 'test', @OSS_DROP_1_0_ID, @OSSRC_CM_ID),
  (18, 'OSS-RC Test Plan #3', 'OSS-RC Test Plan #3', NULL, 'test', @OSS_DROP_2_0_ID, @OSSRC_DEPLOYMENT_ID),
  (19, 'OSS-RC Test Plan #4', 'OSS-RC Test Plan #4', NULL, 'test', @OSS_DROP_2_0_ID, @OSSRC_DEPLOYMENT_ID);

SELECT `id` INTO @ENM_FM_COMPONENT_1 FROM `TECHNICAL_COMPONENTS` WHERE `name` = 'CPP MA' AND `product_feature_id` = @ENM_FM_ID;
SELECT `id` INTO @ENM_FM_COMPONENT_2 FROM `TECHNICAL_COMPONENTS` WHERE `name` = 'SNMP MA' AND `product_feature_id` = @ENM_FM_ID;
SELECT `id` INTO @ENM_FM_COMPONENT_3 FROM `TECHNICAL_COMPONENTS` WHERE `name` = 'BNSI NBI' AND `product_feature_id` = @ENM_FM_ID;

SELECT `id` INTO @ENM_CM_COMPONENT_1 FROM `TECHNICAL_COMPONENTS` WHERE `name` = 'Import' AND `product_feature_id` = @ENM_CM_ID;
SELECT `id` INTO @ENM_CM_COMPONENT_2 FROM `TECHNICAL_COMPONENTS` WHERE `name` = 'Export' AND `product_feature_id` = @ENM_CM_ID;
SELECT `id` INTO @ENM_CM_COMPONENT_3 FROM `TECHNICAL_COMPONENTS` WHERE `name` = 'Non Live Configuration Handling' AND `product_feature_id` = @ENM_CM_ID;

SELECT `id` INTO @ENM_PM_COMPONENT_1 FROM `TECHNICAL_COMPONENTS` WHERE `name` = 'PMIC: EBS-M' AND `product_feature_id` = @ENM_PM_ID;
SELECT `id` INTO @ENM_PM_COMPONENT_2 FROM `TECHNICAL_COMPONENTS` WHERE `name` = 'PMIC: EBM' AND `product_feature_id` = @ENM_PM_ID;

SELECT `id` INTO @OSSRC_CM_COMPONENT_1 FROM `TECHNICAL_COMPONENTS` WHERE `name` = 'CM-GR_CM-ERICbsmwz' AND `product_feature_id` = @OSSRC_CM_ID;
SELECT `id` INTO @OSSRC_CM_COMPONENT_2 FROM `TECHNICAL_COMPONENTS` WHERE `name` = 'CM-GR_CM-ERICbsmhw' AND `product_feature_id` = @OSSRC_CM_ID;

SELECT `id` INTO @OSSRC_DEPLOYMENT_COMPONENT_1 FROM `TECHNICAL_COMPONENTS` WHERE `name` = 'Deployment-DEPL_3PP-ERIC3pp' AND `product_feature_id` = @OSSRC_DEPLOYMENT_ID;
SELECT `id` INTO @OSSRC_DEPLOYMENT_COMPONENT_2 FROM `TECHNICAL_COMPONENTS` WHERE `name` = 'Deployment-DEPL_3PP-ERICactiviti' AND `product_feature_id` = @OSSRC_DEPLOYMENT_ID;

INSERT INTO `TEST_CAMPAIGNS_TECHNICAL_COMPONENTS` (`test_campaign_id`, `technical_component_id`) VALUES
  (11, @ENM_FM_COMPONENT_1),
  (11, @ENM_FM_COMPONENT_2),
  (11, @ENM_FM_COMPONENT_3),
  (12, @ENM_FM_COMPONENT_2),
  (12, @ENM_FM_COMPONENT_3),
  (13, @ENM_CM_COMPONENT_1),
  (14, @ENM_CM_COMPONENT_2),
  (14, @ENM_CM_COMPONENT_3),
  (15, @ENM_PM_COMPONENT_1),
  (15, @ENM_PM_COMPONENT_2),
  (16, @OSSRC_CM_COMPONENT_1),
  (17, @OSSRC_CM_COMPONENT_2),
  (18, @OSSRC_DEPLOYMENT_COMPONENT_1),
  (19, @OSSRC_DEPLOYMENT_COMPONENT_2);

