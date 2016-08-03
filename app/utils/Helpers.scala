package utils

import java.util.Base64


object Helpers {

  def passwordEncoder(password: String): String = {
    Base64.getEncoder().withoutPadding().encodeToString(password.toString.getBytes)
  }

}
