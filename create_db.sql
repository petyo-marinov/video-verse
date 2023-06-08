-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema videoVerse
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema videoVerse
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `videoVerse` DEFAULT CHARACTER SET utf8 ;
USE `videoVerse` ;

-- -----------------------------------------------------
-- Table `videoVerse`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `videoVerse`.`users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `age` INT NOT NULL,
  `gender` VARCHAR(1) NOT NULL,
  `created_at` DATETIME NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `videoVerse`.`videos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `videoVerse`.`videos` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(145) NOT NULL,
  `url` VARCHAR(205) NOT NULL,
  `owner_id` INT NOT NULL,
  `views` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  INDEX `video_owner_fk_idx` (`owner_id` ASC) VISIBLE,
  CONSTRAINT `video_owner_fk`
    FOREIGN KEY (`owner_id`)
    REFERENCES `videoVerse`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `videoVerse`.`users_like_videos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `videoVerse`.`users_like_videos` (
  `user_id` INT NOT NULL,
  `video_id` INT NOT NULL,
  PRIMARY KEY (`user_id`, `video_id`),
  INDEX `ulv_vide_fk_idx` (`video_id` ASC) VISIBLE,
  CONSTRAINT `ulv_user_fk`
    FOREIGN KEY (`user_id`)
    REFERENCES `videoVerse`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `ulv_vide_fk`
    FOREIGN KEY (`video_id`)
    REFERENCES `videoVerse`.`videos` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `videoVerse`.`users_dislike_videos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `videoVerse`.`users_dislike_videos` (
  `user_id` INT NOT NULL,
  `video_id` INT NOT NULL,
  PRIMARY KEY (`user_id`, `video_id`),
  INDEX `udv_video_fk_idx` (`video_id` ASC) VISIBLE,
  CONSTRAINT `udv_user_fk`
    FOREIGN KEY (`user_id`)
    REFERENCES `videoVerse`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `udv_video_fk`
    FOREIGN KEY (`video_id`)
    REFERENCES `videoVerse`.`videos` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
