package com.wix.training

import com.objogate.wl.swing.AWTEventQueueProber
import com.objogate.wl.swing.driver.ComponentDriver._
import com.objogate.wl.swing.driver.{JFrameDriver, JLabelDriver}
import com.objogate.wl.swing.gesture.GesturePerformer
import org.hamcrest.Matchers.equalTo


class AuctionSniperDriver(timeoutMillis: Int) extends JFrameDriver(
  new GesturePerformer(),
  JFrameDriver.topLevelFrame(named(MainWindow.MAIN_WINDOW_NAME),
    showingOnScreen()),
  new AWTEventQueueProber(timeoutMillis, 100)) {

  def isShowingSniperStatus(statusText: String) = {
    new JLabelDriver(
      this, named(Main.SNIPER_STATUS_NAME)).hasText(equalTo(statusText))
  }
}