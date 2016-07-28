package services

import play.api.Application
import org.specs2.mutable.Specification
import play.api.test.{PlaySpecification, WithApplication}
import scala.concurrent.duration.Duration
import scala.concurrent.Await
import java.util.Date
import models.User
import org.junit.runner.RunWith
import org.openqa.selenium.remote.RemoteWebDriver.When
import org.specs2.mock.Mockito
import org.mockito.Mockito._
import repo.UserRepo
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by rahul on 27/7/16.
  */

class UserServiceSpec extends PlaySpecification with Mockito{

  val userRepo = mock[UserRepo]
  val user  = List( User("rahul@gmail.com","qwerty","rahul",Some("consultant"),Some(2)))
  val userService = new UserService(userRepo)

  "validate Email" in new WithApplication()  {
    when(userRepo.checkEmail("rahul@gmail.com")).thenReturn(Future(user) )
    val result = await(userService.validateEmail("rahul@gmail.com"))
    result === false
  }


  "validate user" in new WithApplication()  {
    when(userRepo.getByEmailAndPassword("rahul@gmail.com","qwerty")).thenReturn(Future(user) )
    val result = await(userService.validateUser("rahul@gmail.com","qwerty"))
    result === true
  }


  "signup user" in new WithApplication()  {
    when(userRepo.insert(user.head)).thenReturn(Future(1l))
    val result = await(userService.signUpUser(user.head))
    result === true
  }


}
