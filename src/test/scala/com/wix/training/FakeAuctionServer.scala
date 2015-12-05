package com.wix.training

import java.util.concurrent.{ArrayBlockingQueue, TimeUnit}

import com.wix.training.FakeAuctionServer._
import org.jivesoftware.smack.packet.Message
import org.jivesoftware.smack.{Chat, ChatManagerListener, XMPPConnection, _}
import org.specs2.matcher.{Matcher, MustMatchers}

class FakeAuctionServer(itemId: String) extends MustMatchers{

  val connection = new XMPPConnection(XMPP_HOSTNAME)
  var currentChat: Chat = _

  val messageListener: SingleMessageListener = new SingleMessageListener

  def startSellingItem() {
    connection.connect()
    connection.login(String.format(ITEM_ID_AS_LOGIN, itemId),
                      AUCTION_PASSWORD, AUCTION_RESOURCE)
    connection.getChatManager.addChatListener(new ChatManagerListener() {
      def chatCreated(chat: Chat, createdLocally: Boolean): Unit = {
        currentChat = chat
        chat.addMessageListener(messageListener)
      }
    })
  }

  def reportPrice(price: Int, increment: Int, bidder: String) = {
    currentChat.sendMessage(s"SQLVersion :1.1; Event: PRICE; CurrentPrice: $price; Increment: $increment; Bidder: $bidder")
  }

  def announceClosed() = {
    currentChat.sendMessage("SQLVersion: 1,1; Event: CLOSE;")
  }

  def stop() = {
    connection.disconnect()
  }


  def hasReceivedBid(bid: Int, sniperId: String) = {
    receivesAMessageMatching(sniperId, equalTo(XMPPConnection.BID_COMMAND_FORMAT))
  }

  def hasReceivedJoinRequestFromSniper(sniperId: String) = {
    receivesAMessageMatching(sniperId, equalTo(XMPPConnection.JOIN_COMMAND_FORMAT))
  }

  private def receivesAMessageMatching(sniperId : String, messageMatcher : Matcher[String]) = {
    messageListener.receivesAMessage(messageMatcher)
    currentChat.getParticipant must be equalTo(sniperId)
  }


  def getItemId = itemId

}

object FakeAuctionServer{
  val ITEM_ID_AS_LOGIN: String = "auction_%s"
  val AUCTION_RESOURCE: String = "Auction"
  val XMPP_HOSTNAME: String = "localhost"
  val AUCTION_PASSWORD: String = "auction"

}

class SingleMessageListener extends MessageListener with MustMatchers{
 private val messages = new ArrayBlockingQueue[Message](1)

  override def processMessage(chat: Chat, message: Message) = {
    messages.add(message)
  }

  def receivesAMessage(messageMatcher : Matcher[String]) = {
    val message = messages.poll(5,TimeUnit.SECONDS)
    message must not beNull;
    message.getBody must messageMatcher
  }

}