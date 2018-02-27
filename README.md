# comment-system-play

## Setup

* Download and install sbt
* Clone the project
* Setup the database by running following script (Use MySql database)

CREATE DATABASE IF NOT EXISTS `comment_system`;
USE `comment_system`;
DROP TABLE IF EXISTS `comments`;
CREATE TABLE `comments` (
`id` int(11) NOT NULL AUTO_INCREMENT,
`parentCommentId` int(11) NOT NULL DEFAULT '0',
`author` varchar(45) NOT NULL,
`content` text NOT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;
LOCK TABLES `comments` WRITE;
INSERT INTO `comments` VALUES (1,0,'Abhishek','This is my first comment.'),(2,0,'Abhishek','This is my second comment.'),(3,1,'Chetan','This is Chetan\'s reply to Abhishek\'s first comment.'),(4,1,'Chetan','This is Chetan\'s second reply to Abhishek\'s first comment.'),(5,1,'Chetan','This is Chetan\'s third reply to Abhishek\'s first comment.'),(14,0,'Ayush','This is Ayush\'s first comment.'),(15,0,'Ankit','This is Ankit\'s first comment.'),(16,0,'Adil','This is Adil\'s first comment.'),(17,14,'Abhishek','This is Abhishek\'s reply to Ayush\' first comment.');
UNLOCK TABLES;

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
`id` int(11) NOT NULL AUTO_INCREMENT,
`name` varchar(45) DEFAULT NULL,
`active` tinyint(1) DEFAULT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
LOCK TABLES `users` WRITE;
INSERT INTO `users` VALUES (1,'Abhishek',1),(2,'Chetan',0),(3,'Ayush',0),(4,'Ankit',0),(5,'Adil',0);
UNLOCK TABLES;

## Usage

### To run the server:

* Under the root (comment-system-play) directory run `sbt ~run` or `sbt run`

Above command installs all the dependencies and JAR files for the project and starts the server.

The app will be accessible at [localhost:9000](http://localhost:9000)
