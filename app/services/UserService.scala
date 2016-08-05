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
    * service for getting user name by email
    */

  def getNameByEmail(email: String): Future[Option[String]] = {
    val user: Future[Option[User]] = userRepository.getByEmail(email)
    user.map(value => value.map(_.name))
  }

  def getAll(): Future[List[User]] = {
    Logger.debug("Getiing All Users.")
    userRepository.getAll.map(values => values.map(user => println(user)))
    userRepository.getAll
  }

}
