package pages

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import base.BaseHelpers._
import io.gatling.core.structure.ChainBuilder

object Tables {

  val open: ChainBuilder =
    group("02_Open_Tables_Page") {
      exec(
        http("Open Tables Page")
          .get("/tables")
          .check(regex("/products/(.+?)\"").findRandom.saveAs("tableId"))
      )
        .pause(minThinkTime, maxThinkTime)
    }

  val openRandomTable: ChainBuilder =
    group("03_Open_Table_Product") {
      exec(
        http("Open Table Product")
          .get("/products/#{tableId}")
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
  // Full tables flow
  val flow: ChainBuilder =
    exec(open)
      .exec(openRandomTable)
      .exec(Cart.addToCart)
}