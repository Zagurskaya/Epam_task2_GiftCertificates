-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema jd2_project_week2
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `jd2_project_week2` ;

-- -----------------------------------------------------
-- Schema jd2_project_week2
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `jd2_project_week2` DEFAULT CHARACTER SET utf8 ;
USE `jd2_project_week2` ;

-- -----------------------------------------------------
-- Table `jd2_project_week2`.`document`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `jd2_project_week2`.`document` (
  `id` INT(100) NOT NULL AUTO_INCREMENT,
  `uniqueNumber` VARCHAR(100) NOT NULL,
  `description` VARCHAR(100) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
