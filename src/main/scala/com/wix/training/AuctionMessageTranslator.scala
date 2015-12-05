package com.wix.training

import org.jivesoftware.smack.{MessageListener, Chat}
import org.jivesoftware.smack.packet.Message

class AuctionMessageTranslator(listener: AuctionEventListener) extends MessageListener{
  override def processMessage(value: Chat, message: Message) = {
    listener.auctionClosed()
  }
}
