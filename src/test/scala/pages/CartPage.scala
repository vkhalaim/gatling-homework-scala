package pages

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import base.BaseHelpers._
import io.gatling.core.structure.ChainBuilder

object CartPage {

  val open: ChainBuilder =
    group("08_Open_Cart_Page") {
      exec(
        http("Open Cart")
          .get("/cart")
          .check(status.is(200))
          .check(
            regex("name=\"cart_content\" value='(.*?)'")
              .optional
              .saveAs("cartContent")
          )
      )
        .pause(minThinkTime, maxThinkTime)
    }

}
