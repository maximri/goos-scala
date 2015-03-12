package com.wix.training

import java.awt.Color
import javax.swing.border.LineBorder
import javax.swing.{JFrame, JLabel}

import com.wix.training.MainWindow._

class MainWindow() extends JFrame(APP_TITLE) {
  private val sniperStatus: JLabel = createLabel(Main.STATUS_JOINING)

  setName(MAIN_WINDOW_NAME)
  add(sniperStatus)
  pack()
  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
  setVisible(true)


  def createLabel(initialText: String): JLabel = {
    val result = new JLabel(initialText)
    result.setName(SNIPER_STATUS_NAME)
    result.setBorder(new LineBorder(Color.BLACK))
    result
  }

  def showStatus(status: String): Unit = {
    sniperStatus.setText(status)
  }
}

object MainWindow {
  val MAIN_WINDOW_NAME = "Auction Sniper Main"
  val APP_TITLE = "Auction Sniper"
  val SNIPER_STATUS_NAME = "sniper status"
}


