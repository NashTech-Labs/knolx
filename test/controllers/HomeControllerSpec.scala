package controllers

import javax.inject.Inject

import org.specs2.mutable.Specification
import play.api.cache.CacheApi
import play.api.test.{FakeRequest, WithApplication}
import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.test.Helpers._
import play.api.test._
import services.UserService
import org.mockito.Mockito._
import org.specs2.mock.Mockito
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by deepti on 26/7/16.
  */

@RunWith(classOf[JUnitRunner])
class HomeControllerSpec extends Specification with Mockito{
  sequential


  val userService = mock[UserService]
  val webJarAssets = mock[WebJarAssets]
  val cache = mock[CacheApi]

  val homeController = new HomeController(cache,webJarAssets,userService)

  "home Controller" should{

    "show homePage" in new WithApplication() {

      val result = route(FakeRequest(GET,"/home")).get

      status(result) must equalTo(200)
    }
  }

  "should signIn with valid emailId and password" in new WithApplication() {

    when(cache.get[String]("johndeo@gmail.com")).thenReturn(Some("abc"))
    when(userService.validateUser("johndeo@gmail.com", "qwerty")).thenReturn(Future(true))
    val result = call(homeController.signIn, FakeRequest(POST,"/signIn").withFormUrlEncodedBody("emailId"->"johndeo@gmail.com" ,"password"-> "qwerty").withSession("id" -> "johndeo@gmail.com"))
    status(result) must equalTo(303)
  }
  "should not signIn with bad form" in new WithApplication() {
    when(cache.get[String]("johndeo@gmail.com")).thenReturn(Some("abc"))
    when(userService.validateUser("johndeo@gmail.com", "as")).thenReturn(Future(false))
    val result = call(homeController.signIn, FakeRequest(POST,"/signIn").withFormUrlEncodedBody("emailId"->"johndeo@gmail.com" ,"password"-> "as").withSession("id" -> "johndeo@gmail.com"))
    status(result) must equalTo(400)

  }
  "should not signIn with invalid emailId or password" in new WithApplication() {
    when(userService.validateUser("johndeo@gmail.com", "johndeo")).thenReturn(Future(false))
    val result = call(homeController.signIn, FakeRequest(POST, "/signIn").withFormUrlEncodedBody("emailId" -> "johndeo@gmail.com", "password" -> "johndeo"))
    status(result) must equalTo(303)
  }

   "should signOut" in new WithApplication() {
    val result = route(FakeRequest(GET,"/signOut")).get
    status(result) must equalTo(303)
  }

}
