
package services
import java.sql.Date

import models.Commitment
import org.mockito.Mockito._
import org.specs2.mock.Mockito
import play.api.test.{PlaySpecification, WithApplication}
import repo.CommitmentRepository

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class CommitmentServiceSpec extends PlaySpecification with Mockito{

  val commitmentRepository = mock[CommitmentRepository]
  val commitmentService = new CommitmentService(commitmentRepository)
  val commitment = Commitment(1, 5, 0, Some(1))

  "get all users' commitment" in new WithApplication() {
    when(commitmentRepository.getAll()).thenReturn(Future(List(commitment)))
    val result = await(commitmentService.getAll)
    result == List(commitment)
  }

  "join commitment with users and ksessions" in new WithApplication(){

    when(commitmentRepository.joinWithUserAndKSession()).thenReturn(Future(List()))
    val result = await(commitmentService.joinService())
    result === List()

  }

}

