package repo


import com.google.inject.Inject
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import models._

import scala.concurrent.Future

/**
  * Created by deepti on 22/7/16.
  */
class UserRepo {

}
trait UserTable  {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import driver.api._

  class UserTable(tag:Tag) extends Table[SignUp](tag,"users"){
    val id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    val name = column[String]("name", O.SqlType("VARCHAR(20)"))
    val email = column[String]("email", O.SqlType("VARCHAR(20)"))
    val password = column[String]("password", O.SqlType("VARCHAR(20)"))
    val mobile = column[String]("mobile", O.SqlType("VARCHAR(10)"))
    val designation = column[String]("designation", O.SqlType("VARCHAR(20)"))
    val dateOfJoining = column[String]("dateOfJoining", O.SqlType("VARCHAR(20)"))
    val expertise = column[String]("expertise", O.SqlType("VARCHAR(20)"))
    val aboutMe = column[String]("aboutMe", O.SqlType("VARCHAR(20)"))
    def * = (name,email,password,mobile,designation,dateOfJoining,expertise,aboutMe,id) <>(SignUp.tupled, SignUp.unapply)

  }
  val userTable = TableQuery[UserTable]
  
}

