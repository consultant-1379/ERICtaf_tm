UPDATE `USERS` SET external_email = 'taf@ericsson.com' WHERE id = 1;
UPDATE `USERS` SET external_name = 'tafUser' WHERE id = 1;
UPDATE `USERS` SET external_surname = 'tafSurname' WHERE id = 1;


-- Insert new user
INSERT INTO `USERS`(`id`, `external_id`, `external_email`, `external_name`, `external_surname` ) VALUES
  (2, 'taf2', 'taf@ericsson.com', 'tafUser2', 'tafSurname2' );