package com.wix.training

class ApplicationRunner {

  var driver: AuctionSniperDriver = _

  def startBiddingIn(auction: FakeAuctionServer) = {
    val thread = new Thread(new Runnable {
      override def run() {
        new Main(ApplicationRunner.arguments(auction.getItemId): _*)
      }
    })

    thread.setDaemon(true)
    thread.start()

    driver = new AuctionSniperDriver(timeoutMillis = 1000)
    driver.isShowingSniperStatus(Main.STATUS_JOINING)
  }

  def hasShownSniperIsBidding() = {
    driver.isShowingSniperStatus(Main.SNIPER_BIDDING)
  }

  def isShowingSniperHasLostAuction() = {
    driver.isShowingSniperStatus(Main.STATUS_LOST)
  }

  def stop(): Any = {
    if (driver != null) {
      driver.dispose()
    }
  }
}


object ApplicationRunner {
  val XMPP_HOSTNAME = "localhost"

  val SNIPER_ID = "sniper"
  val SNIPER_PASSWORD = "sniper"

  val SNIPER_XMPP_ID = SNIPER_ID + "@" + XMPP_HOSTNAME + "/Auction"

  protected def arguments(auctionId: String): List[String] = XMPP_HOSTNAME :: SNIPER_ID :: SNIPER_PASSWORD :: auctionId :: Nil
}



