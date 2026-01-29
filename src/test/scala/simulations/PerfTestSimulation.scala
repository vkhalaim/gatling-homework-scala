package simulations

import io.gatling.core.Predef._

import scala.concurrent.duration._
import base.BaseHelpers._
import io.gatling.core.structure.{PopulationBuilder, ScenarioBuilder}
import pages._

class PerfTestSimulation extends Simulation {

  /*
   * Run command:
   * mvn gatling:test \
   *  -Dgatling.simulationClass=simulations.PerfTestSimulation \
   *  -DbaseUrl=http://localhost
   */

  // -------------------------
  // Scenario definition
  // -------------------------
  val PerfTestSimulation: ScenarioBuilder = scenario("User_Journey")
    .exitBlockOnFail {
      exec(flushState)

        // 1. Open application
        .exec(Home.open)

        // 2. Navigate to Tables
        .exec(Tables.open)
        .exec(Tables.openRandomTable)
        .exec(Tables.addToCart)

        // 3. 50% users add Chair
        .randomSwitch(
          50.0 -> exec(Chairs.flow)
        )

        // 4. 30% of ALL users go to checkout
        .randomSwitch(
          30.0 -> exec(
            Cart.open
              .exec(Checkout.open)
              .exec(Checkout.submit)
          )
        )
    }

  // -------------------------
  // Injection profiles
  // -------------------------

  // Open model (mandatory)
  val openModel: PopulationBuilder = PerfTestSimulation.inject(
    atOnceUsers(100)
  )

  // Closed model (advanced, optional)
  val closedModel: PopulationBuilder = PerfTestSimulation.inject(
    constantConcurrentUsers(100).during(30.seconds)
  )

  // -------------------------
  // Setup
  // -------------------------
  setUp(
    openModel
    // closedModel   // uncomment if needed
  ).protocols(httpProtocol)
}
