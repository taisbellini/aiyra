-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema paje
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema paje
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `paje` DEFAULT CHARACTER SET utf8 ;
USE `paje` ;

-- -----------------------------------------------------
-- Table `paje`.`file`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `paje`.`file` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL,
  `comment` VARCHAR(500) NULL,
  `date` DATETIME NULL,
  PRIMARY KEY (`id`));


-- -----------------------------------------------------
-- Table `paje`.`type`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `paje`.`type` (
  `alias` VARCHAR(20) NOT NULL,
  `name` VARCHAR(50) NOT NULL,
  `depth` INT NULL,
  `parent_type_alias` VARCHAR(20) NULL,
  `file_id` INT NOT NULL,
  `start_link_type` VARCHAR(20) NULL,
  `end_link_type` VARCHAR(20) NULL,
  `color` VARCHAR(25) NULL,
  PRIMARY KEY (`alias`, `file_id`),
  INDEX `fk_type_type1_idx` (`parent_type_alias` ASC),
  INDEX `fk_type_file1_idx` (`file_id` ASC),
  INDEX `fk_type_type2_idx` (`start_link_type` ASC),
  INDEX `fk_type_type3_idx` (`end_link_type` ASC),
  CONSTRAINT `fk_type_type1`
    FOREIGN KEY (`parent_type_alias`)
    REFERENCES `paje`.`type` (`alias`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_type_file1`
    FOREIGN KEY (`file_id`)
    REFERENCES `paje`.`file` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_type_type2`
    FOREIGN KEY (`start_link_type`)
    REFERENCES `paje`.`type` (`alias`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_type_type3`
    FOREIGN KEY (`end_link_type`)
    REFERENCES `paje`.`type` (`alias`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `paje`.`container`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `paje`.`container` (
  `alias` VARCHAR(20) NOT NULL,
  `name` VARCHAR(20) NOT NULL,
  `startTime` DOUBLE NULL,
  `endTime` DOUBLE NULL,
  `parent_container_alias` VARCHAR(20) NULL,
  `type_alias` VARCHAR(20) NOT NULL,
  `file_id` INT NOT NULL,
  PRIMARY KEY (`alias`, `file_id`),
  INDEX `fk_container_container_idx` (`parent_container_alias` ASC),
  INDEX `fk_container_type1_idx` (`type_alias` ASC),
  INDEX `fk_container_file1_idx` (`file_id` ASC),
  CONSTRAINT `fk_container_container`
    FOREIGN KEY (`parent_container_alias`)
    REFERENCES `paje`.`container` (`alias`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_container_type1`
    FOREIGN KEY (`type_alias`)
    REFERENCES `paje`.`type` (`alias`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_container_file1`
    FOREIGN KEY (`file_id`)
    REFERENCES `paje`.`file` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `paje`.`value`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `paje`.`value` (
  `alias` VARCHAR(20) NOT NULL,
  `name` VARCHAR(50) NOT NULL,
  `color` VARCHAR(15) NULL,
  `type_alias` VARCHAR(20) NOT NULL,
  `type_file_id` INT NOT NULL,
  PRIMARY KEY (`alias`, `type_alias`, `type_file_id`),
  INDEX `fk_value_type1_idx` (`type_alias` ASC, `type_file_id` ASC),
  CONSTRAINT `fk_value_type1`
    FOREIGN KEY (`type_alias` , `type_file_id`)
    REFERENCES `paje`.`type` (`alias` , `file_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `paje`.`event`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `paje`.`event` (
  `time` DOUBLE NOT NULL,
  `type_alias` VARCHAR(20) NOT NULL,
  `type_file_id` INT NOT NULL,
  `container_alias` VARCHAR(20) NOT NULL,
  `value_alias` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`type_alias`, `type_file_id`, `container_alias`, `value_alias`),
  INDEX `fk_event_container1_idx` (`container_alias` ASC),
  INDEX `fk_event_value1_idx` (`value_alias` ASC),
  CONSTRAINT `fk_event_type1`
    FOREIGN KEY (`type_alias` , `type_file_id`)
    REFERENCES `paje`.`type` (`alias` , `file_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_event_container1`
    FOREIGN KEY (`container_alias`)
    REFERENCES `paje`.`container` (`alias`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_event_value1`
    FOREIGN KEY (`value_alias`)
    REFERENCES `paje`.`value` (`alias`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `paje`.`variable`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `paje`.`variable` (
  `time` DOUBLE NULL,
  `value` DOUBLE NOT NULL,
  `type_alias` VARCHAR(20) NOT NULL,
  `start_time` DOUBLE NOT NULL,
  `container_alias` VARCHAR(20) NOT NULL,
  `container_file_id` INT NOT NULL,
  `end_time` DOUBLE NOT NULL,
  PRIMARY KEY (`type_alias`, `start_time`, `container_alias`, `container_file_id`),
  INDEX `fk_variable_type1_idx` (`type_alias` ASC),
  INDEX `fk_variable_container1_idx` (`container_alias` ASC, `container_file_id` ASC),
  CONSTRAINT `fk_variable_type1`
    FOREIGN KEY (`type_alias`)
    REFERENCES `paje`.`type` (`alias`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_variable_container1`
    FOREIGN KEY (`container_alias` , `container_file_id`)
    REFERENCES `paje`.`container` (`alias` , `file_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `paje`.`state`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `paje`.`state` (
  `startTime` DOUBLE NOT NULL,
  `endTime` DOUBLE NOT NULL,
  `type_alias` VARCHAR(20) NOT NULL,
  `container_alias` VARCHAR(20) NOT NULL,
  `container_file_id` INT NOT NULL,
  `value_alias` VARCHAR(20) NOT NULL,
  `imbrication` INT NULL,
  PRIMARY KEY (`type_alias`, `container_alias`, `container_file_id`, `value_alias`, `startTime`),
  INDEX `fk_state_type1_idx` (`type_alias` ASC),
  INDEX `fk_state_container1_idx` (`container_alias` ASC, `container_file_id` ASC),
  INDEX `fk_state_value1_idx` (`value_alias` ASC),
  CONSTRAINT `fk_state_type1`
    FOREIGN KEY (`type_alias`)
    REFERENCES `paje`.`type` (`alias`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_state_container1`
    FOREIGN KEY (`container_alias` , `container_file_id`)
    REFERENCES `paje`.`container` (`alias` , `file_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_state_value1`
    FOREIGN KEY (`value_alias`)
    REFERENCES `paje`.`value` (`alias`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `paje`.`link`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `paje`.`link` (
  `start_time` DOUBLE NULL,
  `link_key` VARCHAR(50) NOT NULL,
  `type_alias` VARCHAR(20) NOT NULL,
  `type_file_id` INT NOT NULL,
  `start_container_alias` VARCHAR(20) NOT NULL,
  `end_container_alias` VARCHAR(20) NOT NULL,
  `end_time` DOUBLE NULL,
  `value_alias` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`type_alias`, `type_file_id`, `link_key`),
  INDEX `fk_link_type1_idx` (`type_alias` ASC, `type_file_id` ASC),
  INDEX `fk_link_container1_idx` (`start_container_alias` ASC),
  INDEX `fk_link_container2_idx` (`end_container_alias` ASC),
  INDEX `fk_link_value1_idx` (`value_alias` ASC),
  CONSTRAINT `fk_link_type1`
    FOREIGN KEY (`type_alias` , `type_file_id`)
    REFERENCES `paje`.`type` (`alias` , `file_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_link_container1`
    FOREIGN KEY (`start_container_alias`)
    REFERENCES `paje`.`container` (`alias`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_link_container2`
    FOREIGN KEY (`end_container_alias`)
    REFERENCES `paje`.`container` (`alias`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_link_value1`
    FOREIGN KEY (`value_alias`)
    REFERENCES `paje`.`value` (`alias`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
