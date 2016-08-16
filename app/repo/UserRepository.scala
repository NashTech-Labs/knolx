package repo


import com.google.inject.Inject
import models.User
import play.api.Logger
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import slick.lifted.ProvenShape
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class UserRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends UserTable

  with HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  /**
    * insert a new user
    */
  def insert(user: User): Future[Long] = {
    Logger.info("Inserting user Record.")
    db.run(userTableQuery.returning(userTableQuery.map(_.id)) += user)
  }


  /**
    * get  user email and password
    */
  def getByEmailAndPassword(email: String, password: String): Future[Option[User]] = {
    Logger.info("Getting user Record by Email-Id and password . ")
    db.run(userTableQuery.filter(user => (user.email === email) && (user.password === password)).result.headOption)
  }

  /**
    * check whether user email exists in database or not
    */
  def getByEmail(email: String): Future[Option[User]] = {
    Logger.info("Getting user Record by Email-Id . ")
    db.run(userTableQuery.filter(_.email === email).result.headOption)
  }

  /**
    * get all users from database
    */
  def getAll: Future[List[User]] = {
    Logger.info("Getting all user record.")
    db.run(userTableQuery.to[List].result)
  }

  def getById(id: Long): Future[Option[User]] = {
    db.run(userTableQuery.filter(_.id === id).result.headOption)
  }

  /**
    * delete a user from database
    */

  def delete(id: Long): Future[Int] = {
    Logger.info("Deleting user record.")
    db.run(userTableQuery.filter(_.id === id).delete)
  }

  /**
    * update a user in database
    */

  def update(id:Long,user: User):Future[Int]={
    Logger.info("Updating user record.")
    db.run(userTableQuery.filter(_.id===id).update(user))
  }

  /**
    *get a user by ID
    */
  def  getByID(id:Long):Future[User]={
    Logger.info("getting user by ID")
  db.run(userTableQuery.filter(_.id === id).to[List].result.head)

  }

}

/**
  * user trait which is used for mapping
  */
trait UserTable {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import driver.api._

  lazy val userTableQuery = TableQuery[UserInfo]

  class UserInfo(tag: Tag) extends Table[User](tag, "users") {

    def * : ProvenShape[User] = (email, password, name, designation, category, isBanned, id.?) <>((User.apply _).tupled, User.unapply)

    def id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)

    def password: Rep[String] = column[String]("password", O.SqlType("VARCHAR(100"))

    def name: Rep[String] = column[String]("name", O.SqlType("VARCHAR(100"))

    def designation: Rep[String] = column[String]("designation", O.SqlType("VARCHAR(100"))

    def category: Rep[Int] = column[Int]("category", O.Default(0))

    def isBanned : Rep[Boolean] = column[Boolean]("is_banned", O.Default(false))

    def email: Rep[String] = column[String]("email", O.SqlType("VARCHAR(100"))

  }

}




