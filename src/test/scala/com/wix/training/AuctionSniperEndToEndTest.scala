package com.wix.training

import com.wix.training.ApplicationRunner.SNIPER_XMPP_ID
import org.specs2.mutable.{After, SpecificationWithJUnit}
import org.specs2.specification.Scope

class AuctionSniperEndToEndTest extends SpecificationWithJUnit{
  sequential

  trait ctx extends Scope with After {
    val sniperId: String = "item_54321"
    val auction = new FakeAuctionServer(sniperId)
    val application = new ApplicationRunner

   override def after = {
      auction.stop()
      application.stop()
    }
  }

  "Sniper" should {
    "joins auction until auction closes" in new ctx{
      auction.startSellingItem()
      application.startBiddingIn(auction)
      auction.hasReceivedJoinRequestFromSniper(SNIPER_XMPP_ID)
      auction.announceClosed
      application showsSniperHasLostAuction
    }

    "make a higher bid but lose" in new ctx {
      auction.startSellingItem()

      application.startBiddingIn(auction)
      auction.hasReceivedJoinRequestFromSniper(SNIPER_XMPP_ID)

      auction.reportPrice(1000, 98, "other bidder")
      application.showsSniperIsBidding()

      auction.hasReceivedBid(1098, SNIPER_XMPP_ID)

      auction.announceClosed()
      application.showsSniperHasLostAuction()
    }
  }
}
