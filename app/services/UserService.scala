package services
import com.google.inject.Inject
import play.api.db.slick.DatabaseConfigProvider
import repo.UserRepo
import scala.concurrent.Future
import sun.font.TrueTypeFont
import scala.concurrent.ExecutionContext.Implicits.global
/**
  * Created by deepti on 25/7/16.
  */
 class UserService@Inject()(userRepo:UserRepo) {


  def validateUser(emailId: String, password: String): Future[Boolean] = {
    val userList = userRepo.getByEmailId(emailId, password)
    userList.map(value => if (value.length == 1) true else false)

  }
}
