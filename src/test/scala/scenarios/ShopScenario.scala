package scenarios

import io.gatling.core.Predef._
import base.BaseHelpers._
import io.gatling.core.structure.ScenarioBuilder
import pages._

object ShopScenario {

  val scn: ScenarioBuilder =
    scenario("Shop_User_Journey")
      .exitBlockOnFail {
        exec(flushState)

          // Home
          .exec(Home.open)

          // Tables
          .exec(Tables.open)
          .exec(Tables.openRandomTable)
          .exec(Tables.addToCart)

          // 50% add chair
          .randomSwitch(
            50.0 -> exec(Chairs.flow)
          )

          // 30% checkout (independent)
          .randomSwitch(
            30.0 -> exec(
              Cart.open
                .exec(Checkout.open)
                .exec(Checkout.submit)
            )
          )
      }
}
