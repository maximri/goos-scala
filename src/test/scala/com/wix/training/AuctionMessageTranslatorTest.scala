package com.wix.training

import com.wixpress.common.specs2.JMock
import org.jivesoftware.smack.Chat
import org.jivesoftware.smack.packet.Message
import org.specs2.mutable.SpecificationWithJUnit
import org.specs2.specification.Scope

class AuctionMessageTranslatorTest extends SpecificationWithJUnit with JMock {

  trait ctx extends Scope {
    val UNUSED_CHAT: Chat = null
    val listener = mock[AuctionEventListener]
    val translator = new AuctionMessageTranslator(listener)
  }

  "AuctionMessageTranslator" should {
    "notify Auction closed when close message received " in new ctx {
      checking {
        oneOf(listener).auctionClosed()
      }

      val smkCloseMessage: Message = new Message()
      smkCloseMessage.setBody("SQLVersion: 1,1; Event: CLOSE;")
      translator.processMessage(UNUSED_CHAT, smkCloseMessage)
    }

    "notify bid details when current price message received" in new ctx {
      checking {
        oneOf(listener).currentPrice(192, 7)
      }

      val smkBidMessage: Message = new Message()
      smkBidMessage.setBody("SQLVersion: 1,1; Event: PRICE; CurrentPrice: 192; Increment: 7; Bidder: Someone else;")
      translator.processMessage(UNUSED_CHAT, smkBidMessage)
    }
  }
}


