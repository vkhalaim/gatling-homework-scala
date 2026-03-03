package simulations

import io.gatling.core.Predef._
import scala.concurrent.duration._

import base.BaseHelpers._
import scenarios.ShopScenario._

class PerfTestSimulation extends Simulation {
  val users: Int =
    Integer.getInteger("users", 50)

  val ramp: Int =
    Integer.getInteger("ramp", 30)

  val duration: Int =
    Integer.getInteger("duration", 300)

  /*
   * Run command. PerfTestSimulation was added to pom.xml:
   * mvn gatling:test -Dusers=80 -Dramp=150 -Dduration=1800
   */

  setUp(
    scn.inject(
      //rampUsers(users).during(ramp.seconds) // open model
      rampConcurrentUsers(1).to(users).during(ramp.seconds),
      constantConcurrentUsers(users).during(duration.seconds) // closed model
    )
  ).protocols(httpProtocol)
}
