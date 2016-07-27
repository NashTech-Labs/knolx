package services

import com.google.inject.Inject

import models.User

import repo.UserRepo

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import play.api.Logger

import utils.Helpers

/**
  * Created by deepti on 25/7/16.
  */
class UserService @Inject()(userRepo: UserRepo) {


  def addUser(emailId: String, password: String): Future[Boolean] = {
    Logger.debug("Validating User.")
    val userList = userRepo.getByEmailAndPassword(emailId, password)
    userList.map(value => if (value.length == 1) true else false)
  }

  def signUpUser(user: User): Future[Boolean] = {
    Logger.debug("signUp User")
    val recordInserted = userRepo.insert(user)
    recordInserted.map(value => if (value > 0) true else false)

  }

  def validateEmail(email: String): Future[Boolean] = {
    Logger.debug("Validating Email")
    userRepo.checkEmail(email).map(user => if (user.isEmpty) true else false)
  }

  def encodePassword(userData: User, password: String): User = {
    val encodedUserdata = userData.copy(emailId = userData.emailId, password = Helpers.passwordEncoder(password), name = userData.name, designation = userData.designation, id = userData.id)
    encodedUserdata

  }


}
