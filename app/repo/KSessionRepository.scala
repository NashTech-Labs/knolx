package repo


import java.sql.Date

import com.google.inject.Inject
import play.api.Logger
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import models.KSession
import slick.lifted.ProvenShape

import scala.concurrent.Future


class KSessionRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends KSessionTable
  with HasDatabaseConfigProvider[JdbcProfile] with UserTable{

  import driver.api._

  /**
    * insert a new knolX session
    */
  def insert(kSession: KSession): Future[Long] = {
    Logger.info("Inserting KnolX session.")
    db.run(kSessionTableQuery.returning(kSessionTableQuery.map(_.id)) += kSession)
  }

  /**
    * get all KnolX sessions from database
    */
  def getAll(): Future[List[KSession]] = {
    Logger.info("Getting all KnolX session record.")
    db.run(kSessionTableQuery.to[List].result)
  }



  /**
    * get all KnolX sessions from database matching a given date
    */
  def getAllByDate(date:Date): Future[List[KSession]] = {
    Logger.info("Getting all KnolX session record by date")
    db.run(kSessionTableQuery.filter(_.date === date).to[List].result)

  }


  def getTableView: Future[List[(Option[KSession], String)]] ={
    val resultView = for{(k,u) <- kSessionTableQuery.to[List] joinRight userTableQuery.to[List] on (_.uID === _.id)}yield (k , u.email)
    db.run(resultView.to[List].result)

  }

  /**
    *delete a knolX session from database
    */

  def delete(id: Long): Future[Int] = {
    Logger.info("Deleting KnolX session record.")
    db.run(kSessionTableQuery.filter(_.id === id).delete)
  }

  /**
    *update a knolX session in database
    */
  def update(id: Long, ksession: KSession): Future[Int] = {
    Logger.info("Updating KnolX session record.")
    db.run(kSessionTableQuery.filter(_.id === id).update(ksession))
  }

  def findByIdAndTopic(id: Long,topic:String): Future[KSession] ={
    Logger.debug("find session by id and topic")
    db.run(kSessionTableQuery.filter(user=> user.id === id && user.topic=== topic ).result.head)
  }

  def getAllWithStatus: Future[List[(String, String, String, Boolean, String, Long)]] = {
    Logger.info("Getting all KnolX session with status")

    val resultView: Query[(Rep[String], Rep[String], Rep[String], Rep[Boolean], Rep[String], Rep[Long]), (String, String, String, Boolean, String, Long), List] = for {(k, u) <- kSessionTableQuery.to[List] join userTableQuery.to[List] on (_.uID === _.id)}
      yield (u.email, u.designation, u.name, k.status, k.topic,k.id)
    db.run(resultView.to[List].result)

  }

}

/**
  * KSession trait which is used for mapping
  */

trait KSessionTable extends UserTable{
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import driver.api._



  lazy val kSessionTableQuery = TableQuery[KSessionInfo]


  class KSessionInfo(tag: Tag) extends Table[KSession](tag, "sessions") {
    def * : ProvenShape[KSession] = (topic.?, date, slot, status, uID, id.?) <>((KSession.apply _).tupled, KSession.unapply)

    def id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)

    def topic: Rep[String] = column[String]("topic", O.SqlType("VARCHAR(100"))

    def date: Rep[Date] = column[Date]("date", O.SqlType("Date"))

    def slot:Rep[Int] = column[Int]("slot",O.SqlType("NUMBER"))

    def status:Rep[Boolean] = column[Boolean]("status", O.SqlType("BOOLEAN"))

    def uID: Rep[Long] = column[Long]("user_id")

    def user = foreignKey("USER_FK", uID, userTableQuery)(_.id, onUpdate = ForeignKeyAction.Restrict, onDelete = ForeignKeyAction.Cascade)


  }

}


