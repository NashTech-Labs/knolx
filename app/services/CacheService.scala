package services

import com.google.inject.Inject
import play.api.cache.CacheApi


/**
  * Created by knoldus on 30/7/16.
  */
class CacheService @Inject()(cache: CacheApi) {

  def setCache(key: String, value: Any):Unit = cache.set(key, value)

  def isUserLogOut:Option[String] = cache.get[String]("id")

  def remove(key: String):Unit = cache.remove(key)
}
