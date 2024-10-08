-- Connect components to products
ALTER TABLE `TECHNICAL_COMPONENTS` ADD COLUMN `product_id` BIGINT NULL DEFAULT NULL;
ALTER TABLE `TECHNICAL_COMPONENTS_AUD` ADD COLUMN `product_id` BIGINT NULL DEFAULT NULL;

ALTER TABLE `TECHNICAL_COMPONENTS` ADD CONSTRAINT `FK_TECHNICAL_COMPONENTS_PRODUCTS`
FOREIGN KEY (`product_id`)
REFERENCES `PRODUCTS` (`id`)
  ON DELETE RESTRICT
  ON UPDATE RESTRICT;

-- Populate components table
INSERT INTO `TECHNICAL_COMPONENTS` (`product_id`, `name`)
VALUES

  -- OSSRC
  (3, 'PM-PMS-ERICpms'),
  (3, 'PM-PMS-ERICpmscfg'),
  (3, 'PM-PMS-ERICpmtrace'),
  (3, 'PM-PMS-ERICftpm'),
  (3, 'PM-PMS -ERICpmcs'),
  (3, 'PM-PMS-ERICpmnm'),
  (3, 'CoreNetwork-CN_NSD-ERICcpsnsd'),
  (3, 'CoreNetwork-CN_NSD-ERICepcmpep'),
  (3, 'CoreNetwork-CN_NSD-ERICepcsd'),
  (3, 'CoreNetwork-CN_NSD-ERICepcpcpb'),
  (3, 'CoreNetwork-CN_MMA-ERICcnmscad'),
  (3, 'CoreNetwork-CN_ PSSNAD-ERICcnpssnad'),
  (3, 'CoreNetwork-CN_GMA-ERICcngma'),
  (3, 'CoreNetwork-CN_NSD-ERICepchck'),
  (3, 'CoreNetwork-CN_NSD-ERICepcdc'),
  (3, 'CoreNetwork-CN_NMA-ERICcnma'),
  (3, 'Platform-PF_SAPP_AXS-ERICfeam'),
  (3, 'Platform-PF_UPG_SYS-ERICusck'),
  (3, 'Platform-PF_SCK-ERICaxe'),
  (3, 'Platform-PF_SCK-ERICcore'),
  (3, 'Platform-PF_SCK-ECONFsystem'),
  (3, 'Platform-PF_SCK-ERICsck'),
  (3, 'Platform-PF_SCK-ECONFbase'),
  (3, 'Platform-PF_SCK-ERICpeer'),
  (3, 'Platform-PF_SAPP_FWK-ERICarne'),
  (3, 'Platform-PF_SAPP_FWK-ERICfwcfg'),
  (3, 'Deployment-DEPL_3PP-ERIC3pp'),
  (3, 'Deployment-DEPL_3PP-ERICactiviti'),
  (3, 'Deployment-DEPL_3PP-ERICactmq'),
  (3, 'Deployment-DEPL_3PP-ERICadvt'),
  (3, 'Deployment-DEPL_3PP-ERICalex'),
  (3, 'Deployment-DEPL_3PP-ERICbrlndi386'),
  (3, 'Deployment-DEPL_3PP-ERICbrlndSPARC'),
  (3, 'Deployment-DEPL_3PP-ERICftmoc'),
  (3, 'Deployment-DEPL_3PP-ERICgfish'),
  (3, 'Deployment-DEPL_3PP-ERICj2ssh'),
  (3, 'Deployment-DEPL_3PP-ERICjdk16'),
  (3, 'Deployment-DEPL_3PP-ERICjdk17'),
  (3, 'Deployment-DEPL_3PP-ERICjpyth'),
  (3, 'Deployment-DEPL_3PP-ERICjre13109'),
  (3, 'Deployment-DEPL_3PP-ERICjre142'),
  (3, 'Deployment-DEPL_3PP-ERICjre15'),
  (3, 'Deployment-DEPL_3PP-ERICjre1607'),
  (3, 'Deployment-DEPL_3PP-ERICmisc3pp'),
  (3, 'Deployment-DEPL_3PP-ERICmisc3ppi386'),
  (3, 'Deployment-DEPL_3PP-ERICmisc3ppSPARC'),
  (3, 'Deployment-DEPL_3PP-ERICmiscOSGI3pp'),
  (3, 'Deployment-DEPL_3PP-ERICns478'),
  (3, 'Deployment-DEPL_3PP-ERICopendj'),
  (3, 'Deployment-DEPL_3PP-ERICopenf'),
  (3, 'Deployment-DEPL_3PP-ERICopenldap'),
  (3, 'Deployment-DEPL_3PP-ERICoro'),
  (3, 'Deployment-DEPL_3PP-ERICosgifwk'),
  (3, 'Deployment-DEPL_3PP-ERICsnlm'),
  (3, 'Deployment-DEPL_3PP-ERICsyb'),
  (3, 'Deployment-DEPL_3PP-ERICvrsnt'),
  (3, 'Deployment-DEPL_3PP-EXTRactmq'),
  (3, 'Deployment-DEPL_3PP-EXTRadvt'),
  (3, 'Deployment-DEPL_3PP-EXTRalex'),
  (3, 'Deployment-DEPL_3PP-EXTRbrlndi386'),
  (3, 'Deployment-DEPL_3PP-EXTRbrlndSPARC'),
  (3, 'Deployment-DEPL_3PP-EXTRftomc'),
  (3, 'Deployment-DEPL_3PP-EXTRgfish'),
  (3, 'Deployment-DEPL_3PP-EXTRj2ssh'),
  (3, 'Deployment-DEPL_3PP-EXTRjdk16'),
  (3, 'Deployment-DEPL_3PP-EXTRjdk17'),
  (3, 'Deployment-DEPL_3PP-EXTRjre13101'),
  (3, 'Deployment-DEPL_3PP-EXTRjre13109'),
  (3, 'Deployment-DEPL_3PP-EXTRjre142'),
  (3, 'Deployment-DEPL_3PP-EXTRjre15'),
  (3, 'Deployment-DEPL_3PP-EXTrjre1607'),
  (3, 'Deployment-DEPL_3PP-EXTRjypth'),
  (3, 'Deployment-DEPL_3PP-EXTRmisc3pp'),
  (3, 'Deployment-DEPL_3PP-EXTRmisc3ppi86'),
  (3, 'Deployment-DEPL_3PP-EXTRmiscOSGI3pp'),
  (3, 'Deployment-DEPL_3PP-EXTRns478 '),
  (3, 'Deployment-DEPL_3PP-EXTRopendj'),
  (3, 'Deployment-DEPL_3PP-EXTRopenf'),
  (3, 'Deployment-DEPL_3PP-EXTRopenldap'),
  (3, 'Deployment-DEPL_3PP-EXTRoro'),
  (3, 'Deployment-DEPL_3PP-EXTRsnlm'),
  (3, 'Deployment-DEPL_3PP-EXTRsyb'),
  (3, 'Deployment-DEPL_3PP-EXTRvrsnt'),
  (3, 'Deployment-DEPL_DDC-ERICddc'),
  (3, 'Deployment-DEPL_INFRA-ERICdmr'),
  (3, 'Deployment-DEPL_INFRA-ERICsct'),
  (3, 'Platform-DEPL_MON-ERICtcko'),
  (3, 'Platform-COMINF-DVD_ISO'),
  (3, 'Platform-COMINF-ERICocs'),
  (3, 'Platform-COMINF-ERICsdse'),
  (3, 'Platform-COMINF-ERICbismrsmc'),
  (3, 'Platform-SECURITY-DVD_ISO'),
  (3, 'Platform-SECURITY-ERICsinst'),
  (3, 'Platform-SECURITY-ERICcadm'),
  (3, 'Platform-SECURITY-ERICcaudb'),
  (3, 'Platform-SECURITY-ERICcsa'),
  (3, 'Platform-SECURITY-ERICracrs'),
  (3, 'Platform-SECURITY-ERICsls'),
  (3, 'Platform-SECURITY-ERICsuti'),
  (3, 'Platform-SECURITY-ERICaiws'),
  (3, 'Platform-SECURITY-ERICscs'),
  (3, 'Platform-SECURITY-ERICpss'),
  (3, 'Platform-SECURITY-ERICsecpf'),
  (3, 'Platform-SECURITY-ERICftss'),
  (3, 'SwManagment-CCSHM_NSS-ERICshm'),
  (3, 'PM-CCOPT_EBA-ERICeba86'),
  (3, 'PM-CCOPT_EBA-ERICebsg'),
  (3, 'PM-CCOPT_EBA-ERICffaxblr'),
  (3, 'PM-CCOPT_EBA-ERICebss'),
  (3, 'PM-CCOPT_EBA-ERICffax'),
  (3, 'PM-CCOPT_EBA-ERICrtt'),
  (3, 'PM-CCOPT_EBA-ERICrpdbi'),
  (3, 'PM-CCOPT_EBA-ERICebsw'),
  (3, 'PM-CCOPT_EBA-ERICrpmo'),
  (3, 'PM-CCOPT_EBA-ERICeba'),
  (3, 'PM-CCOPT_RNO-ERICgeow'),
  (3, 'PM-CCOPT_RNO-ERICcce'),
  (3, 'PM-CCOPT_RNO-ERICffaxw'),
  (3, 'PM-CCOPT_RNO-ERICwmrr'),
  (3, 'PM-CCOPT_RNO-ERICrndbi'),
  (3, 'PM-CCOPT_RNO-ERICwncs'),
  (3, 'PM-CCOPT_RNO-ERICncs'),
  (3, 'PM-CCOPT_RNO-ERICnox'),
  (3, 'PM-CCOPT_RNO-ERICgwncs'),
  (3, 'PM-CCOPT_RNO-ERICmrr'),
  (3, 'PM-CCOPT_RNO-ERICfox'),
  (3, 'PM-CCOPT_RNO-ERICbrf'),
  (3, 'PM-CCOPT_RNO-ERICfas'),
  (3, 'SwManagment-CCSHM-ERICsmo'),
  (3, 'SwManagment-CCSHM-ERICops'),
  (3, 'SwManagment-CCSHM-ERIChc'),
  (3, 'SwManagment-CCSHM-ERICfha'),
  (3, 'FM-CCFM-ERICfmcor'),
  (3, 'FM-CCFM-ERICfmali'),
  (3, 'FM-CCFM-ERICfmba'),
  (3, 'FM-CCFM-ERICfmual'),
  (3, 'FM-CCFM-ERICfmav'),
  (3, 'FM-CCFM-ERICtxf'),
  (3, 'FM-CCFM-ERICbnsm'),
  (3, 'FM-CCFM-ERICbnsa'),
  (3, 'FM-CCFM-ERICsnmps'),
  (3, 'FM-CCFM-ERICcirpm'),
  (3, 'FM-CCFM-ERICfmaa'),
  (3, 'FM-CCFM-ERICmpc'),
  (3, 'FM-CCFM-ERICrtxf'),
  (3, 'FM-CCFM-ERICrcnv'),
  (3, 'FM-CCFM-ERICfgran'),
  (3, 'FM-CCFM-ERICfmaxe'),
  (3, 'FM-CCFM-ERICfmoss'),
  (3, 'FM-CCFM-ERICfmtmp'),
  (3, 'FM-CCFM-ERICftrap'),
  (3, 'FM-CCFM-ERICfmisi'),
  (3, 'FM-CCFM-ERICfmsegw'),
  (3, 'FM-CCFM-ERICfmomp'),
  (3, 'FM-CCFM-ERICfmspn'),
  (3, 'FM-CCFM-ERICfmivr'),
  (3, 'FM-CCFM-ERICfmocmp'),
  (3, 'FM-CCFM-ERICfmvivr'),
  (3, 'FM-CCFM-ERICipms'),
  (3, 'FM-CCFM-ERICfmdmx'),
  (3, 'FM-CCFM-ERICfwlnta'),
  (3, 'FM-CCFM-ERICfmcaau'),
  (3, 'FM-CCFM-ERICfmesax'),
  (3, 'FM-CCFM-ERICfaosmc'),
  (3, 'FM-CCFM-ERICfmmlink'),
  (3, 'FM-CCFM-ERICfmpci'),
  (3, 'FM-CCFM-ERICfmcaa'),
  (3, 'FM-CCFM-ERICfmpscor'),
  (3, 'FM-CCFM-ERICfmcomecim'),
  (3, 'FM-CCFM-ERICftxf'),
  (3, 'FM-CCFM-ERICfmwifi'),
  (3, 'FM-CCFM-ERICfmsdc'),
  (3, 'FM-CCFM-ERICfmecm'),
  (3, 'FM-CCFM-ERICfwtmp'),
  (3, 'FM-CCFM-ERICtea'),
  (3, 'FM-CCFM-ERICaeh'),
  (3, 'CM-GR_CM-ERICbsmwz'),
  (3, 'CM-GR_CM-ERICbsmhw'),
  (3, 'CM-GR_CM-ERICbsmcm'),
  (3, 'CM-GR_CM-ERICbsmam'),
  (3, 'CM-WLR_CM_RPS-ERICrps'),
  (3, 'CM-WLR_CM_CEX-ERICurcex'),
  (3, 'CM-APROV_BSIM-ERICbsim'),
  (3, 'CM-APROV_TENG-ERICteng'),
  (3, 'CM-WLR_CM_CONFIG_CMS-ERICurcms'),
  (3, 'CM-WLR_CM_CONFIG_CMS-ERICplgns'),
  (3, 'CM-WLR_CM_CONFIG_BCG-ERICurbcg'),
  (3, 'CM-WLR_CM_CONFIG_PCA-ERICurpca'),
  (3, 'CM-WLR_CM_CONFIG_FALLBACK-ERICurfbk'),
  (3, 'CM-WLR_CM_CONFIG_MOM-ERICurwmom'),
  (3, 'CM-WLR_CM_CONFIG_CSMP-ERICfmm'),
  (3, 'CM-WLR_CM_CONFIG_CSMP-ERICfcs'),
  (3, 'CM-WLR_CM_CONFIG_CSMP-ERICfmp'),
  (3, 'CM-WLR_CM_APPS_CC-ERICrestcm'),
  (3, 'CM-WLR_CM_CONFIG_SAPP_EWA-ERICfewa'),
  (3, 'CM-WLR_CM_APPS_RRPM-ERICrrpm'),
  (3, 'CM-WLR_CM_APPS_PCI-ERICpcisel')

