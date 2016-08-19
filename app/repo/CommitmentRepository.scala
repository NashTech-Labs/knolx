package repo

import java.sql.Date

import com.google.inject.Inject
import models.{Commitment, KSession, User}
import play.api.Logger
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import slick.lifted.ProvenShape

import scala.concurrent.Future


class CommitmentRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends CommitmentTable
  with HasDatabaseConfigProvider[JdbcProfile] with UserTable with KSessionTable{

  import driver.api._

  def insert(commitment: Commitment): Future[Long] = {
    Logger.info("Inserting Commitment session.")
    db.run(commitmentTableQuery.returning(commitmentTableQuery.map(_.id)) += commitment)
  }

  /**
    * get all commitment from database
    */
  def getAll(): Future[List[Commitment]] = {
    Logger.info("Getting all Commitment record.")
    db.run(commitmentTableQuery.to[List].result)
  }

  /**
    *delete a commitment from database
    */

  def delete(id: Long): Future[Int] = {
    Logger.info("Deleting commitment record.")
    db.run(commitmentTableQuery.filter(_.id === id).delete)
  }

  /**
    *update a commitment in database
    */
  def update(id: Long, commitment: Commitment): Future[Int] = {
    Logger.info("Updating commitment record.")
    db.run(commitmentTableQuery.filter(_.id === id).update(commitment))
  }

def joinWithUser(): Future[List[(Long, Long, String, String, Boolean, Date, Boolean)]] = {

  val findCommit = commitmentTableQuery.filter(value => value.commit > value.done)
  val isBanned = userTableQuery.filter(_.isBanned === false)
  val notDone = kSessionTableQuery.filter(_.status === false)
  val joinQuery = for {
    ((commitment, user),kSession) <- findCommit.to[List] join isBanned.to[List] on (_.uid === _.id) join notDone on (_._1.id === _.uID)
  } yield(commitment.uid, user.id,user.email,user.name,user.isBanned,kSession.date,kSession.status)

  db.run(joinQuery.to[List].result)
}

}

trait CommitmentTable extends UserTable{
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import driver.api._


  lazy val commitmentTableQuery = TableQuery[CommitmentInfo]


  class CommitmentInfo(tag: Tag) extends Table[Commitment](tag, "commitment") {
    def * : ProvenShape[Commitment] = (uid, commit, done, id.?) <>((Commitment.apply _).tupled, Commitment.unapply)

    def id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)

    def commit: Rep[Long] = column[Long]("commit")

    def done: Rep[Long] = column[Long]("done")

    def uid: Rep[Long] = column[Long]("uid")

    def idxuid = index("idx_uid", uid , unique = true)

    def user = foreignKey("USER_FK", uid, userTableQuery)(_.id, onUpdate = ForeignKeyAction.Restrict, onDelete = ForeignKeyAction.Cascade)

  }

}
