package models

import java.sql.Date

/**
  * model case classes for user, KnolX session, KSessionView and Commitment
  */

case class User(email: String, password: String, name: String, designation: String, category: Int, isBanned:Boolean, id: Option[Long] = None)

case class KSession(topic:Option[String], date: Date, slot: Int, status: Boolean , uid: Long, id: Option[Long] = None)

case class KSessionView(topic:Option[String], date: Date, slot: Int, status: Boolean , id: Option[Long],email: String)

case class Commitment(uid : Long,commit : Long ,done :Long, id: Option[Long])
