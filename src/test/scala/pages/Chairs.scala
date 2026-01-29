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
          .check(status.is(200))
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
              .saveAs("chairProductId")
          )
          .check(
            regex("name=\"cart_content\" value='(.*?)'")
              .optional
              .saveAs("cartContent")
          )
      )
        .pause(minThinkTime, maxThinkTime)
    }

  val addToCart: ChainBuilder =
    group("07_Add_Chair_To_Cart") {
      exec(
        http("Add Chair To Cart")
          .post("/wp-admin/admin-ajax.php")
          .asFormUrlEncoded
          .formParam("action", "ic_add_to_cart")
          .formParam(
            "add_cart_data",
            "current_product=#{tableProductId}&cart_content=#{cartContent}&current_quantity=1"
          )
          .formParam("cart_container", "0")
          .formParam("cart_widget", "0")
          .check(regex("Added!").exists)
      )
        .pause(minThinkTime, maxThinkTime)
    }

  // Full chair flow (used in randomSwitch)
  val flow: ChainBuilder =
    exec(open)
      .exec(openRandomChair)
      .exec(addToCart)

}
