package base

import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.concurrent.duration._

object BaseHelpers {
  val baseUrl: String =
    System.getProperty("baseUrl", "http://localhost")

  // http headers
  val httpProtocol: HttpProtocolBuilder = http
    .baseUrl(baseUrl)
    .acceptHeader("*/*")
    .acceptEncodingHeader("gzip, deflate, br")
    .acceptLanguageHeader("en-GB,en-US;q=0.9,en;q=0.8")
    .userAgentHeader("KGatling Performance Test")

  // think timers
  val minThinkTime: FiniteDuration = 1.second
  val maxThinkTime: FiniteDuration = 3.second

  // flush cookie and cache
  val flushState: ChainBuilder =
    exec(flushHttpCache)
      .exec(flushCookieJar)
}
