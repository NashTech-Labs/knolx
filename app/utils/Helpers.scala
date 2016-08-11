package utils
import java.util.{Base64, Calendar, Date}


object Helpers {

  /**
    * encode the password
    */
  def passwordEncoder(password: String): String = {
    Base64.getEncoder().withoutPadding().encodeToString(password.toString.getBytes)
  }
  def find(): java.sql.Date ={

    /**
      * finding current date and adding 15 days to it
      */

    val today = Calendar.getInstance()
    today.setTime(new Date())
    today.add(Calendar.DATE, 15)

    /**
      * converting java.util.date to java.sql.Date
      */
    new java.sql.Date(today.getTime.getTime)

  }
}
