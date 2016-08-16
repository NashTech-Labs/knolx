package repo


import play.api.Application
import play.api.test.{PlaySpecification, WithApplication}
import java.sql.Date


class KSessionRepositorySpec extends PlaySpecification{
  import models.KSession
  def kSessionRepository(implicit app: Application): KSessionRepository = Application.instanceCache[KSessionRepository].apply(app)

    sequential

  "insert KnolX session " in new WithApplication() {
    val result = await(kSessionRepository.insert(KSession(Some("Generic Programming in Scala"),Date("2016-07-15"),1,false,1,Some(2))))
    result === 2
  }

  "get all KnolX sessions" in new WithApplication() {
    val result = await(kSessionRepository.getAll())
    result.size === 2
  }

  "update a KnolX session " in new WithApplication() {
    val result = await(kSessionRepository.update(2,KSession("Kafka Connect","22-07-2016",2,false,1,None)))
    result === 1
  }

  "delete a KnolX session" in new WithApplication() {
    val result = await(kSessionRepository.delete(1))
    result === 1
  }

}
