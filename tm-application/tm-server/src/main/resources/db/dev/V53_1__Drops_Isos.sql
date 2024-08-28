INSERT INTO `DROPS` (`id`, `name`, `product_id`) VALUES
  (1, '1.0.ENM', 2),
  (2, '1.1.ENM', 2),
  (3, '2.0.ENM', 2),
  (4, '16.3', 2),
  (5, '1.0.OSS', 3),
  (6, '2.0.OSS', 3),
  (7, '3.0.OSS', 3);

INSERT INTO `ISOS` (`id`, `name`, `version`) VALUES
  (1, 'CXP1234ISO', '1.0.1'),
  (2, 'CXP7812ISO', '2.0.2'),
  (3, 'CXP9999ISO', '3.0.3'),
  (4, 'CXP1111ISO', '0.0.1'),
  (5, 'CXP7777ISO', '0.0.1'),
  (6, 'CXP5555ISO', '5.5.5');

INSERT INTO `ISOS_DROPS` (`id`, `iso_id`, `drop_id`) VALUES
  (1, 1, 1),
  (2, 2, 1),
  (3, 3, 2),
  (4, 4, 5),
  (5, 5, 5),
  (6, 6, 6);