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

          // Tables flow
          .exec(Tables.flow)

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
