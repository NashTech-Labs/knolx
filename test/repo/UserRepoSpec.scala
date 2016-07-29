package repo

/**
  * Created by deepti on 26/7/16.
  */

import play.api.Application
import org.specs2.mutable.Specification
import play.api.test.{PlaySpecification, WithApplication}

import scala.concurrent.duration.Duration
import scala.concurrent.Await
import java.util.Date

import org.junit.runner.RunWith
import repo.UserRepo
import scala.concurrent.Future



class UserRepoSpec extends PlaySpecification{

  import models.User
  def userRepo(implicit app: Application) = Application.instanceCache[UserRepo].apply(app)

  "get user by email id and password" in new WithApplication()  {
    val result = await(userRepo.getByEmailAndPassword("deepti@gmail.com", "qwerty"))
    result.get.email == "deepti@gmail.com"
  }

  "get user by email id " in new WithApplication()  {
    val result = await(userRepo.getByEmail("deepti@gmail.com"))
    result.get.email == "deepti@gmail.com"
  }

  "insert user " in new WithApplication()  {
    val result = await(userRepo.insert(User("rahul@gmail.com", "rahul1234","rahul", Some("consultant"),Some(2))))
    result === 2
  }
/*
  "delete user" in new WithApplication(){
  val result = await(userRepo.delete(2))
  result === 1
}*/

  "get all users" in new WithApplication() {

    val result = await(userRepo.getAll)
    result.length === 2
  }
}
