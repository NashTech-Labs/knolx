package services

import com.fasterxml.jackson.databind.JsonNode
import com.google.inject.Inject

import models.User
import play.libs.Json

import repo.UserRepository

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import play.api.Logger


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

  def getAll() : Future[List[User]] ={
    userRepository.getAll.map(users => users.map(values => println(values)))
    userRepository.getAll
  }

}
