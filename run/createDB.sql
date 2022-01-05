-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema whatsupp_db
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema whatsupp_db
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `whatsupp_db` DEFAULT CHARACTER SET utf8 ;
USE `whatsupp_db` ;

-- -----------------------------------------------------
-- Table `whatsupp_db`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `whatsupp_db`.`users` (
  `user_id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `status` INT NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE INDEX `userID_UNIQUE` (`user_id` ASC) VISIBLE,
  UNIQUE INDEX `username_UNIQUE` (`username` ASC) VISIBLE,
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 0
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `whatsupp_db`.`groups`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `whatsupp_db`.`groups` (
  `group_id` INT NOT NULL AUTO_INCREMENT,
  `admin_user_id` INT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`group_id`, `admin_user_id`),
  UNIQUE INDEX `group_id_UNIQUE` (`group_id` ASC) VISIBLE,
  INDEX `admin_user_id_idx` (`admin_user_id` ASC) VISIBLE,
  CONSTRAINT `admin_user_id`
    FOREIGN KEY (`admin_user_id`)
    REFERENCES `whatsupp_db`.`users` (`user_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 0
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `whatsupp_db`.`messages`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `whatsupp_db`.`messages` (
  `message_id` INT NOT NULL AUTO_INCREMENT,
  `user_id_from` INT NOT NULL,
  `text` VARCHAR(45) NULL DEFAULT NULL,
  `sent_time` TIMESTAMP NOT NULL,
  `user_id_to` INT NULL DEFAULT NULL,
  `group_id` INT NULL DEFAULT NULL,
  PRIMARY KEY (`message_id`, `user_id_from`),
  UNIQUE INDEX `message_id_UNIQUE` (`message_id` ASC) VISIBLE,
  INDEX `user_id_from_idx` (`user_id_from` ASC) VISIBLE,
  INDEX `user_id_to_idx` (`user_id_to` ASC) VISIBLE,
  INDEX `group_id_idx` (`group_id` ASC) VISIBLE,
  CONSTRAINT `group_id`
    FOREIGN KEY (`group_id`)
    REFERENCES `whatsupp_db`.`groups` (`group_id`),
  CONSTRAINT `user_id_from`
    FOREIGN KEY (`user_id_from`)
    REFERENCES `whatsupp_db`.`users` (`user_id`),
  CONSTRAINT `user_id_to`
    FOREIGN KEY (`user_id_to`)
    REFERENCES `whatsupp_db`.`users` (`user_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 0
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `whatsupp_db`.`files`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `whatsupp_db`.`files` (
  `files_id` INT NOT NULL AUTO_INCREMENT,
  `message_id` INT NOT NULL,
  `file_path` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`files_id`, `message_id`),
  UNIQUE INDEX `files_id_UNIQUE` (`files_id` ASC) VISIBLE,
  INDEX `message_id_idx` (`message_id` ASC) VISIBLE,
  CONSTRAINT `message_id`
    FOREIGN KEY (`message_id`)
    REFERENCES `whatsupp_db`.`messages` (`message_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 0
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `whatsupp_db`.`friends_requests`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `whatsupp_db`.`friends_requests` (
  `requester_user_id` INT NOT NULL,
  `friend_user_id` INT NOT NULL,
  `request_time` TIMESTAMP NOT NULL,
  `request_status` INT NOT NULL,
  `answer_time` TIMESTAMP NULL DEFAULT NULL,
  PRIMARY KEY (`requester_user_id`, `friend_user_id`),
  INDEX `user_id_idx` (`requester_user_id` ASC) VISIBLE,
  INDEX `friend_user_id_idx` (`friend_user_id` ASC) VISIBLE,
  CONSTRAINT `friend_user_id`
    FOREIGN KEY (`friend_user_id`)
    REFERENCES `whatsupp_db`.`users` (`user_id`),
  CONSTRAINT `user_id`
    FOREIGN KEY (`requester_user_id`)
    REFERENCES `whatsupp_db`.`users` (`user_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `whatsupp_db`.`group_requests`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `whatsupp_db`.`group_requests` (
  `requester_user_id` INT NOT NULL,
  `group_id` INT NOT NULL,
  `request_time` TIMESTAMP NOT NULL,
  `request_status` INT NOT NULL DEFAULT '0',
  `answer_time` TIMESTAMP NULL DEFAULT NULL,
  PRIMARY KEY (`requester_user_id`, `group_id`),
  INDEX `requester_user_id_idx` (`requester_user_id` ASC) VISIBLE,
  INDEX `group_id_idx` (`group_id` ASC) VISIBLE,
  CONSTRAINT `group_id_group_requests`
    FOREIGN KEY (`group_id`)
    REFERENCES `whatsupp_db`.`groups` (`group_id`),
  CONSTRAINT `requester_user_id`
    FOREIGN KEY (`requester_user_id`)
    REFERENCES `whatsupp_db`.`users` (`user_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

DROP USER IF EXISTS 'tester'@'%';
CREATE USER IF NOT EXISTS 'tester'@'%' IDENTIFIED BY 'password-123';
GRANT SELECT, INSERT, UPDATE, DELETE ON *.* TO `tester`@`%`;

DROP USER IF EXISTS 'tester'@'localhost';
CREATE USER IF NOT EXISTS `tester`@`localhost` IDENTIFIED BY 'password-123';
GRANT SELECT, INSERT, UPDATE, DELETE ON *.* TO 'tester'@'localhost';

FLUSH privileges;
