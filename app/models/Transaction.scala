package models

import play.api.libs.json.Json

object Transaction {

  case class Transaction(id: Option[Long], parentId: Option[Long], amount: Double, types: String)

  implicit val transactionWrites = Json.writes[Transaction]
  implicit val transactionReads = Json.reads[Transaction]

  var transactions = List[Transaction]()

  def addTransaction(b: Transaction) = transactions = transactions ::: List(b)
}
