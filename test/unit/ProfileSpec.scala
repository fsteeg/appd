package unit

import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json.Json
import play.api.libs.json.Json._
import play.api.libs.json.JsValue
import models.Job
import models.User

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class ProfileSpec extends Specification {

  "Profile" should {

    "render the index page" in new WithApplication {
      val home = route(FakeRequest(GET, "/profile")).get

      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
      contentAsString(home) must contain("fsteeg")
      contentAsString(home) must contain("Tätigkeit")
      contentAsString(home) must contain("hbz")
    }
  }

  "Data" should {

    val password = "password"

      val userJson = Json.toJson(Map(
      "id" -> toJson("fsteeg@gmail.com"),
      "attributes" -> toJson(Map(
        "name" -> toJson("Fabian"),
        "mail" -> toJson("fsteeg@gmail.com"),
        "password" -> toJson(password),
        "expertise" -> toJson("software construction"))),
      "priorities" -> toJson(List(
        Map("label" -> "Ort", "comment" -> "Vor Ort, mit dem Rad erreichbar"),
        Map("label" -> "Tätigkeit", "comment" -> "Was man konkret tut (Entwickeln) und nicht tut (Meetings, Reisen)"),
        Map("label" -> "Perspektive", "comment" -> "Langfristige Perspektive"),
        Map("label" -> "Arbeitsbed.", "comment" -> "Arbeitszeiten, Büros, Ausstattung, etc."))),
      "skills" -> toJson(List(
        Map("label" -> "Java", "comment" -> "Java, 10 Jahre"),
        Map("label" -> "Eclipse", "comment" -> "Eclipse, 8 Jahre")))))

    val jobJson = Map(
      "id" -> toJson("hbz"),
      "attributes" -> toJson(Map("name" -> toJson("hbz"), "user" -> toJson("fsteeg@gmail.com"))),
      "links" -> toJson(List(Map("label" -> "homepage", "url" -> "http://www.hbz-nrw.de"))),
      "location" -> toJson(Map("lon" -> 6.935512, "lat" -> 50.934060)),
      "priorities" -> toJson(Map(
        "Ort" -> toJson(Map("score" -> toJson("9"), "comment" -> toJson("Köln"))),
        "Tätigkeit" -> toJson(Map("score" -> toJson("9"), "comment" -> toJson("Software-Entwickler, z.T. viel orgn. und fachl."))),
        "Perspektive" -> toJson(Map("score" -> toJson("9"), "comment" -> toJson("Grundlage: feste Stelle öD; nur zunächst befristet"))),
        "Arbeitsbed." -> toJson(Map("score" -> toJson("8"), "comment" -> toJson("ruhig, 1-2 Personen/Büro, gute Ausstattung"))))),
      "skills" -> toJson(Map(
        "Java" -> toJson(Map("score" -> toJson("9"), "comment" -> toJson("Java"))),
        "Eclipse" -> toJson(Map("score" -> toJson("6"), "comment" -> toJson("MF-IDE"))))))

    "be accessible via user objects" in {
      val user = User(userJson)
      user.id mustEqual "fsteeg@gmail.com"
      user.attributes mustEqual Map(
        "name" -> "Fabian",
        "mail" -> "fsteeg@gmail.com",
        "password" -> password,
        "expertise" -> "software construction")
      user.priorities mustEqual List(
        Map("label" -> "Ort",
          "comment" -> "Vor Ort, mit dem Rad erreichbar"),
        Map("label" -> "Tätigkeit",
          "comment" -> "Was man konkret tut (Entwickeln) und nicht tut (Meetings, Reisen)"),
        Map("label" -> "Perspektive",
          "comment" -> "Langfristige Perspektive"),
        Map("label" -> "Arbeitsbed.",
          "comment" -> "Arbeitszeiten, Büros, Ausstattung, etc."))
      user.skills mustEqual List(
        Map("label" -> "Java",
          "comment" -> "Java, 10 Jahre"),
        Map("label" -> "Eclipse",
          "comment" -> "Eclipse, 8 Jahre"))
    }

    "must conform to the expected format of user objects" in {
      val user = User(Json.toJson(Map[String, JsValue]()))
      user.id must throwA[user.NoId]
      user.skills must throwA[user.NoSkills]
      user.priorities must throwA[user.NoPriorities]
      user.attributes must throwA[user.NoAttributes]
    }

    "be accessible via job objects" in {
      val user = User(userJson)
      val job = Job(toJson(jobJson), user)
      job.id mustEqual "hbz"
      job.attributes mustEqual Map("name" -> "hbz", "user" -> "fsteeg@gmail.com")
      job.priorities.data mustEqual Map(
        "Ort" -> Map("score" -> "9", "comment" -> "Köln"),
        "Tätigkeit" -> Map(
          "score" -> "9",
          "comment" -> "Software-Entwickler, z.T. viel orgn. und fachl."),
        "Perspektive" -> Map(
          "score" -> "9",
          "comment" -> "Grundlage: feste Stelle öD; nur zunächst befristet"),
        "Arbeitsbed." -> Map(
          "score" -> "8",
          "comment" -> "ruhig, 1-2 Personen/Büro, gute Ausstattung"))
      job.skills.data mustEqual Map(
        "Java" -> Map("score" -> "9", "comment" -> "Java"),
        "Eclipse" -> Map("score" -> "6", "comment" -> "MF-IDE"))
      job.links mustEqual List(Map(
        "label" -> "homepage",
        "url" -> "http://www.hbz-nrw.de"))
      job.location mustEqual Map("lon" -> 6.935512, "lat" -> 50.934060)
    }

    "must conform to the expected format of job objects" in {
      val user = User(userJson)
      val job = Job(Json.toJson(Map[String, JsValue]()), user)
      job.id must throwA[job.NoId]
      job.skills must throwA[job.NoSkills]
      job.priorities must throwA[job.NoPriorities]
      job.attributes must throwA[job.NoAttributes]
      job.links must throwA[job.NoLinks]
      job.location must throwA[job.NoLocation]
    }

    "implicitly rate jobs for their users" in {
      val user = User(userJson)
      val job = Job(toJson(jobJson), user)
      job.score(user.priorities, job.priorities.data) === "9998"
      job.score(user.skills, job.skills.data) === "96"
    }
  }
}
