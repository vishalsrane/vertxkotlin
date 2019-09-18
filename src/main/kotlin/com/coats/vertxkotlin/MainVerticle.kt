package com.coats.vertxkotlin

import io.vertx.core.AbstractVerticle
import io.vertx.core.Promise
import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import kotlinx.serialization.Serializable
//import kotlinx.serialization.json.JSON
import kotlinx.serialization.json.Json
import kotlin.random.Random

class MainVerticle : AbstractVerticle() {

  override fun start(startPromise: Promise<Void>) {
    var server = vertx.createHttpServer()
    var router = Router.router(vertx)
    router.route().handler(BodyHandler.create())
    router.route(HttpMethod.POST, "/person").handler { routingContext ->
      var jsonBody = routingContext.bodyAsString
      var person = toPerson(jsonBody)
      person.id = Random.nextInt(0, 10000)
      var response = routingContext.response()
      response.putHeader("content-type", "application/json")
      response.setStatusCode(201)
      response.end(toPersonJson(person))
    }
    server.requestHandler(router).listen(8080)
  }

  fun toPerson(name: String) : Person{
    return Json.parse(Person.serializer(), name)
  }

  fun toPersonJson(person: Person): String {
    return Json.stringify(Person.serializer(), person)
  }
}

@Serializable
data class Person(val name: String){
  var id: Int = 0
}
