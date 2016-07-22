package models

/**
  * Created by deepti on 22/7/16.
  */

case class SignUp (name:String,email:String,password:String , mobile:String, designation:String,dateOfJoining:String,expertise:String,aboutMe:String,id:Int)
case class SignIn(email:String,password:String)