package utils
import java.util.{Base64, Calendar, Date}


object Helpers {

  /**
    * encode the password
    */
  def passwordEncoder(password: String): String = {
    Base64.getEncoder().withoutPadding().encodeToString(password.toString.getBytes)
  }

  /**
    * finding current date and adding 15 days to it
    * converting java.util.date to java.sql.Date
    */

  def find(): java.sql.Date ={


    val today = Calendar.getInstance()
    today.setTime(new Date())
    today.add(Calendar.DATE, 15)
    new java.sql.Date(today.getTime.getTime)

  }
}
