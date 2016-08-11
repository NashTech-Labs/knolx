package services

import com.google.inject.Inject
import models.{KSessionView, KSession}
import play.api.Logger
import repo.{UserRepository, KSessionRepository}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class KSessionService @Inject()(kSessionRepository: KSessionRepository,userRepository: UserRepository) {

  def getAll(): Future[List[KSession]] = {
    Logger.debug("Getting All KnolX.")
    kSessionRepository.getAll
  }

  def createView: Future[List[KSessionView]] ={
    kSessionRepository.getTableView.map(views => views.map(v => KSessionView(v._1.get.topic,v._1.get.date,v._1.get.slot,v._1.get.status,v._1.get.id,v._2)))
  }

  def createSession(kSession: KSession)= {
    kSessionRepository.insert(kSession)
  }

  def upDateSession(kSession: KSession) ={
    println("\n\n\nDate:"+kSession.date+" id:"+kSession.id+" slot:"+kSession.slot+" status:"+kSession.status+" topic:"+kSession.topic+kSession.uid)
    kSessionRepository.update(kSession.id.get,kSession)
  }

}

