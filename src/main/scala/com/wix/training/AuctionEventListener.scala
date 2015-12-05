package com.wix.training

trait AuctionEventListener {
  def currentPrice(price: Int, increment: Int): Unit
  def auctionClosed(): Unit
}
