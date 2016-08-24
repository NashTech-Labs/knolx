package repo

import java.sql.Date

import models.{Commitment, KSession}
import play.api.Application
import play.api.test.{PlaySpecification, WithApplication}


class CommitmentRepositorySpec extends PlaySpecification{

  def commitmentRepository(implicit app: Application): CommitmentRepository = Application.instanceCache[CommitmentRepository].apply(app)


  "insert a user's commitment " in new WithApplication() {
    val result = await(commitmentRepository.insert(Commitment(2,5,0,Some(2))))
    result === 2
  }

  "update a user's commitment " in new WithApplication() {
    val result = await(commitmentRepository.update(2,Commitment(2,5,1,Some(2))))
    result === 1
  }

  "get all users' commitment " in new WithApplication() {
    val result = await(commitmentRepository.getAll())
    result.length === 2
  }

  "delete a user's commitment " in new WithApplication() {
    val result = await(commitmentRepository.delete(2))
    result === 1
  }

  "join with user" in new WithApplication() {
    val result = await(commitmentRepository.joinWithUserAndKSession())
    result ===  List(("admin@gmail.com","admin",Some(KSession(Some("Spark"),new Date(1472063400000L),1,false,1,Some(1)))))
  }

}
