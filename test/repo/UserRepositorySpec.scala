package repo

import play.api.Application
import play.api.test.{PlaySpecification, WithApplication}
import scala.concurrent.duration.Duration
import scala.concurrent.Await
import scala.concurrent.Future


class UserRepositorySpec extends PlaySpecification {

  import models.User

  def userRepository(implicit app: Application): UserRepository = Application.instanceCache[UserRepository].apply(app)

  "get user by email id and password" in new WithApplication() {
    val result = await(userRepository.getByEmailAndPassword("admin@gmail.com", "cXdlcnR5"))
    result.get.email == "admin@gmail.com"
  }

  "get user by email id " in new WithApplication() {
    val result = await(userRepository.getByEmail("admin@gmail.com"))
    result.get.email == "admin@gmail.com"
  }

  "insert user " in new WithApplication() {
    val result = await(userRepository.insert(User("rahul@gmail.com", "rahul1234", "rahul", "consultant",0,false, Some(3))))
    result === 3
  }

  "get all users" in new WithApplication() {
    val result = await(userRepository.getAll)
    result.length === 3
  }


  "delete a user" in new WithApplication() {
    val result = await(userRepository.delete(3))
    result === 1
  }


  "update a user" in new WithApplication() {
    val result = await(userRepository.update(3, User("anubhav@gmail.com","anubhav1234","anubhav","consultant",0,false)))
    result === 0
  }

  "get user by ID" in new WithApplication() {

    val result = await(userRepository.getByID(1))
    result === User("admin@gmail.com","cXdlcnR5","admin","consultant",0,false,Some(1))
  }
}
