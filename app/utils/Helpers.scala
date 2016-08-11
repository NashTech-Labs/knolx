package utils
import java.text.SimpleDateFormat
import java.util.{Calendar, Date}
import java.util.Base64


object Helpers {

  /**
    * encode the password
    */
  def passwordEncoder(password: String): String = {
    Base64.getEncoder().withoutPadding().encodeToString(password.toString.getBytes)
  }
  def find(): String ={

    /**
      * finding current date and adding 15 days to it
      */

    val today = Calendar.getInstance()
    today.setTime(new Date())
    today.add(Calendar.DATE, 15)

    /**
      * converting java.util.date to string
      */

     new SimpleDateFormat("yyyy-MM-dd").format(today.getTime)
  }
}
