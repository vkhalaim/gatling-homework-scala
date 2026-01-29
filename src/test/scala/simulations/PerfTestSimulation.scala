package simulations

import io.gatling.core.Predef._
import scala.concurrent.duration._

import base.BaseHelpers._
import scenarios.ShopScenario._

class PerfTestSimulation extends Simulation {

  /*
   * Run command. PerfTestSimulation was added to pom.xml:
   * mvn gatling:test
   */

  setUp(
    scn.inject(
      atOnceUsers(100)
      // constantConcurrentUsers(100).during(30.seconds) // optional
    )
  ).protocols(httpProtocol)
}
