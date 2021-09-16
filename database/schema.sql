CREATE DATABASE IF NOT EXISTS taskdb CHARACTER SET = 'utf8mb4';
USE 'taskdb';

DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS task;
DROP TABLE IF EXISTS closing;
DROP TABLE IF EXISTS folder;
DROP TABLE IF EXISTS document;

CREATE TABLE IF NOT EXISTS users(
 id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
 username VARCHAR(100) not null,
 password VARCHAR(255) not null,
 email VARCHAR(255) not null,
 authority VARCHAR(25),
 enable BOOLEAN NOT NULL
)engine=innodb;

CREATE TABLE IF NOT EXISTS task(
id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
intitule VARCHAR(100),
debut DATE,
echeance DATE,
note VARCHAR(255),
ispriority BOOLEAN,
responsable INTEGER,
author INTEGER
)engine=innodb;

CREATE TABLE IF NOT EXISTS closing(
id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
task INTEGER,
ending DATE,
note VARCHAR(255),
isclosed BOOLEAN
)engine=innodb;

CREATE TABLE IF NOT EXISTS folder(
 id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
 name VARCHAR(50) NOT NULL,
 created DATE NOT NULL,
 parent INTEGER NOT NULL,
 personal BOOLEAN,
 author INTEGER
)engine=innodb;

CREATE TABLE IF NOT EXISTS document(
 id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
 name VARCHAR(255),
 uri VARCHAR(255),
 type VARCHAR(45),
 size INTEGER,
 file LONGBLOB,
 star BOOLEAN,
 task BOOLEAN,
 event BOOLEAN,
 personal BOOLEAN,
 parent INTEGER NOT NULL,
 created DATE,
 author INTEGER
)engine=innodb;

INSERT INTO users (username, password, email, authority, enable) VALUES
('admin', '$2a$10$aDKbye.gm0R7YPVpTI.7m.b.1MKJqC4mz4tGI1vnehGV8gNGG2bFi', 'klusseys@gmail.com', 'ADMIN' , 1),
('user', '$2a$10$RzT1o/hDFrU7zGyuuclWYeZumbnskas7U4.B1/syADNv/BARM1Ryq', 'gekone15@gmail.com', 'USER' , 1);