package com.wix.training

import java.awt.Color
import javax.swing.border.LineBorder
import javax.swing.{JLabel, JFrame}

import com.wix.training.MainWindow.{MAIN_WINDOW_NAME, APP_TITLE}

class MainWindow() extends JFrame(APP_TITLE){
  private val sniperStatus:JLabel = createLabel(Main.STATUS_JOINING)


  setName(MAIN_WINDOW_NAME)
  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
  add(sniperStatus)


  setVisible(true)


  def createLabel(initialText: String): JLabel = {
    val result = new JLabel(initialText)
    result.setName(MainWindow.SNIPER_STATUS_NAME)
    result.setBorder(new LineBorder(Color.BLACK))
    result
  }

}

object MainWindow {
  val MAIN_WINDOW_NAME = "Auction Sniper Main"
  val APP_TITLE = "Auction Sniper"
  val SNIPER_STATUS_NAME = "sniper-status"
  //  val SNIPERS_TABLE_NAME = "Snipers Table"
  //  val NEW_ITEM_ID_NAME = "new-item-id"
  //  val NEW_ITEM_STOP_PRICE_NAME = "new-item-stop-price"
  //  val JOIN_BUTTON_NAME = "join-button"
}
