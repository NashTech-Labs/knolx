package services

import java.sql.Date

import com.google.inject.Inject
import models.{KSessionView, KSession}
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


  /**
    * service for getting user_id and topic of KnolX by date
    */

  def getUserIDByDate(date: Date): Future[List[Long]] = {

    Logger.debug("Getting user-id by date")
    kSessionRepository.getAllByDate(date).map(kSessions => kSessions.map(kSession => (kSession.uid)).distinct)

  }



  def upDateSessionStatus(id: Long,topic:String): Future[Int] = {
    Logger.debug("update status of user")
    getUserKsession(id,topic).flatMap (kSession =>
      kSessionRepository.update(id,kSession.copy
      (topic = kSession.topic,date = kSession.date,
        slot=kSession.slot,status = true,uid = kSession.uid)))
  }

  def delete(id:Long): Future[Int] = {
    kSessionRepository.delete(id)
  }

  def createSession(kSession: KSession): Future[Long] = {

    kSessionRepository.insert(kSession)
  }

  def getUserKsession(id: Long,topic:String): Future[KSession] ={
    Logger.debug(" searching for ksession")
    kSessionRepository.findByIdAndTopic(id,topic)
  }

  def getAllUserList: Future[List[(String, String, String, Boolean, String, Long)]] ={
    kSessionRepository.getAllWithStatus
  }

}

