package controllers

import play.api._
import play.api.mvc._

object Projects extends Controller {

  def index = Action {
    Ok(<h1>Your projects</h1>).as("text/html")
  }

}