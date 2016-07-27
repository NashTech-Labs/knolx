package repo


import com.google.inject.Inject
import models.User
import play.api.Logger
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import slick.lifted.Tag
import play.api.Logger
import models.User
import java.util.Date
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.control


/**
  * Created by deepti on 22/7/16.
  */


class UserRepo @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends UserTable
  with HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  def insert(user: User): Future[Long] = {

   try {
     Logger.info("Inserting user Record.")
     db.run(UserTableQuery.returning(UserTableQuery.map(_.id)) += user)
   }
    catch {
      case ex:Exception =>
        Logger.error("Exception During insertion of user record . " + ex)
        Future{

          0
        }
    }
  }


  def getByEmailId(email: String, password: String): Future[List[User]] = {
    try {
      Logger.info("Getting user Record by Email-Id and password . ")
      db.run(UserTableQuery.filter(_.email === email).filter(_.password === password).to[List].result)
    }
    catch {
      case ex: Exception => Logger.error("Exception occurred during getting record by emailId and password : " + ex)
        Future {
          List[User]()
        }
    }
  }

  def checkEmail(email: String): Future[List[User]] = {
    try {
      Logger.info("Getting user Record by Email-Id . ")
      db.run(UserTableQuery.filter(_.email === email).to[List].result)
    }
    catch {
      case ex: Exception => Logger.error("Exception occurred during getting record by emailId: " + ex)
        Future {
          List[User]()
        }
    }
  }



  def getAll(): Future[List[User]] = {
    try {
      Logger.info("Getting all user record.")
      db.run(UserTableQuery.to[List].result)
    }
    catch {
      case ex: Exception => Logger.error("Exception in getting all user record. " + ex)
        Future {
          List[User]()
        }
    }
  }
}

trait UserTable {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import driver.api._

  lazy val UserTableQuery = TableQuery[UserInfo]

  class UserInfo(tag: Tag) extends Table[User](tag, "users") {
    implicit val dateMapper = MappedColumnType.base[java.util.Date, java.sql.Timestamp](
      d => new java.sql.Timestamp(d.getTime),
      d => new java.util.Date(d.getTime))

    def * = ( email, password, name,  designation.?,id.?) <>((User.apply _).tupled, User.unapply)

    def id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)

    def password: Rep[String] = column[String]("password", O.SqlType("VARCHAR(100"))

    def name: Rep[String] = column[String]("name", O.SqlType("VARCHAR(100"))

    // def joiningDate: Rep[String] = column[String]("joiningDate", O.SqlType("VARCHAR(100"))

    def designation: Rep[String] = column[String]("designation", O.SqlType("VARCHAR(100"))

    def emailUnique = index("email_unique_key", email, unique = true)

    def email: Rep[String] = column[String]("email", O.SqlType("VARCHAR(100"))
  }

}




