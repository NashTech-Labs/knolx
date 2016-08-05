package controllers

import models.User

import org.specs2.mutable.Specification
import org.junit.runner._
import org.specs2.runner._
import org.mockito.Mockito._
import org.specs2.mock.Mockito

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import play.api.cache.CacheApi
import services.{CacheService, UserService}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global



@RunWith(classOf[JUnitRunner])
class AuthenticationControllerSpec extends Specification with Mockito {
  sequential


  val userService = mock[UserService]
  val webJarAssets = mock[WebJarAssets]
  val cacheService = mock[CacheService]

  val authenticationController = new AuthenticationController(cacheService, webJarAssets, userService)

  "home Controller" should {


    "signIn with valid emailId and password" in new WithApplication() {
      when(userService.validateUser("deepti@gmail.com", "cXdlcnR5")).thenReturn(Future.successful(true))
      val result = call(authenticationController.signIn,
        FakeRequest(POST, "/signIn").
          withFormUrlEncodedBody("emailId" -> "deepti@gmail.com", "password" -> "qwerty"))
      status(result) must equalTo(SEE_OTHER)
      contentAsString(result).contains("knolx | dashboard")
    }

    "not signIn with bad form" in new WithApplication() {

      when(userService.validateUser("deepti@gmail.com", "as")).thenReturn(Future.successful(false))
      val result = call(authenticationController.signIn,
        FakeRequest(POST, "/signIn").
          withFormUrlEncodedBody("emailId" -> "deepti@gmail.com", "password" -> "as"))
      status(result) must equalTo(BAD_REQUEST)

    }
    "should not signIn with invalid emailId or password" in new WithApplication() {

      when(userService.validateUser("deepti@gmail.com", "cXdlcnR5")).thenReturn(Future.successful(false))
      val result = call(authenticationController.signIn,
        FakeRequest(POST, "/signIn").
          withFormUrlEncodedBody("emailId" -> "deepti@gmail.com", "password" -> "qwerty"))
      status(result) must equalTo(SEE_OTHER)
      contentAsString(result).contains("knolx")
    }

    "should render the dashbaord in case home url is hit and user doesNot logout" in new WithApplication() {
      when(cacheService.getCache).thenReturn(Some("deepti@gmail.com"))

      when(userService.getNameByEmail("deepti@gmail.com")) thenReturn Future.successful(Some("deepti"))


      val results = call(authenticationController.loginPage, FakeRequest(GET, "/home"))
      status(results) must equalTo(OK)
      contentAsString(results).contains("knolx | DashBoard")
    }

    "should render the homepage in case home url is hit and user logout" in new WithApplication() {
      when(cacheService.getCache).thenReturn(None)

      when(userService.getNameByEmail("deepti@gmail.com")) thenReturn Future.successful(Some("deepti"))

      val results = call(authenticationController.loginPage, FakeRequest(GET, "/home"))
      status(results) must equalTo(OK)
      contentAsString(results).contains("knolx")
    }

    "should not signp if badform" in new WithApplication() {

      val results = call(authenticationController.signUp, FakeRequest(POST, "/signup").withFormUrlEncodedBody("emailId" -> "deep@gmail.com",
        "password" -> "q", "name" -> "deep", "designation" -> "sw"))
      when(userService.validateEmail("deep@gmail.com")).thenReturn(Future.successful(false))
      when(userService.signUpUser(new User("deep@gmail.com", "c", "deep", "sw", 0,None))).thenReturn(Future.successful(true))
      status(results) must equalTo(BAD_REQUEST)
      contentAsString(results).contains("knolx")
    }

    "should render DashBoard if signup is successfull" in new WithApplication() {

      val results = call(authenticationController.signUp, FakeRequest(POST, "/signup").withFormUrlEncodedBody("emailId" -> "deep@gmail.com",
        "password" -> "qwerty", "name" -> "deep", "designation" -> "sw"))
      when(userService.validateEmail("deep@gmail.com")).thenReturn(Future.successful(false))
      when(userService.signUpUser(new User("deep@gmail.com", "cXdlcnR5", "deep", "sw",0, None))).thenReturn(Future.successful(true))
      status(results) must equalTo(SEE_OTHER)
      contentAsString(results).contains("knolx | dashboard")
    }
    "should not renderDashBoard if signup is unsuccessfull" in new WithApplication() {

      val results = call(authenticationController.signUp, FakeRequest(POST, "/signup").withFormUrlEncodedBody("emailId" -> "deep@gmail.com",
        "password" -> "qwerty", "name" -> "deep", "designation" -> "sw"))
      when(userService.validateEmail("deep@gmail.com")).thenReturn(Future.successful(true))
      status(results) must equalTo(SEE_OTHER)
      contentAsString(results).contains("knolx")
    }
    "should not renderDashBoard if signup user is false" in new WithApplication() {

      val results = call(authenticationController.signUp, FakeRequest(POST, "/signup").withFormUrlEncodedBody("emailId" -> "deep@gmail.com",
        "password" -> "qwerty", "name" -> "deep", "designation" -> "sw"))
      when(userService.validateEmail("deep@gmail.com")).thenReturn(Future.successful(true))
      when(userService.signUpUser(new User("deep@gmail.com", "cXdlcnR5", "deep", "sw",0, None))).thenReturn(Future.successful(false))
      status(results) must equalTo(SEE_OTHER)
      contentAsString(results).contains("knolx")
    }
    "should be able to signout" in new WithApplication() {

      val results = call(authenticationController.signOut, FakeRequest(POST, "/signup"))
      cacheService.remove("id")
      status(results) must equalTo(SEE_OTHER)
      contentAsString(results).contains("knolx")
    }

  }

}
