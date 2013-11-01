package controllers

import play.api._
import play.api.mvc._
import com.typesafe.config.Config
import java.net.URLConnection
import java.net.HttpURLConnection
import java.net.URL
import scala.xml.Elem

object Application extends Controller {

  def index = Action { implicit request =>
    Ok(views.html.index(
      loadRss(List(
        "http://www.bibsonomy.org/rss/user/fsteeg",
        "http://www.bibsonomy.org/publrss/user/fsteeg",
        "http://fsteeg.com/feed",
        "https://github.com/fsteeg",
        "http://stackoverflow.com/feeds/user/18154",
        routes.Posts.index.absoluteURL())),
      loadRss(List(
        routes.Notes.index.absoluteURL()))))
  }

  def data(kind: String): String = {
    val default = "test/data/"
    (Play.maybeApplication match {
      case Some(application) => application.configuration.getString("appd.data").getOrElse(default)
      case None => default
    }) + kind
  }

  private def contentType(urlConnection: URLConnection) = {
    urlConnection.setRequestProperty("Accept", "application/rss+xml, application/atom+xml")
    val contentType = urlConnection.getContentType().split(";")(0)
    Logger.trace(s"Getting content type '$contentType' from '${urlConnection.getURL}'")
    contentType
  }

  def loadRss(feeds: List[String]): List[(Elem, String)] = {
    for (
      feed <- feeds;
      urlConnection = new URL(feed).openConnection();
      content = contentType(urlConnection);
      if (List("text/xml", "application/xml", "application/rss+xml", "application/atom+xml") contains content)
    ) yield (scala.xml.XML.load(urlConnection.getInputStream), content)
  }
}