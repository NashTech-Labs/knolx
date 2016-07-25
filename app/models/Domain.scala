package models

import java.util.Date

/**
  * Created by deepti on 22/7/16.
  */


case class User(id: Option[Long] = None, emailId: String, password: String, name: String, address: String, joiningDate: Option[Date], designation: Option[String])

case class Login(emailId: String, password: String)



