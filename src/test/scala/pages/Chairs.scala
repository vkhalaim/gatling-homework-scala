package pages

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import base.BaseHelpers._
import io.gatling.core.structure.ChainBuilder

object Chairs {

  val open: ChainBuilder =
    group("05_Open_Chairs_Page") {
      exec(
        http("Open Chairs Page")
          .get("/chairs")
          .check(regex("/products/(.+?)\"").findRandom.saveAs("chairId"))
      )
        .pause(minThinkTime, maxThinkTime)
    }

  val openRandomChair: ChainBuilder =
    group("06_Open_Chair_Product") {
      exec(
        http("Open Chair Product")
          .get("/products/#{chairId}")
          .check(
            regex("""<link rel='shortlink' href='.*/\?p=(\d+)'""")
              .saveAs("currentProductId")
          )
          .check(
            regex("""name="cart_content" value='(.*?)'""")
              .optional
              .saveAs("cartContent")
          )
      )
        .pause(minThinkTime, maxThinkTime)
    }

  // Full chair flow (used in randomSwitch)
  val flow: ChainBuilder =
    exec(open)
      .exec(openRandomChair)
      .exec(Cart.addToCart)
}
