package services

import com.google.inject.Inject
import models.KSession
import play.api.Logger
import repo.KSessionRepository

import scala.concurrent.Future


/**
  * Created by rahul on 4/8/16.
  */
class KSessionService @Inject()(kSessionRepository: KSessionRepository) {

  def getAll(): Future[List[KSession]] = {
    Logger.debug("Getiing All Users.")
    kSessionRepository.getAll
  }

  def createSession(kSession: KSession)={
    kSessionRepository.insert(kSession)
  }
}
