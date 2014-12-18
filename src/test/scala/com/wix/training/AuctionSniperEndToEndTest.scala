package com.wix.training

import org.specs2.mutable.{After, SpecificationWithJUnit}
import org.specs2.specification.Scope

class AuctionSniperEndToEndTest extends SpecificationWithJUnit{
  sequential

  trait ctx extends Scope with After {
    val auction = new FakeAuctionServer("item-54321")
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
      auction.hasReceivedJoinRequestFromSniper
      auction.announceClosed
      application showsSniperHasLostAuction
    }
  }
}
