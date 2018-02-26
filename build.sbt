name := """comment-system-play"""
organization := "com.loco"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.12.4"

libraryDependencies += guice
libraryDependencies += javaJdbc
libraryDependencies += javaCore
libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.41"
libraryDependencies += "commons-dbutils" % "commons-dbutils" % "1.6"