ON DUPLICATE KEY UPDATE
  `product_id` = VALUES(`product_id`);

-- Move components components without product links to test fields
INSERT INTO `TEST_FIELDS` (`deleted`, `field`, `name`, `value`, `test_case_id`)
    SELECT cp.`deleted`, 'STRING', 'COMPONENT', cp.`name`, tcv.`id`
    FROM `TEST_CASE_VERSIONS` AS tcv
    LEFT JOIN `TECHNICAL_COMPONENTS` AS cp ON tcv.`technical_component_id` = cp.`id`
    WHERE cp.`product_id` IS NULL;

UPDATE `TEST_CASE_VERSIONS` SET `technical_component_id` = NULL
WHERE `id` IN (
    -- H2 doesn't support joins in update query
    -- MySQL cannot update a table and select from the same table in a direct subquery
    SELECT tmp.`id` FROM (
      SELECT tcv.`id` FROM `TEST_CASE_VERSIONS` as tcv
      LEFT JOIN `TECHNICAL_COMPONENTS` AS cp ON tcv.`technical_component_id` = cp.`id`
      WHERE cp.`product_id` IS NULL
    ) AS tmp
);

DELETE FROM `TECHNICAL_COMPONENTS` WHERE `product_id` IS NULL;

-- Fix component-product relation constraints
ALTER TABLE `TECHNICAL_COMPONENTS` DROP INDEX `UK_TECHNICAL_COMPONENTS`;
ALTER TABLE `TECHNICAL_COMPONENTS` MODIFY COLUMN `product_id` BIGINT NOT NULL;
CREATE UNIQUE INDEX `UK_TECHNICAL_COMPONENTS` ON `TECHNICAL_COMPONENTS` (`name` ASC, `product_id` ASC);
