package pages

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import base.BaseHelpers._
import io.gatling.core.structure.ChainBuilder

object Home {

  val open: ChainBuilder =
    group("01_Open_Home_Page") {
      exec(
        http("Open Home")
          .get("/")
          .check(status.is(200))
          .check(regex("Performance testing Essentials").exists)
      )
        .pause(minThinkTime, maxThinkTime)
    }

}
