package services

import java.sql.Date
import javax.inject.Inject

import models.{Commitment, KSession, User}
import repo.CommitmentRepository

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class CommitmentService @Inject()(commitmentRepository: CommitmentRepository) {


  def getAll: Future[List[Commitment]] = {
    commitmentRepository.getAll()
  }

  def joinService(): Future[List[(Long, Long, String, String, Boolean, Date, Boolean)]] ={
    commitmentRepository.joinWithUser()
  }

    }


