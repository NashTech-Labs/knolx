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
    * service for getting user_id and topic of KnolX
    */

  def getTopicAndUserIDByDate(): Future[List[(String, Long)]] = {

    Logger.debug("Getting topic and user-id by date")
    kSessionRepository.getAll.map(value => value.map(value => (value.topic, value.uid)))

  }
}
