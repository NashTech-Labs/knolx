package models


/**
  * model case classes for user
  */
case class User(email: String, password: String, name: String, designation: Option[String], id: Option[Long] = None)


