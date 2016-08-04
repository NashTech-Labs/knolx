package repo

import com.google.inject.Inject
import play.api.Logger
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import models.KSession

import scala.concurrent.Future


class KSessionRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends KSessionTable
  with HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  /**
    * insert a new knolx session
    */
  def insert(kSession: KSession): Future[Long] = {
    Logger.info("Inserting KnolX session.")
    db.run(kSessionTableQuery.returning(kSessionTableQuery.map(_.id)) += kSession)
  }

  /**
    * get all KnolX sessions from databse
    */
  def getAll(): Future[List[KSession]] = {
    Logger.info("Getting all KnolX session record.")
    db.run(kSessionTableQuery.to[List].result)
  }

  /**
    * delete a knolx session from databse
    */

  def delete(id: Long): Future[Int] = {
    Logger.info("Deleting KnolX session record.")
    db.run(kSessionTableQuery.filter(_.id === id).delete)
  }

  /**
    * update a knolx session in databse
    */
  def update(id: Long, ksession: KSession): Future[Int] = {
    Logger.info("Updating KnolX session record.")
    db.run(kSessionTableQuery.filter(_.id === id).update(ksession))
  }

}

trait KSessionTable extends UserTable {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import driver.api._

  lazy val kSessionTableQuery = TableQuery[KSessionInfo]


  class KSessionInfo(tag: Tag) extends Table[KSession](tag, "sessions") {
    def * = (topic, date, uID, id.?) <>((KSession.apply _).tupled, KSession.unapply)

    def id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)

    def topic: Rep[String] = column[String]("topic", O.SqlType("VARCHAR(100"))

    def date: Rep[String] = column[String]("date", O.SqlType("VARCHAR(100"))

    def uID: Rep[Long] = column[Long]("user_id")

    def user = foreignKey("USER_FK", uID, userTableQuery)(_.id, onUpdate = ForeignKeyAction.Restrict, onDelete = ForeignKeyAction.Cascade)


  }

}

