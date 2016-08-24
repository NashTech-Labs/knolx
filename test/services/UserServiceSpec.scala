package services

import models.User
import org.mockito.Mockito._
import org.specs2.mock.Mockito
import play.api.test.{PlaySpecification, WithApplication}
import repo.UserRepository

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class UserServiceSpec extends PlaySpecification with Mockito {

  val userRepository = mock[UserRepository]

  val user1 = User("rahul@gmail.com", "cXdlcnR5", "rahul", "consultant", 0, false, Some(1))
  val user2 = User("rahul@gmail.com", "qwerty", "rahul", "consultant", 0, false, Some(2))

  val userService = new UserService(userRepository)

  "validate Email" in new WithApplication() {
    when(userRepository.getByEmail("rahul@gmail.com")).thenReturn(Future(Option(user1)))
    val result = await(userService.validateEmail("rahul@gmail.com"))
    result === true
  }

  "validate user" in new WithApplication() {
    when(userRepository.getByEmailAndPassword("rahul@gmail.com", "qwerty")).thenReturn(Future(Option(user1)))
    val result = await(userService.validateUser("rahul@gmail.com", "qwerty"))
    result === true
  }

  "sign up user" in new WithApplication() {
    when(userRepository.insert(user1)).thenReturn(Future(1L))
    val result = await(userService.signUpUser(user1))
    result === true
  }

  "get name and type by email" in new WithApplication() {
    when(userRepository.getByEmail("rahul@gmail.com")).thenReturn(Future(Option(user1)))
    val result = await(userService.getNameAndCategoryByEmail("rahul@gmail.com"))
    result.get._1 === "rahul"
    result.get._2 === 0
  }

  "get all users" in new WithApplication() {
    when(userRepository.getAll).thenReturn(Future(List(User("rahul@gmail.com", "qwerty", "rahul", "consultant", 0, false, Some(2)))))
    val result = await(userService.getAll())
    result === List(User("rahul@gmail.com", "qwerty", "rahul", "consultant", 0, false, Some(2)))
  }

  "get User by ID" in new WithApplication() {
    when(userRepository.getByID(1)).thenReturn(Future(user1))
    when(userRepository.getByID(2)).thenReturn(Future(user2))
    val result = await(userService.getUserByID(List(1, 2)))
    result === List(user1, user2)
  }

  "get ID by email" in new WithApplication() {

    when(userRepository.getByEmail("rahul@gmail.com")).thenReturn(Future(Some(User("rahul@gmail.com", "cXdlcnR5", "rahul", "consultant", 0, false, Some(1)))))
    val result = await(userService.getId("rahul@gmail.com"))
    result.get === 1
  }
}
