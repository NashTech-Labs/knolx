package models

import java.sql.Date


/**
  * model case classes for user and KnolX session
  */
case class User(email: String, password: String, name: String, designation: String, category: Int, isBanned:Boolean, id: Option[Long] = None)

case class KSession(topic:Option[String], date: Date, slot: Int, status: Boolean , uid: Long, id: Option[Long] = None)
