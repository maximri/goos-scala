package com.wix.training


import org.specs2.mutable.{After, Specification}
import org.specs2.specification.Scope

class AuctionSniperEndToEndTest extends Specification{

  trait ctx extends Scope with After {
    val auction = new FakeAuctionServer("item-54321")
    val application = new ApplicationRunner

    def after = {
      auction.stop()
      application.stop()
    }
  }

  "Sniper" should {
    "joins auction until auction closes" in new ctx{
      auction.stratSellingItem()
      application.startBiddingIn(auction)
      auction.hasReceivedJoinRequestFromSniper
      auction.announceClosed
      application showsSniperHasLostAuction
    }
  }
}
