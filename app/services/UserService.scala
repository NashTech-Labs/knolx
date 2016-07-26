package services

import com.google.inject.Inject
import models.User
import play.api.db.slick.DatabaseConfigProvider
import repo.UserRepo

import scala.concurrent.Future
import sun.font.TrueTypeFont

import scala.concurrent.ExecutionContext.Implicits.global
import play.api.Logger

/**
  * Created by deepti on 25/7/16.
  */
class UserService @Inject()(userRepo: UserRepo) {


  def validateUser(emailId: String, password: String): Future[Boolean] = {
    Logger.debug("Validating User.")
    val userList = userRepo.getByEmailId(emailId, password)
    userList.map(value => if (value.length == 1) true else false)
  }

  def signUpUser(user: User): Future[Boolean] = {
    Logger.debug("signup User")
      val recordInserted = userRepo.insert(user)
      recordInserted.map ( value => if (value > 0)   true   else  false   )

  }
}
