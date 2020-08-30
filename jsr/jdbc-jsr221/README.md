
`mysql -uroot`

```sql
CREATE DATABASE test DEFAULT CHARACTER SET utf8mb4;

CREATE DATABASE test2 DEFAULT CHARACTER SET utf8mb4;

CREATE USER 'deploy'@'localhost' IDENTIFIED BY 'Hello@123456';

GRANT ALL PRIVILEGES ON test.* TO 'deploy'@'localhost';
GRANT ALL PRIVILEGES ON test2.* TO 'deploy'@'localhost';
FLUSH PRIVILEGES;

SHOW GRANTS FOR 'deploy'@'localhost';

USE `test`;
CREATE TABLE `account_from`
(
    `id`    bigint  NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `money` bigint  NOT NULL COMMENT '金额',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;

INSERT INTO `account_from`(`id`, `money`) VALUE(1, 100);


USE `test2`;
CREATE TABLE `account_to`
(
    `id`    bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `money` bigint  NOT NULL COMMENT '金额',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;

INSERT INTO `account_to`(`id`, `money`) VALUE(1, 0);

```