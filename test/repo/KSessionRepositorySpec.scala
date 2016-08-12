package repo


import play.api.Application
import play.api.test.{PlaySpecification, WithApplication}
import java.sql.Date


class KSessionRepositorySpec extends PlaySpecification{
  import models.KSession
  def kSessionRepository(implicit app: Application): KSessionRepository = Application.instanceCache[KSessionRepository].apply(app)

    sequential

  "insert KnolX session " in new WithApplication() {
    val result = await(kSessionRepository.insert(KSession(Some("Generic Programming in Scala"),new Date(1472063400000L),1,false,1,Some(2))))
    result === 2
  }

  "get all KnolX sessions" in new WithApplication() {
    val result = await(kSessionRepository.getAll())
    result.size === 2
  }

  "update a KnolX session " in new WithApplication() {
    val result = await(kSessionRepository.update(2,KSession(Some("Kafka Connect"),new Date(1472063400000L),2,false,1,None)))
    result === 1
  }

  "delete a KnolX session" in new WithApplication() {
    val result = await(kSessionRepository.delete(1))
    result === 1
  }


  "get all KnolX by date" in new WithApplication() {

    val result = await(kSessionRepository.getAllByDate(new Date(1472063400000L)))
    result === List(KSession(Some("Kafka Connect"),new Date(1472063400000L),2,false,1,Some(3)))
  }
}
