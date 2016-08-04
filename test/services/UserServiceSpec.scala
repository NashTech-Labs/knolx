package services

import models.User

import org.mockito.Mockito._
import org.specs2.mock.Mockito

import play.api.test.{PlaySpecification, WithApplication}
import play.cache.CacheApi

import repo.UserRepository

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future



class UserServiceSpec extends PlaySpecification with Mockito {

  val userRepository = mock[UserRepository]

  val user = User("rahul@gmail.com", "qwerty", "rahul", "consultant", 0,Some(2))
  val userService = new UserService(userRepository)

  "validate Email" in new WithApplication() {
    when(userRepository.getByEmail("rahul@gmail.com")).thenReturn(Future(Option(user)))
    val result = await(userService.validateEmail("rahul@gmail.com"))
    result === true
  }

  "validate user" in new WithApplication() {
    when(userRepository.getByEmailAndPassword("rahul@gmail.com", "qwerty")).thenReturn(Future(Option(user)))
    val result = await(userService.validateUser("rahul@gmail.com", "qwerty"))
    result === true
  }

  "sign up user" in new WithApplication() {
    when(userRepository.insert(user)).thenReturn(Future(1L))
    val result = await(userService.signUpUser(user))
    result === true
  }

  "get name by email" in new WithApplication() {
    when(userRepository.getByEmail("rahul@gmail.com")).thenReturn(Future(Option(user)))
    val result = await(userService.getNameByEmail("rahul@gmail.com"))
    result.get === "rahul"
  }

}
