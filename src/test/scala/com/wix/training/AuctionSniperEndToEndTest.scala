package com.wix.training

import com.wix.training.ApplicationRunner.SNIPER_XMPP_ID
import org.specs2.mutable.{After, SpecificationWithJUnit}
import org.specs2.specification.Scope

class AuctionSniperEndToEndTest extends SpecificationWithJUnit{
  sequential

  trait ctx extends Scope with After {
    val sniperId = "item_54321"
    val auctionServer = new FakeAuctionServer(sniperId)
    val application = new ApplicationRunner

   override def after = {
      auctionServer.stop()
      application.stop()
    }
  }

  "Sniper" should {
    "joins auction until auction closes" in new ctx{
      auctionServer.startSellingItem()
      application.startBiddingIn(auctionServer)
      auctionServer.hasReceivedJoinRequestFromSniper(SNIPER_XMPP_ID)
      auctionServer.announceClosed()
      application isShowingSniperHasLostAuction()
    }

    "make a higher bid but lose" in new ctx {
      auctionServer.startSellingItem()

      application.startBiddingIn(auctionServer)
      auctionServer.hasReceivedJoinRequestFromSniper(SNIPER_XMPP_ID)

      auctionServer.reportPrice(1000, 98, "other bidder")
      application.hasShownSniperIsBidding()

      auctionServer.hasReceivedBid(1098, SNIPER_XMPP_ID)

      auctionServer.announceClosed()
      application.isShowingSniperHasLostAuction()
    }
  }
}
