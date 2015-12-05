package com.wix.training

import org.jivesoftware.smack.packet.Message
import org.jivesoftware.smack.{Chat, MessageListener}

import scala.collection.mutable

class AuctionMessageTranslator(listener: AuctionEventListener) extends MessageListener {
  override def processMessage(value: Chat, message: Message) = {

    def unpackEventFrom(message: Message): mutable.HashMap[String, String] = {
      val eventMap = new mutable.HashMap[String, String]

      message.getBody.split(";").map(element => {
        val pair: Array[String] = element.split(":")
        eventMap.put(pair(0).trim, pair(1).trim)
      })
      eventMap
    }

    val event = unpackEventFrom(message)

    event.get("Event") match {
      case Some("CLOSE") => listener.auctionClosed()
      case Some("PRICE") => listener.currentPrice(event.get("CurrentPrice").get.toInt, event.get("Increment").get.toInt)
      case _ => throw new RuntimeException("unsupported Event type")
    }
  }
}
