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
    val result = await(userRepository.getByEmailAndPassword("deepti@gmail.com", "cXdlcnR5"))
    result.get.email == "deepti@gmail.com"
  }

  "get user by email id " in new WithApplication() {
    val result = await(userRepository.getByEmail("deepti@gmail.com"))
    result.get.email == "deepti@gmail.com"
  }

  "insert user " in new WithApplication() {
    val result = await(userRepository.insert(User("rahul@gmail.com", "rahul1234", "rahul", Some("consultant"), Some(2))))
    result === 2
  }

  "get all users" in new WithApplication() {

    val result = await(userRepository.getAll)
    result.length === 2
  }
}
