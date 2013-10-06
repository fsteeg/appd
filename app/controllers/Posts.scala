package controllers

import play.api._
import play.api.mvc._

object Posts extends Controller {

  def index = Action {
    Ok(Notes.notesAsRss(Application.data("posts/"))).as("application/rss+xml")
  }

}