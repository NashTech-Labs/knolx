package services

import java.sql.Date
import com.google.inject.Inject
import models.{KSession, KSessionView}
import play.api.Logger

import repo.{UserRepository, KSessionRepository}

import repo.KSessionRepository

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future



class KSessionService @Inject()(kSessionRepository: KSessionRepository, userRepository: UserRepository) {


  def getAll(): Future[List[KSession]] = {
    Logger.debug("Getting All KnolX.")
    kSessionRepository.getAll
  }



  def createView: Future[List[KSessionView]] = {
    kSessionRepository.getTableView.map(views => views.map(v => KSessionView(v._1.get.topic, v._1.get.date, v._1.get.slot, v._1.get.status, v._1.get.id, v._2)))
  }

  /**
    * service for getting user_id and topic of KnolX by date
    */

  def getUserIDByDate(date: Date): Future[List[Long]] = {

    Logger.debug("Getting user-id by date")
    kSessionRepository.getAllByDate(date).map(kSessions => kSessions.map(kSession => (kSession.uid)).distinct)

  }

  def upDateSession(kSession: KSession) = {
    kSessionRepository.update(kSession.id.get, kSession)
  }


  def createSession(kSession: KSession): Future[Long] = {

    kSessionRepository.insert(kSession)
  }

}

