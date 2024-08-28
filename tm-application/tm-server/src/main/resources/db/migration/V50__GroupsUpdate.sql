ALTER TABLE `SCOPES` DROP INDEX `UK_SCOPES`;
CREATE UNIQUE INDEX `UK_SCOPES_PRODUCTS` ON `SCOPES` (`name`, `product_id` ASC);

INSERT INTO `SCOPES` (`product_id`, `enabled`, `name`)
VALUES
-- Assure
(5, TRUE, 'Robustness'),
  (5, TRUE, 'RFA'),
  (5, TRUE, 'CDB'),
  (5, TRUE, 'RVB'),
  (5, TRUE, 'Stonetablet_KPI'),
  (5, TRUE, 'KPI'),
  (5, TRUE, 'Local'),
  (5, TRUE, 'Performance Test'),
  (5, TRUE, 'Duration Test'),
  (5, TRUE, 'Upgrade'),
  (5, TRUE, 'Backup_Restore'),
  (5, TRUE, 'REAL NODE'),
  (5, TRUE, 'G1'),
  (5, TRUE, 'G2'),
-- ENM
  (2, TRUE, 'REAL NODE'),
  (2, TRUE, 'G1'),
  (2, TRUE, 'G2');
