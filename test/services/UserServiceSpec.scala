package services

import models.User

import org.mockito.Mockito._
import org.specs2.mock.Mockito

import play.api.test.{PlaySpecification, WithApplication}
import play.cache.CacheApi

import repo.UserRepo

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by rahul on 27/7/16.
  */

class UserServiceSpec extends PlaySpecification with Mockito {

  val userRepo = mock[UserRepo]

  val user = User("rahul@gmail.com", "qwerty", "rahul", Some("consultant"), Some(2))
  val userService = new UserService(userRepo)

  "validate Email" in new WithApplication() {
    when(userRepo.getByEmail("rahul@gmail.com")).thenReturn(Future(Option(user)))
    val result = await(userService.validateEmail("rahul@gmail.com"))
    result === true
  }

  "validate user" in new WithApplication() {
    when(userRepo.getByEmailAndPassword("rahul@gmail.com", "qwerty")).thenReturn(Future(Option(user)))
    val result = await(userService.validateUser("rahul@gmail.com", "qwerty"))
    result === true
  }

  "sign up user" in new WithApplication() {
    when(userRepo.insert(user)).thenReturn(Future(1L))
    val result = await(userService.signUpUser(user))
    result === true
  }

  "get name by email" in new WithApplication() {
    when(userRepo.getByEmail("rahul@gmail.com")).thenReturn(Future(Option(user)))
    val result = await(userService.getNameByEmail("rahul@gmail.com"))
    result === "rahul"
  }

}
