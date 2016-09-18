package controllers

import play.api.libs.json._
import play.api.mvc._
import models.Transaction._

object Application extends Controller {

  def saveTransaction(id: Long) = Action(BodyParsers.parse.json) { request =>
    val b = request.body.validate[Transaction]
    b.fold(
      errors => {
        BadRequest(Json.obj("status" -> "OK", "message" -> JsError.toFlatJson(errors)))
      },
      transaction => {
        addTransaction(Transaction(Some(id), transaction.parentId, transaction.amount, transaction.types))
        Ok(Json.obj("status" -> "OK"))
      }
    )
  }

  def getTransaction(id: Long) = Action {
    val transaction = transactions.filter(_.id == Option(id)).head
    Ok(Json.obj("amount" -> transaction.amount, "types" -> transaction.types, "parentId" -> transaction.parentId))
  }

  def listTypes(types: String) = Action {
    Ok(Json.toJson(transactions.filter(_.types == types).map(_.id.getOrElse(0).asInstanceOf[Long])))
  }

  def sumTransaction(id: Long) = Action {
    Ok(Json.obj("sum" -> transactions.filter(t => (t.id == Option(id) || t.parentId == Option(id)) ).map(_.amount).sum))
  }
}
