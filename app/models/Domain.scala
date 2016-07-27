package models


/**
  * Created by deepti on 22/7/16.
  */


case class User( emailId: String, password: String, name: String,designation: Option[String],id: Option[Long])

case class Login(emailId: String, password: String)



