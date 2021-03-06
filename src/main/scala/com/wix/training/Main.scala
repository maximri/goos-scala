package com.wix.training

import java.awt.event.{WindowAdapter, WindowEvent}
import javax.swing.SwingUtilities

import com.wix.training.Main._
import com.wix.training.XMPPConnection.AUCTION_ID_FORMAT
import org.jivesoftware.smack.packet.Message
import org.jivesoftware.smack.{Chat, XMPPConnection}

class Main(args: String*) extends AuctionEventListener{
  var ui: MainWindow = _
  var notToBeGCD: Chat = _

  startUserInterface()
  val auctionHouseConnection: XMPPConnection = XMPPConnection.connection(args(ARG_HOSTNAME), args(ARG_USERNAME), args(ARG_PASSWORD))

  joinAuction(auctionHouseConnection,args(Main.ARG_ITEM_ID))

  private def startUserInterface() = {
    SwingUtilities.invokeAndWait(new Runnable {
      override def run() {
        ui = new MainWindow()
      }
    })
  }

  private def disconnectWhenUICloses(connection: XMPPConnection) = {
    ui.addWindowListener(new WindowAdapter {
      override def windowClosed(e: WindowEvent): Unit = {
        connection.disconnect()
      }
    })
  }

  def joinAuction(connection: XMPPConnection, itemID: String): Unit = {
    disconnectWhenUICloses(connection)
    val chat: Chat = connection.getChatManager.createChat(
                        auctionId(itemID, connection), new AuctionMessageTranslator(this))
    notToBeGCD = chat

    chat.sendMessage(new Message(XMPPConnection.JOIN_COMMAND_FORMAT))
  }

  override def auctionClosed(): Unit = {
    ui.showStatus(STATUS_LOST)
  }

  override def currentPrice(price: Int, increment: Int): Unit = {
  }
}

object Main {
  val SNIPER_BIDDING = "Bidding"


  val STATUS_JOINING = "Joining"
  val SNIPER_STATUS_NAME = "sniper status"
  val STATUS_LOST = "lost"

  val ARG_HOSTNAME = 0
  val ARG_USERNAME = 1
  val ARG_PASSWORD = 2
  val ARG_ITEM_ID = 3

  def auctionId(itemId: String, connection: XMPPConnection): String = {
    String.format(AUCTION_ID_FORMAT, itemId, connection.getServiceName)
  }
}


private object XMPPConnection {

  val AUCTION_RESOURCE = "Auction"
  val ITEM_ID_AS_LOGIN = "auction_%s"
  val AUCTION_ID_FORMAT = ITEM_ID_AS_LOGIN + "@%s/" + AUCTION_RESOURCE

  val JOIN_COMMAND_FORMAT = "SOLVersion: 1.1; Command: JOIN; "
  val BID_COMMAND_FORMAT = "SOLVersion: 1.1; Command: BID; Price: %d;"

  def connection(hostName: String, username: String, password: String): XMPPConnection = {
    val connection: XMPPConnection = new XMPPConnection(hostName)
    connection.connect()
    connection.login(username, password, AUCTION_RESOURCE)
    connection
  }
}

