package services

import javax.inject.Inject
import models.Commitment
import repo.CommitmentRepository

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class CommitmentService @Inject()(commitmentRepository: CommitmentRepository) {


  def updateCommitment(uid: Long): Future[Int] = {
    commitmentRepository.getById(uid).flatMap(commit => commitmentRepository.update(commit.id.get, Commitment(uid, commit.commit, commit.done + 1, commit.id)))
  }


}
