package services

import com.google.inject.Inject
import models.KSession
import play.api.Logger
import repo.KSessionRepository
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class KSessionService @Inject()(kSessionRepository: KSessionRepository) {

  def getAll(): Future[List[KSession]] = {
    Logger.debug("Getting All KnolX.")
    kSessionRepository.getAll
  }


  /**
    * service for getting user_id and topic of KnolX by date
    */

  def getUserIDByDate(date: String): Future[List[Long]] = {

    Logger.debug("Getting user-id by date")
    kSessionRepository.getAllByDate(date).map(kSessions => kSessions.map(kSession => (kSession.uid)).distinct)

  }

  def createSession(kSession: KSession): Future[Long] = {

    kSessionRepository.insert(kSession)
  }
}




