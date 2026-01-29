package pages

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import base.BaseHelpers._
import io.gatling.core.structure.ChainBuilder

object Cart {

  val addToCart: ChainBuilder =
    group("Add_Product_To_Cart") {
      exec(
        http("Add Chair To Cart")
          .post("/wp-admin/admin-ajax.php")
          .asFormUrlEncoded
          .formParam("action", "ic_add_to_cart")
          .formParam(
            "add_cart_data",
            "current_product=#{currentProductId}&cart_content=#{cartContent}&current_quantity=1"
          )
          .formParam("cart_container", "0")
          .formParam("cart_widget", "0")
          .check(regex("Added!").exists)
      )
        .pause(minThinkTime, maxThinkTime)
    }

  val open: ChainBuilder =
    group("08_Open_Cart_Page") {
      exec(
        http("Open Cart")
          .get("/cart")
          .check(
            regex("name=\"cart_content\" value='(.*?)'")
              .optional
              .saveAs("cartContent")
          )
      )
        .pause(minThinkTime, maxThinkTime)
    }

}
