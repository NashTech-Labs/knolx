package services


import com.google.inject.Inject

import models.User

import play.api.Logger

import repo.UserRepository

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class UserService @Inject()(userRepository: UserRepository) {

  /**
    * validate user by
    * email and password
    */


  def validateUser(emailId: String, password: String): Future[Boolean] = {
    Logger.debug("Validating User.")
    val user: Future[Option[User]] = userRepository.getByEmailAndPassword(emailId, password)
    user.map(_.isDefined)
  }

  /**
    * service for sign up user
    */

  def signUpUser(user: User): Future[Boolean] = {
    Logger.debug("signUp User")
    val recordInserted: Future[Long] = userRepository.insert(user)
    recordInserted.map(_ > 0)
  }

  /**
    * service for validating email user
    */

  def validateEmail(email: String): Future[Boolean] = {
    Logger.debug("Validating Email")
    userRepository.getByEmail(email).map(_.isDefined)
  }

  /**
    * service for getting user name and category by email
    */

  def getNameAndCategoryByEmail(email: String): Future[Option[(String, Int)]] = {
    userRepository.getByEmail(email)
      .map(user => user.map((user) => (user.name, user.category)))
  }

  def getAll(): Future[List[User]] = {
    Logger.debug("Getting All Users.")
    userRepository.getAll
  }

  def getId(email: String) = {
    userRepository.getByEmail(email).map(user => user.get.id)
  }

  def getUserByID(uidList: List[Long]): Future[List[User]] = {
    val userList = uidList.map {
      userID => userRepository.getByID(userID)
    }
    Future.sequence(userList)
  }


}
