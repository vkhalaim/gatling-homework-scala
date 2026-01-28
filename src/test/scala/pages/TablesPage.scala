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
          .check(
            regex("""<link rel='shortlink' href='.*/\?p=(\d+)'""")
              .saveAs("tableProductId")
          )
      )
        .pause(minThinkTime, maxThinkTime)
    }

  val addToCart: ChainBuilder =
    group("04_Add_Table_To_Cart") {
      exec(
        http("Add Table To Cart")
          .post("/wp-admin/admin-ajax.php")
          .asFormUrlEncoded
          .formParam("action", "ic_add_to_cart")
          .formParam(
            "add_cart_data",
            "current_product=#{tableProductId}&cart_content=&current_quantity=1"
          )
          .check(regex("Added!").exists)
      )
        .pause(minThinkTime, maxThinkTime)
    }

}
