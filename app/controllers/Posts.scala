package controllers

import play.api._
import play.api.mvc._

object Posts extends Controller {

  def index = Action {
    val rss =
      <rss version="2.0">
        <channel>
          <title>Posts RSS feed</title>
          <description>Feed description</description>
          <link>http://www.example.com/rss</link>
          <lastBuildDate>Mon, 05 Oct 2012 11:12:55 =0100 </lastBuildDate>
          <pubDate>Tue, 06 Oct 2012 09:00:00 +0100</pubDate>
          {
            for (title <- List("foo", "bar", "baz")) yield {
              <item>
                <title>{ title }</title>
                <description>Item description</description>
                <link>http://www.example.com/item</link>
                <guid isPermaLink="false">123</guid>
                <pubDate>Tue, 06 Oct 2012 13:00:00 +0100</pubDate>
              </item>
            }
          }
        </channel>
      </rss>
    Ok(rss).as("application/rss+xml")
  }

}