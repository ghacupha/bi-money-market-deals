package gatling.simulations;

/*-
 * Money Market Bi - BI Microservice for Money Market Bi deals is part of the Granular Bi System
 * Copyright © 2025 Edwin Njeru (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

import static io.gatling.javaapi.core.CoreDsl.StringBody;
import static io.gatling.javaapi.core.CoreDsl.exec;
import static io.gatling.javaapi.core.CoreDsl.rampUsers;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.header;
import static io.gatling.javaapi.http.HttpDsl.headerRegex;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;

/**
 * Performance test for the MoneyMarketDeal entity.
 *
 * @see <a href="https://github.com/jhipster/generator-jhipster/tree/v8.11.0/generators/gatling#logging-tips">Logging tips</a>
 */
public class MoneyMarketDealGatlingTest extends Simulation {

    String baseURL = Optional.ofNullable(System.getProperty("baseURL")).orElse("http://localhost:8575");

    HttpProtocolBuilder httpConf = http
        .baseUrl(baseURL)
        .inferHtmlResources()
        .acceptHeader("*/*")
        .acceptEncodingHeader("gzip, deflate")
        .acceptLanguageHeader("fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3")
        .connectionHeader("keep-alive")
        .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:33.0) Gecko/20100101 Firefox/33.0")
        .silentResources(); // Silence all resources like css or css so they don't clutter the results

    Map<String, String> headersHttp = Map.of("Accept", "application/json");

    Map<String, String> headersHttpAuthentication = Map.of("Content-Type", "application/json", "Accept", "application/json");

    Map<String, String> headersHttpAuthenticated = Map.of("Accept", "application/json", "Authorization", "${access_token}");

    ChainBuilder scn = exec(http("First unauthenticated request").get("/api/account").headers(headersHttp).check(status().is(401)))
        .exitHereIfFailed()
        .pause(10)
        .exec(
            http("Authentication")
                .post("/api/authenticate")
                .headers(headersHttpAuthentication)
                .body(StringBody("{\"username\":\"admin\", \"password\":\"admin\"}"))
                .asJson()
                .check(header("Authorization").saveAs("access_token"))
        )
        .exitHereIfFailed()
        .pause(2)
        .exec(http("Authenticated request").get("/api/account").headers(headersHttpAuthenticated).check(status().is(200)))
        .pause(10)
        .repeat(2)
        .on(
            exec(
                http("Get all moneyMarketDeals")
                    .get("/services/moneymarketbi/api/money-market-deals")
                    .headers(headersHttpAuthenticated)
                    .check(status().is(200))
            )
                .pause(Duration.ofSeconds(10), Duration.ofSeconds(20))
                .exec(
                    http("Create new moneyMarketDeal")
                        .post("/services/moneymarketbi/api/money-market-deals")
                        .headers(headersHttpAuthenticated)
                        .body(
                            StringBody(
                                "{" +
                                "\"dealNumber\": \"SAMPLE_TEXT\"" +
                                ", \"tradingBook\": \"SAMPLE_TEXT\"" +
                                ", \"counterPartyName\": \"SAMPLE_TEXT\"" +
                                ", \"finalInterestAccrualDate\": \"2020-01-01T00:00:00.000Z\"" +
                                ", \"counterPartySideType\": \"SAMPLE_TEXT\"" +
                                ", \"dateOfCollectionStatement\": \"SAMPLE_TEXT\"" +
                                ", \"currencyCode\": \"SAMPLE_TEXT\"" +
                                ", \"principalAmount\": 0" +
                                ", \"interestRate\": 0" +
                                ", \"interestAccruedAmount\": 0" +
                                ", \"totalInterestAtMaturity\": 0" +
                                ", \"counterpartyNationality\": \"SAMPLE_TEXT\"" +
                                ", \"endDate\": \"2020-01-01T00:00:00.000Z\"" +
                                ", \"treasuryLedger\": \"SAMPLE_TEXT\"" +
                                ", \"dealSubtype\": \"SAMPLE_TEXT\"" +
                                ", \"shillingEquivalentPrincipal\": 0" +
                                ", \"shillingEquivalentInterestAccrued\": 0" +
                                ", \"shillingEquivalentPVFull\": 0" +
                                ", \"counterpartyDomicile\": \"SAMPLE_TEXT\"" +
                                ", \"settlementDate\": \"2020-01-01T00:00:00.000Z\"" +
                                ", \"transactionCollateral\": \"SAMPLE_TEXT\"" +
                                ", \"institutionType\": \"SAMPLE_TEXT\"" +
                                ", \"maturityDate\": \"2020-01-01T00:00:00.000Z\"" +
                                ", \"institutionReportName\": \"SAMPLE_TEXT\"" +
                                ", \"transactionType\": \"SAMPLE_TEXT\"" +
                                ", \"reportDate\": \"2020-01-01T00:00:00.000Z\"" +
                                ", \"active\": null" +
                                "}"
                            )
                        )
                        .asJson()
                        .check(status().is(201))
                        .check(headerRegex("Location", "(.*)").saveAs("new_moneyMarketDeal_url"))
                )
                .exitHereIfFailed()
                .pause(10)
                .repeat(5)
                .on(
                    exec(
                        http("Get created moneyMarketDeal")
                            .get("/services/moneymarketbi${new_moneyMarketDeal_url}")
                            .headers(headersHttpAuthenticated)
                    ).pause(10)
                )
                .exec(
                    http("Delete created moneyMarketDeal")
                        .delete("/services/moneymarketbi${new_moneyMarketDeal_url}")
                        .headers(headersHttpAuthenticated)
                )
                .pause(10)
        );

    ScenarioBuilder users = scenario("Test the MoneyMarketDeal entity").exec(scn);

    {
        setUp(
            users.injectOpen(rampUsers(Integer.getInteger("users", 100)).during(Duration.ofMinutes(Integer.getInteger("ramp", 1))))
        ).protocols(httpConf);
    }
}
