package repo

import com.google.inject.Inject
import models.{Commitment}
import play.api.Logger
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import slick.lifted.ProvenShape

import scala.concurrent.Future


class CommitmentRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends CommitmentTable
  with HasDatabaseConfigProvider[JdbcProfile] with UserTable {

  import driver.api._

  def insert(commitment: Commitment): Future[Long] = {
    Logger.info("Inserting Commitment session.")
    db.run(commitmentTableQuerry.returning(commitmentTableQuerry.map(_.id)) += commitment)
  }

  /**
    * get all commitment from database
    */
  def getAll(): Future[List[Commitment]] = {
    Logger.info("Getting all Commitment record.")
    db.run(commitmentTableQuerry.to[List].result)
  }

  /**
    * get commitment from database by id
    */
  def getById(id: Long): Future[Commitment] = {
    Logger.info("Getting Commitment record by id.")
    db.run(commitmentTableQuerry.filter(_.id === id).to[List].result.head)
  }

  /**
    * delete a commitment from database
    */

  def delete(id: Long): Future[Int] = {
    Logger.info("Deleting commitment record.")
    db.run(commitmentTableQuerry.filter(_.id === id).delete)
  }

  /**
    * update a commitment in database
    */
  def update(id: Long, commitment: Commitment): Future[Int] = {
    Logger.info("Updating commitment record.")
    db.run(commitmentTableQuerry.filter(_.id === id).update(commitment))
  }

}

trait CommitmentTable extends UserTable {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import driver.api._


  lazy val commitmentTableQuerry = TableQuery[CommitmentInfo]


  class CommitmentInfo(tag: Tag) extends Table[Commitment](tag, "commitment") {
    def * : ProvenShape[Commitment] = (uid, commit, done, id.?) <>((Commitment.apply _).tupled, Commitment.unapply)

    def id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)

    def commit: Rep[Long] = column[Long]("commit")

    def done: Rep[Long] = column[Long]("done")

    def uid: Rep[Long] = column[Long]("uid")

    def user = foreignKey("USER_FK", uid, userTableQuery)(_.id, onUpdate = ForeignKeyAction.Restrict, onDelete = ForeignKeyAction.Cascade)

  }

}
