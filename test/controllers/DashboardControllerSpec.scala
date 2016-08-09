package controllers

import org.mockito.Mockito._
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification

import play.api.cache.CacheApi
import play.api.test.Helpers._
import play.api.test.{FakeRequest, WithApplication}

import services.{CacheService, UserService}

import scala.concurrent.Future


class DashboardControllerSpec extends Specification with Mockito {

  val userService = mock[UserService]
  val webJarAssets = mock[WebJarAssets]
  val cacheService = mock[CacheService]

  val dashBoardController = new DashboardController(cacheService, webJarAssets, userService)
  "should render the dashboard in case renderDashBoard url is hit and user doesNot logout" in new WithApplication() {
    when(cacheService.getCache).thenReturn(Some("johndeo@gmail.com"))

    when(userService.getNameAndCategoryByEmail("johndeo@gmail.com")) thenReturn Future.successful(Some("johndeo",0))
    val results = call(dashBoardController.renderDashBoard, FakeRequest(GET, "/"))
    status(results) must equalTo(OK)
    contentAsString(results).contains("knolx | DashBoard")
  }

  "should not render the dashboard in case renderDashBoard url is hit and user doesNot logout" in new WithApplication() {
    when(cacheService.getCache).thenReturn(None)
    when(userService.getNameAndCategoryByEmail("johndeo@gmail.com")) thenReturn Future.successful(None)
    val results = call(dashBoardController.renderDashBoard, FakeRequest(GET, "/"))
    status(results) must equalTo(SEE_OTHER)
    contentAsString(results).contains("knolx")
  }


}
