package com.wix.training

import javax.swing.SwingUtilities

class Main(args: String*) {
  var ui: MainWindow = _

  startUserInterface

  private def startUserInterface() = {
    SwingUtilities.invokeAndWait(new Runnable {
      override def run() {
        ui = new MainWindow()
      }
    })
  }
}

object Main {

  val STATUS_JOINING = "Joining"

  val SNIPER_STATUS_NAME = "sniper status"

  val STATUS_LOST = "lost"

}
