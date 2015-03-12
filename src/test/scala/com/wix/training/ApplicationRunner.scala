package com.wix.training

class ApplicationRunner {
  def showsSniperIsBidding() = {
    driver.showsSniperStatus(Main.SNIPER_BIDDING)
  }

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
    driver.showsSniperStatus(Main.STATUS_JOINING)
  }

  def showsSniperHasLostAuction() = {
    driver.showsSniperStatus(Main.STATUS_LOST)
  }

  def stop(): Any = {
    if (driver != null) {
      driver.dispose()
    }
  }
}


object ApplicationRunner {
  val SNIPER_ID = "sniper"
  val SNIPER_PASSWORD = "sniper"

  val XMPP_HOSTNAME = "localhost"
  
  val SNIPER_XMPP_ID = SNIPER_ID + "@" + XMPP_HOSTNAME + "/Auction"

  protected def arguments(auctionId: String): List[String] = XMPP_HOSTNAME :: SNIPER_ID :: SNIPER_PASSWORD :: auctionId :: Nil
}



