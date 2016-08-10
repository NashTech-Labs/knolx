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


  def createSession(kSession: KSession)= {
    kSessionRepository.insert(kSession)
  }


}
