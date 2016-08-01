package services

import models.User

import org.mockito.Mockito._
import org.specs2.mock.Mockito

import play.api.test.{PlaySpecification, WithApplication}
import play.api.cache.CacheApi

import repo.UserRepo

import scala.concurrent.Future


/**
  * Created by knoldus on 1/8/16.
  */
class CacheServiceSpec extends PlaySpecification with Mockito {

  val cache = mock[CacheApi]
  val cacheService = new CacheService(cache)
  "check for get method of cache service" in new WithApplication() {
    cacheService.setCache("id", "deep@gmail.com")
    when(cache.get[String]("id")).thenReturn(Some("deep@gmail.com"))
    val res: Option[String] = cacheService.getCache
    res === Some("deep@gmail.com")
  }

}
