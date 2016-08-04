package models


/**
  * model case classes for user
  */
case class User(email: String, password: String, name: String, designation: String, category: Int, id: Option[Long] = None)

case class KSession(topic: String, date: String, uid: Long, id: Option[Long] = None)
