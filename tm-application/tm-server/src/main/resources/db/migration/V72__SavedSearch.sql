CREATE TABLE IF NOT EXISTS `SAVED_SEARCH` (
  `id`                    BIGINT NOT NULL AUTO_INCREMENT,
  `name`                  VARCHAR(255) NOT NULL,
  `query`                 LONGTEXT NOT NULL,
  `profile_id`            BIGINT NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE UNIQUE INDEX `UK_SAVED_SEARCH_NAME` ON `SAVED_SEARCH` (`name` ASC, `profile_id` ASC);