package pages

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import base.BaseHelpers._
import io.gatling.core.structure.ChainBuilder

object Checkout {

  val open: ChainBuilder =
    group("09_Open_Checkout_Page") {
      exec(
        http("Open Checkout")
          .get("/checkout")
          .check(status.is(200))
          .check(regex("""name="cart_content" value='(.+?)'""").saveAs("cartContent"))
          .check(regex("""value="(\d+)" name="trans_id"""").saveAs("transId"))
          .check(regex("""name="total_net" value="(.+?)"""").saveAs("totalNet"))
      )
        .pause(minThinkTime, maxThinkTime)
    }

  val submit: ChainBuilder =
    group("10_Submit_Order") {

      exec { session =>
        session
          .set("city", "Kyiv")
          .set("country", "UA")
          .set("postalCode", "01001")
          .set("redirectUrl", "/thank-you")
          .set("comment", "")
          .set("fullName", s"User_${session.userId}")
          .set("email", s"user${session.userId}@test.com")
          .set("phone", s"+380${500000000 + session.userId}")
          .set("address", s"Street ${session.userId}")
      }
        .exec(
          http("Place Order")
            .post("/checkout")
            .asMultipartForm
            .formParam("cart_city", "#{city}")
            .formParam("cart_content", "#{cartContent}")
            .formParam("ic_formbuilder_redirect", "#{redirectUrl}")
            .formParam("cart_submit", "Place Order")
            .formParam("cart_comment", "#{comment}")
            .formParam("trans_id", "#{transId}")
            .formParam("cart_type", "order")
            .formParam("cart_country", "#{country}")
            .formParam("shipping", "order")
            .formParam("cart_name", "#{fullName}")
            .formParam("cart_phone", "#{phone}")
            .formParam("cart_address", "#{address}")
            .formParam("cart_postal", "#{postalCode}")
            .formParam("cart_email", "#{email}")
            .formParam("total_net", "#{totalNet}")
            .check(status.is(200))
        )
        .pause(minThinkTime, maxThinkTime)
    }

}
