package utils

import java.util.Base64

/**
  * Created by knoldus on 27/7/16.
  */
object Helpers {

  def passwordEncoder(password: String): String = {
    Base64.getEncoder().withoutPadding().encodeToString(password.toString.getBytes)
  }

}
