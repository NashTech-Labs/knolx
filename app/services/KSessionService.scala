package services

import java.sql.Date

import com.google.inject.Inject
import models.{KSessionView, KSession}
import play.api.Logger
<<<<<<< HEAD

import repo.{UserRepository, KSessionRepository}

import repo.KSessionRepository

=======
import repo.{UserRepository, KSessionRepository}
>>>>>>> a6852dbe3f3e770962934928b006857bca1169b5
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


<<<<<<< HEAD
class KSessionService @Inject()(kSessionRepository: KSessionRepository, userRepository: UserRepository) {
=======
class KSessionService @Inject()(kSessionRepository: KSessionRepository) {
>>>>>>> a6852dbe3f3e770962934928b006857bca1169b5

  def getAll(): Future[List[KSession]] = {
    Logger.debug("Getting All KnolX.")
    kSessionRepository.getAll
  }

  def createViewByDate(date: Date): Future[List[KSessionView]] = {
    kSessionRepository.getAllByDate(date).map(views => views.map(v => KSessionView(v._1.get.topic, v._1.get.date, v._1.get.slot, v._1.get.status, v._1.get.id, v._2)))
  }

  def createView: Future[List[KSessionView]] = {
    kSessionRepository.getTableView.map(views => views.map(v => KSessionView(v._1.get.topic, v._1.get.date, v._1.get.slot, v._1.get.status, v._1.get.id, v._2)))
  }

  /**
    * service for getting user_id and topic of KnolX by date
    */

  def getUserIDByDate(date: Date): Future[List[Long]] = {

    Logger.debug("Getting user-id by date")
    kSessionRepository.getAllByDate(date).map(kSessions => kSessions.map(kSession => (kSession._1.get.uid)).distinct)

  }

<<<<<<< HEAD
  def upDateSession(kSession: KSession) = {
    kSessionRepository.update(kSession.id.get, kSession)
  }
=======
>>>>>>> a6852dbe3f3e770962934928b006857bca1169b5

  def createSession(kSession: KSession): Future[Long] = {

    kSessionRepository.insert(kSession)
  }
<<<<<<< HEAD
=======
  def upDateSession(kSession: KSession): Future[Int] ={

    kSessionRepository.update(kSession.id.get,kSession)
  }
>>>>>>> a6852dbe3f3e770962934928b006857bca1169b5
}

