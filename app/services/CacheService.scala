package services

import com.google.inject.Inject

import play.api.cache.CacheApi


class CacheService @Inject()(cache: CacheApi) {

  def setCache(key: String, value: Any): Unit = cache.set(key, value)

  def getCache: Option[String] = cache.get[String]("id")

  def remove(key: String): Unit = cache.remove(key)
}
