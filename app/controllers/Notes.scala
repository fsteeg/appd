package controllers

import play.api._
import play.api.mvc._
import java.io.File
import scala.io.Source
import org.clapper.markwrap.MarkWrap
import org.clapper.markwrap.MarkupType
import java.text.SimpleDateFormat
import java.text.DateFormat
import java.util.Locale

object Notes extends Controller {

  val rssDateFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss Z")

  def index = Action {
    Ok(notesAsRss(Application.data("notes/"))).as("application/rss+xml")
  }

  def notesAsRss(root: String) = {
    val data = (loadAllFiles(root) sortBy (_._1))(Ordering[String].reverse)
    Logger.trace("data: " + data.toList)
    rssFor(data).toString
  }

  def loadAllFiles(root: String) = {
    for (file <- new File(root).listFiles if !file.isHidden()) yield {
      Logger.trace("Loading file: " + file)
      val source = Source.fromFile(file).mkString
      (file.getName().split("\\.")(0), parseToHtml(source))
    }
  }

  def parseToHtml(source: String) =
    MarkWrap.parserFor(MarkupType.Textile).parseToHTML(source)

  def rssFor(data: Seq[(String, String)]) = {
    val latestRssTime = parseToRssTime(data.head._1)
    <rss version="2.0">
      <channel>
        <title>Notes RSS feed</title>
        <description>Feed description</description>
        <link>http://www.example.com/rss</link>
        <lastBuildDate>{ latestRssTime }</lastBuildDate>
        <pubDate>{ latestRssTime }</pubDate>
        {
          for ((time, text) <- data; rssTime = parseToRssTime(time)) yield {
            <item>
              <title>{ rssTime }</title>
              <description>{ text }</description>
              <link>http://www.example.com/item</link>
              <guid isPermaLink="false">{ rssTime }</guid>
              <pubDate>{ rssTime }</pubDate>
            </item>
          }
        }
      </channel>
    </rss>
  }

  def parseToRssTime(time: String) = {
    val inputFormat = new SimpleDateFormat("yyyy-mm-dd")
    val outputFormat = rssDateFormat
    outputFormat.format(inputFormat.parse(time))
  }

}