package services

import com.google.inject.Inject
import models.User
import repo.UserRepo
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.Logger


class UserService @Inject()(userRepo: UserRepo) {


  def validateUser(emailId: String, password: String): Future[Boolean] = {
    Logger.debug("Validating User.")
    val user:Future[Option[User]] = userRepo.getByEmailAndPassword(emailId, password)
    user.map(user =>
      if (user.isDefined) true else false)
  }

  def signUpUser(user: User): Future[Boolean] = {
    Logger.debug("signUp User")
    val recordInserted:Future[Long] = userRepo.insert(user)
    recordInserted.map(noOfInsertedRecord => if (noOfInsertedRecord > 0) true else false)
  }

  def validateEmail(email: String): Future[Boolean] = {
    Logger.debug("Validating Email")
    userRepo.getByEmail(email).map(email => if (email.isDefined) true else false)
  }

  def getNameByEmail(email: String): Future[String] = {
    val user:Future[Option[User]] = userRepo.getByEmail(email)
    user.map(value => value.map(user => user.name).get)
  }

}
