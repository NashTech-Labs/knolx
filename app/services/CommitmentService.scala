package services

import javax.inject.Inject
import repo.CommitmentRepository

class CommitmentService @Inject()(commitmentRepository: CommitmentRepository) {


  def getAll ={
    commitmentRepository.getAll()
  }


}
