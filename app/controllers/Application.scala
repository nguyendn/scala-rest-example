package controllers

import play.api.libs.json._
import play.api.mvc._
import models.Transaction._

object Application extends Controller {

  def listTransaction = Action {
    Ok(Json.toJson(transactions))
  }

  def saveTransaction = Action(BodyParsers.parse.json) { request =>
    val b = request.body.validate[Transaction]
    b.fold(
      errors => {
        BadRequest(Json.obj("status" -> "OK", "message" -> JsError.toFlatJson(errors)))
      },
      transaction => {
        addTransaction(transaction)
        Ok(Json.obj("status" -> "OK"))
      }
    )
  }
}
