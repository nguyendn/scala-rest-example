package models

import play.api.libs.json.Json

object Transaction {

  case class Transaction(name: String, author: String)

  implicit val transactionWrites = Json.writes[Transaction]
  implicit val transactionReads = Json.reads[Transaction]

  var transactions = List[Transaction]()

  def addTransaction(b: Transaction) = transactions = transactions ::: List(b)
}
