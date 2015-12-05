package com.wix.training

trait AuctionEventListener {
  def auctionClosed(): Unit
}
