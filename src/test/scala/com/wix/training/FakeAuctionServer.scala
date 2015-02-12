package com.wix.training

import java.util.concurrent.{TimeUnit, ArrayBlockingQueue}

import com.wix.training.FakeAuctionServer._
import org.jivesoftware.smack.packet.Message
import org.jivesoftware.smack.{Chat, ChatManagerListener, XMPPConnection, _}
import org.specs2.matcher.MustMatchers

class FakeAuctionServer(itemId: String) {
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

  def hasReceivedJoinRequestFromSniper() = {
    messageListener.receivesAMessage
  }

  def announceClosed() = {
    currentChat.sendMessage(new Message())
  }

  def stop() = {
    connection.disconnect()
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
 private val messages: ArrayBlockingQueue[Message] = new ArrayBlockingQueue[Message](1)

  override def processMessage(chat: Chat, message: Message): Unit = {
    messages.add(message)
  }

  def receivesAMessage(): Unit = {
    val message: Message = messages.poll(5,TimeUnit.SECONDS)
    message must not beNull
  }

}