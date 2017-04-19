package app

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{AuthorizationFailedRejection, Route}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.softwaremill.session.{SessionConfig, SessionManager}
import com.softwaremill.session.SessionDirectives._
import com.softwaremill.session.SessionOptions._
import org.scalatest.{Matchers, WordSpec}

class Test extends WordSpec with Matchers with ScalatestRouteTest {

  val sessionConfig = SessionConfig.default("some_very_long_secret_and_random_string_some_very_long_secret_and_random_string")
  implicit val sessionManager = new SessionManager[String](sessionConfig)

  def validateUser(username: String, password: String): Boolean =
    username.equals("User") && password.equals("Password")

  val route =
      path("login") {
        post {
          entity(as[String]) { username =>
            if (validateUser(username, "Password"))
              setSession(oneOff, usingCookies, username) {
                complete(s"login successful! $username")
              }
            else complete("wrong credentials!")
          }
        }
      } ~
      path("get_login") {
        get {
          requiredSession(oneOff, usingCookies) { session =>
            complete(session)
          }
        }
      }

  "The service" should {

    "succeed for POST request with right username/password" in {
      // tests:
      Post("/login", content = ("User")) ~> route ~> check {
        responseAs[String] shouldEqual "login successful! User"
      }
    }

    "reject GET request if not authorized" in {
      // tests:
      Get("/get_login") ~> route ~> check {
        rejection shouldEqual AuthorizationFailedRejection
      }
    }
//
//    "return username by GET request" in {
//      // tests:
//      Get("/get_login") ~> route ~> check {
//          responseAs[String] shouldEqual "User"
//      }
//    }
  }
}
//
//entity(as[(String, String)]) { case (username, password) =>
//if (validateUser(username, password))
//setSession(oneOff, usingCookies, username) {
//complete(s"login successful! $username")

//
//    "fail for POST request with right username/password" in {
//      // tests:
//      Post("/login", content = ("wrong", "wrong")) ~> smallRoute ~> check {
//        responseAs[String] shouldEqual "wrong credentials!"
//      }
//    }

//val sessionConfig = SessionConfig.default("some_very_long_secret_and_random_string_some_very_long_secret_and_random_string")
//implicit val sessionManager = new SessionManager[Map[String, String]](sessionConfig)
//
//def validateUser(username: String, password: String): Boolean =
//username.equals("User") && password.equals("Password")
//
//val smallRoute =
//path("login") {
//  post {
//  entity(as[(String, String)]) { data =>
//  if (validateUser(data._1, data._2))
//  setSession(oneOff, usingCookies, (data._1, data._2)) {
//  complete(s"login successful! ${data._1}")
//}
//  else complete("wrong credentials!")
//}
//}
//}
//
//    "return username by GET request" in {
//      // tests:
//      Post("/get_value") ~> smallRoute ~> check {
//        responseAs[String] shouldEqual "User"
//      }
//    }

//    "return a MethodNotAllowed error for PUT requests to the root path" in {
//      // tests:
//      Put() ~> Route.seal(smallRoute) ~> check {
//        status shouldEqual StatusCodes.MethodNotAllowed
//        responseAs[String] shouldEqual "HTTP method not allowed, supported methods: GET"
//      }
//    }

// ~
//        path("cookie") {
//          post {
//            cookie("TestCookie") { cookie =>
//              complete(s"cookie '${cookie.value}'")
//            }
//          }
//        }