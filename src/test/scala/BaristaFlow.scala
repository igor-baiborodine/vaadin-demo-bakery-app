import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class BaristaFlow extends Simulation {

	def toInt(s: String): Option[Int] = {
		try {
			Some(s.toInt)
		} catch {
			case _: NumberFormatException => None
		}
	}

	// The URL of the system under test
	val baseUrl: String = System.getProperty("gatling.baseUrl", "http://localhost:8080")

	// The total number of simulated user sessions
	// NOTE: the number of concurrent sessions is lower because sessions start one by one
	// with a given interval and some may finish before the last session starts.
	val sessionCount: Int = toInt(System.getProperty("gatling.sessionCount", "")) match {
		case Some(n) => n
		case None => 100 // the default
	}

	// The interval (in milliseconds) between starting new user sessions
	val sessionStartInterval: Int = toInt(System.getProperty("gatling.sessionStartInterval", "")) match {
		case Some(n) => n
		case None => 100 // the default
	}

	// The repeat count of the scenario, by default executed only once
	val sessionRepeats: Int = toInt(System.getProperty("gatling.sessionRepeats", "")) match {
		case Some(n) => n
		case None => 1 // the default
	}

	val httpProtocol = http
		.baseURL(baseUrl)
		.acceptHeader("*/*")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("en-US,en;q=0.5")
		.userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.13; rv:57.0) Gecko/20100101 Firefox/57.0")

	val headers_0 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_4 = Map("Content-type" -> "application/json; charset=UTF-8")

	val initSyncAndClientIds = exec((session) => {
		session.setAll(
			"syncId" -> 0,
			"clientId" -> 0
		)
	})

	val url = "/"
	val uidlUrl = url + "?v-r=uidl&v-uiId=${uiId}"

	val uIdExtract = regex(""""v-uiId":(\d+)""").saveAs("uiId")
	val syncIdExtract = regex("""syncId":([0-9]*)""").saveAs("syncId")
	val clientIdExtract = regex("""clientId":([0-9]*)""").saveAs("clientId")
	val xsrfTokenExtract = regex("""Vaadin-Security-Key":\s?"([^"]*)""").saveAs("seckey")

	// Storefront from initial response
	val gridIdExtract       =  regex("""node":(\d+),+"type":"put",+"key":"payload",+"feat":[0-9]*,+"value":\{+"type":"@id",+"payload":"grid"""").saveAs("gridId")
	val dialogIdExtract     =  regex("""node":(\d+),+"type":"put",+"key":"payload",+"feat":[0-9]*,+"value":\{+"type":"@id",+"payload":"dialog"""").saveAs("dialogId")
	val newButtonIdExtract  =  regex("""node":(\d+),+"type":"put",+"key":"payload",+"feat":[0-9]*,+"value":\{+"type":"@id",+"payload":"action"""").saveAs("newButtonId")
	val customerIdExtract   =  regex("""node":(\d+),+"type":"put",+"key":"payload",+"feat":[0-9]*,+"value":\{+"type":"@id",+"payload":"customerName"""").saveAs("customerId")
	val phoneIdExtract      =  regex("""node":(\d+),+"type":"put",+"key":"payload",+"feat":[0-9]*,+"value":\{+"type":"@id",+"payload":"customerNumber"""").saveAs("phoneId")
	val reviewIdExtract     =  regex("""node":(\d+),+"type":"put",+"key":"payload",+"feat":[0-9]*,+"value":\{+"type":"@id",+"payload":"review"""").saveAs("reviewId")
	val saveIdExtract       =  regex("""node":(\d+),+"type":"put",+"key":"payload",+"feat":[0-9]*,+"value":\{+"type":"@id",+"payload":"save"""").saveAs("saveId")
	val saveStoreExtract    =  regex("""node":(\d+),+"type":"put",+"key":"payload",+"feat":[0-9]*,+"value":\{+"type":"@id",+"payload":"pickupLocation"""").saveAs("storeId")
	val statusIdExtract     =  regex("""node":(\d+),+"type":"put",+"key":"payload",+"feat":[0-9]*,+"value":\{+"type":"@id",+"payload":"status"""").saveAs("statusId")
	val dueTimeIdExtract    =  regex("""node":(\d+),+"type":"put",+"key":"payload",+"feat":[0-9]*,+"value":\{+"type":"@id",+"payload":"dueTime"""").saveAs("dueTimeId")
	val dueDateIdExtract    =  regex("""node":(\d+),+"type":"put",+"key":"payload",+"feat":[0-9]*,+"value":\{+"type":"@id",+"payload":"dueDate"""").saveAs("dueDateId")

	// Order dialog
	val amountIdExtract    = regex("""node":(\d+),"type":"put","key":"payload","feat":[0-9]*,"value":\{"type":"@id","payload":"amount"""").saveAs("amountId")
	val productsIdExtract  = regex("""node":(\d+),"type":"put","key":"payload","feat":[0-9]*,"value":\{"type":"@id","payload":"products"""").saveAs("productsId")
	val products2IdExtract = regex("""payload":"products.{2000,4000}node":(\d+),"type":"put","key":"payload","feat":[0-9]*,"value":\{"type":"@id","payload":"products"""").saveAs("products2Id")
	val productsId1Extract2  = regex("""node":(\d+),"type":"put","key":"selectedItem","feat":[0-9],"value":\{"key":"[0-9]","label":"Strawberry Bun""").saveAs("products1Id2")
	val productsId2Extract2  = regex("""node":(\d+),"type":"put","key":"selectedItem","feat":[0-9],"value":\{"key":"[0-9]","label":"Vanilla Cracker""").saveAs("products2Id2")


	val scn = scenario("BaristaFlow")
		.repeat(sessionRepeats) {
			exec(http("Initial request")
				.get(url)
				.headers(headers_0)
				.check(xsrfTokenExtract)
			)
				.exec(initSyncAndClientIds)
				.pause(8, 11)

				.exec(http("Login")
					.post("/login")
					.headers(headers_0)
					.formParam("username", "barista@vaadin.com")
					.formParam("password", "barista")
					.formParam("prefix", "undefined")
					.formParam("suffix", "undefined")
					.check(uIdExtract)
					.check(gridIdExtract)
					.check(newButtonIdExtract)
					.check(dialogIdExtract)
					.check(syncIdExtract).check(clientIdExtract)
				)
				.pause(2)

				.exec(http("First xhr, init grid")
					.post(uidlUrl)
					.headers(headers_4)
					.body(ElFileBody("BaristaFlow_0004_request.txt"))
					.check(syncIdExtract).check(clientIdExtract))
				.pause(4, 10)

				.exec(http("Click new order")
					.post(uidlUrl)
					.headers(headers_4)
					.body(ElFileBody("BaristaFlow_0009_request.txt"))
					.check(syncIdExtract).check(clientIdExtract)
					.check(amountIdExtract)
					.check(productsIdExtract)
					.check(customerIdExtract)
					.check(phoneIdExtract)
					.check(reviewIdExtract)
					.check(saveStoreExtract)
					.check(statusIdExtract)
					.check(dueTimeIdExtract)
					.check(dueDateIdExtract)
				)
				.pause(5)

				.exec(http("Selection changes")
					.post(uidlUrl)
					.headers(headers_4)
					.body(ElFileBody("BaristaFlow_0010_request.txt"))
					.check(syncIdExtract).check(clientIdExtract))
				.pause(2)

				.exec(http("Customer name")
					.post(uidlUrl)
					.headers(headers_4)
					.body(ElFileBody("BaristaFlow_0027_request.txt"))
					.check(syncIdExtract).check(clientIdExtract))
				.pause(5, 6)

				.exec(http("Phone number")
					.post(uidlUrl)
					.headers(headers_4)
					.body(ElFileBody("BaristaFlow_0036_request.txt"))
					.check(syncIdExtract).check(clientIdExtract))
				.pause(5, 6)
				.exec(http("Product CB1 opened")
					.post(uidlUrl)
					.headers(headers_4)
					.body(ElFileBody("BaristaFlow_0037_a_request.txt"))
					.check(syncIdExtract).check(clientIdExtract))
				.pause(4, 6)
				.exec(http("Select product")
					.post(uidlUrl)
					.headers(headers_4)
					.body(ElFileBody("BaristaFlow_0037_request.txt"))
					.check(syncIdExtract).check(clientIdExtract)
					.check(productsId1Extract2)
					.check(products2IdExtract)
				)
				.pause(2)
				.exec(http("Product CB1 closed")
					.post(uidlUrl)
					.headers(headers_4)
					.body(ElFileBody("BaristaFlow_0037b_request.txt"))
					.check(syncIdExtract).check(clientIdExtract))
				.pause(2)
				.exec(http("Product CB2 opened")
					.post(uidlUrl)
					.headers(headers_4)
					.body(ElFileBody("BaristaFlow_0038_a_request.txt"))
					.check(syncIdExtract).check(clientIdExtract))
				.pause(5, 7)
				.exec(http("Select 2. product")
					.post(uidlUrl)
					.headers(headers_4)
					.body(ElFileBody("BaristaFlow_0038_request.txt"))
					.check(syncIdExtract).check(clientIdExtract)
					.check(productsId1Extract2)
					.check(productsId2Extract2)
				)
				.pause(2)
				.exec(http("Product CB2 closed")
					.post(uidlUrl)
					.headers(headers_4)
					.body(ElFileBody("BaristaFlow_0038b_request.txt"))
					.check(syncIdExtract).check(clientIdExtract))
				.pause(5, 7)
				.exec(http("Select Store opened")
					.post(uidlUrl)
					.headers(headers_4)
					.body(ElFileBody("BaristaFlow_0043_a_request.txt"))
					.check(syncIdExtract).check(clientIdExtract))
				.pause(2)
				.exec(http("Select Store selected and closed")
					.post(uidlUrl)
					.headers(headers_4)
					.body(ElFileBody("BaristaFlow_0043_request.txt"))
					.check(syncIdExtract).check(clientIdExtract))
				.pause(2)
				.exec(http("Click review order")
					.post(uidlUrl)
					.headers(headers_4)
					.body(ElFileBody("BaristaFlow_0044_request.txt"))
					.check(syncIdExtract).check(clientIdExtract)
					.check(regex("""Order placed"""))
					.check(saveIdExtract)
				)
				.pause(2)
				.exec(http("Click place order")
					.post(uidlUrl)
					.headers(headers_4)
					.body(ElFileBody("BaristaFlow_0045_request.txt"))
					.check(syncIdExtract).check(clientIdExtract)
					.check(regex("""Order was created"""))
				)
				.pause(2, 6)
		}

	setUp(scn.inject(rampUsers(sessionCount) over sessionStartInterval)).protocols(httpProtocol)
}