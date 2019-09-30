
CREATE SCHEMA IF NOT EXISTS `spring_app_db` DEFAULT CHARACTER SET utf8 ;
USE `spring_app_db` ;
CREATE TABLE IF NOT EXISTS `spring_app_db`.`stack` (
  `id` INT NOT NULL,
  `name` VARCHAR(45) NULL,
  `category` VARCHAR(45) NULL,
  `licencetype` VARCHAR(45) NULL,
  PRIMARY KEY (`id`));
  
  delete  from stack where id>1;

select * from stack where id>1;