package repo


import com.google.inject.Inject

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}

import slick.driver.JdbcProfile
import slick.lifted.Tag

import models.{User}

import java.util.Date

import scala.concurrent.Future



/**
  * Created by deepti on 22/7/16.
  */




class UserRepo @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends UserTable
  with HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  def insert(user: User): Future[Long] = {

    db.run(UserTableQuery.returning(UserTableQuery.map(_.id)) += user)

  }

  def getByEmailId(emailId :String):Future[List[User]] = {
    db.run(UserTableQuery.filter(_.emailId === emailId).to[List].result)
  }

  def delete(id:Long):Future[Int]={

    db.run(UserTableQuery.filter(_.id === id).delete)
  }

  def getAll():Future[List[User]]={

   db.run(UserTableQuery.to[List].result)
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

    def * = (id.?, emailId, password, name, address, joiningDate.?, designation.?) <>((User.apply _).tupled, User.unapply)

    def address: Rep[String] = column[String]("address", O.SqlType("VARCHAR(100"))

    def id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)

    def password: Rep[String] = column[String]("password", O.SqlType("VARCHAR(100"))

    def name: Rep[String] = column[String]("name", O.SqlType("VARCHAR(100"))

    def joiningDate: Rep[Date] = column[Date]("joiningdate")(dateMapper)

    def designation: Rep[String] = column[String]("designation", O.SqlType("VARCHAR(100"))

    def emailUnique = index("email_unique_key", emailId, unique = true)

    def emailId: Rep[String] = column[String]("email", O.SqlType("VARCHAR(100"))
  }


}




