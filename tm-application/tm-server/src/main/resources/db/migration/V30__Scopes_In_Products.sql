-- Connect scopes to products
ALTER TABLE `SCOPES` ADD COLUMN `product_id` BIGINT NULL DEFAULT NULL;
ALTER TABLE `SCOPES_AUD` ADD COLUMN `product_id` BIGINT NULL DEFAULT NULL;

ALTER TABLE `SCOPES` ADD CONSTRAINT `FK_SCOPES_PRODUCTS`
FOREIGN KEY (`product_id`)
REFERENCES `PRODUCTS` (`id`)
  ON DELETE RESTRICT
  ON UPDATE RESTRICT;

-- Mark existing groups as disabled to hide them from UI
ALTER TABLE `SCOPES` ADD COLUMN `enabled` BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE `SCOPES_AUD` ADD COLUMN `enabled` BOOLEAN NOT NULL DEFAULT FALSE;

-- Populate scopes table
INSERT INTO `SCOPES` (`product_id`, `enabled`, `name`)
VALUES
-- Global
  (NULL, TRUE, 'KGB'),
  (NULL, TRUE, 'VCDB'),
  (NULL, TRUE, 'RNCDB'),
  (NULL, TRUE, 'GAT'),

-- ENM
  (2, TRUE, 'Robustness'),
  (2, TRUE, 'RFA'),
  (2, TRUE, 'CDB'),
  (2, TRUE, 'RVB'),
  (2, TRUE, 'Stonetablet_KPI'),
  (2, TRUE, 'KPI'),
  (2, TRUE, 'Local'),
  (2, TRUE, 'Performance Test'),
  (2, TRUE, 'Duration Test'),
  (2, TRUE, 'Upgrade'),
  (2, TRUE, 'Backup_Restore'),

-- OSSRC
  (3, TRUE, 'STCDB')

ON DUPLICATE KEY UPDATE
  `product_id` = VALUES(`product_id`),
  `enabled`    = VALUES(`enabled`);
