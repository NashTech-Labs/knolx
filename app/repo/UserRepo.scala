package repo


import com.google.inject.Inject

import models.User

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.Logger

import slick.driver.JdbcProfile
import slick.lifted.Tag

import scala.concurrent.Future



/**
  * Created by deepti on 22/7/16.
  */


class UserRepo @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends UserTable
  with HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  def insert(user: User): Future[Long] = {
    Logger.info("Inserting user Record.")
    db.run(userTableQuery.returning(userTableQuery.map(_.id)) += user)
  }


  def getByEmailAndPassword(email: String, password: String): Future[Option[User]] = {
    Logger.info("Getting user Record by Email-Id and password . ")
    db.run(userTableQuery.filter(user => (user.email === email) && (user.password === password)).result.headOption)
  }


  def getByEmail(email: String): Future[Option[User]] = {
    Logger.info("Getting user Record by Email-Id . ")
    db.run(userTableQuery.filter(_.email === email).result.headOption)
  }

  def getAll: Future[List[User]] = {
    Logger.info("Getting all user record.")
    db.run(userTableQuery.to[List].result)
  }
}

trait UserTable {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import driver.api._

  lazy val userTableQuery = TableQuery[UserInfo]

  class UserInfo(tag: Tag) extends Table[User](tag, "users") {
    def * = (email, password, name, designation.?, id.?) <>((User.apply _).tupled, User.unapply)

    def id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)

    def password: Rep[String] = column[String]("password", O.SqlType("VARCHAR(100"))

    def name: Rep[String] = column[String]("name", O.SqlType("VARCHAR(100"))

    def designation: Rep[String] = column[String]("designation", O.SqlType("VARCHAR(100"))

    def email: Rep[String] = column[String]("email", O.SqlType("VARCHAR(100"))

  }

}




