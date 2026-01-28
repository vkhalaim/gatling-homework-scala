package pages

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import base.BaseHelpers._
import io.gatling.core.structure.ChainBuilder

object TablesPage {

  val open: ChainBuilder =
    group("02_Open_Tables_Page") {
      exec(
        http("Open Tables Page")
          .get("/tables")
          .check(status.is(200))
          .check(regex("/products/(.+?)\"").findRandom.saveAs("tableId"))
      )
        .pause(minThinkTime, maxThinkTime)
    }

  val openRandomTable: ChainBuilder =
    group("03_Open_Table_Product") {
      exec(
        http("Open Table Product")
          .get("/products/#{tableId}")
          .check(status.is(200))
          .check(regex("name=\"add-to-cart\" value=\"(\\d+)\"").saveAs("tableProductId"))
      )
        .pause(minThinkTime, maxThinkTime)
    }

  val addToCart: ChainBuilder =
    group("04_Add_Table_To_Cart") {
      exec(
        http("Add Table To Cart")
          .post("/?wc-ajax=add_to_cart")
          .asFormUrlEncoded
          .formParam("product_id", "#{tableProductId}")
          .check(status.is(200))
      )
        .pause(minThinkTime, maxThinkTime)
    }

}
