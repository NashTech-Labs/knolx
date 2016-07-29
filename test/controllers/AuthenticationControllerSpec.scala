package controllers

import javax.inject.Inject

import org.specs2.mutable.Specification

import play.api.cache.CacheApi
import play.api.test.{FakeRequest, WithApplication}

import org.junit.runner._
import org.specs2.runner._

import play.api.test.Helpers._


import services.UserService

import org.mockito.Mockito._
import org.specs2.mock.Mockito

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by deepti on 26/7/16.
  */

@RunWith(classOf[JUnitRunner])
class AuthenticationControllerSpec extends Specification with Mockito {
  sequential


  val userService = mock[UserService]
  val webJarAssets = mock[WebJarAssets]
  val cache = mock[CacheApi]

  val authenticationController = new AuthenticationController(cache, webJarAssets, userService)

  "home Controller" should {

    "show homePage" in new WithApplication() {

      val result = route(FakeRequest(GET, "/home")).get

      status(result) must equalTo(200)
    }


    "signIn with valid emailId and password" in new WithApplication() {


      when(userService.validateUser("johndeo@gmail.com", "qwerty")).thenReturn(Future(true))
      //cache.set("id", "johndeo@gmail.com")
      val result = call(authenticationController.signIn, FakeRequest(POST, "/signIn").withFormUrlEncodedBody("emailId" -> "johndeo@gmail.com", "password" -> "qwerty"))
      status(result) must equalTo(303)
    }

    "not signIn with bad form" in new WithApplication() {

      when(userService.validateUser("johndeo@gmail.com", "as")).thenReturn(Future(false))
      val result = call(authenticationController.signIn, FakeRequest(POST, "/signIn").withFormUrlEncodedBody("emailId" -> "johndeo@gmail.com", "password" -> "as"))
      status(result) must equalTo(400)

    }
    "should not signIn with invalid emailId or password" in new WithApplication() {

      when(userService.validateUser("johndeo@gmail.com", "johndeo")).thenReturn(Future(false))
      val result = call(authenticationController.signIn, FakeRequest(POST, "/signIn").withFormUrlEncodedBody("emailId" -> "johndeo@gmail.com", "password" -> "johndeo"))
      status(result) must equalTo(303)
    }


    /* "render the homepage when cache is not set" in new WithApplication() {
       when(cache.get[String]("id")).thenReturn(None)
       val results = call(authenticationController.homePage, FakeRequest(GET, "/home"))
       status(results) must equalTo(200)
       redirectLocation(results) must beSome("/home")

     }
     "render the dashboard when cache is set" in new WithApplication() {
       cache.set("id", "johndeo@gmail.com")
       when(cache.get[String]("id")).thenReturn(Some("johndeo@gmail.com"))
       val results = call(authenticationController.homePage, FakeRequest(GET, "/home"))
       status(results) must equalTo(200)
       redirectLocation(results) must beSome("/")

     }
    "should got dashboard if signup is successfull"  in new WithApplication() {
      when(cache.get[String]("id")).thenReturn(Some("johndeo@gmail.com"))
      val results = call(authenticationController.signUp,FakeRequest(POST,"/signup").withFormUrlEncodedBody("emailId" -> "johndeo@gmail.com", "password" -> "johndeo","name"->"anubhav","designation"->"sw"))
      status(results) must equalTo(200)
    }*/

  }

}