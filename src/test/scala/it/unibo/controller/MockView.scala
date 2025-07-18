
package it.unibo.view

import it.unibo.controller.DisplayableController



class MockView(
                controller: DisplayableController,
                gridOffset: Int,
                cellSize: Int
              ) extends View(controller, gridOffset, cellSize) {  var repaintCalled = false
  var disableStepButtonCalled = false
  var disableResetButtonCalled = false
  var disableStartStopButtonCalled = false
  var enableStepButtonCalled = false
  var enableResetButtonCalled = false
  var enableStartStopButtonCalled = false
  var resetStartStopButtonCalled = false
  var showInfoMessageCalled = false
  var showInfoMessageArgs: (String, String) = ("", "")
  var showErrorMessageCalled = false
  var showErrorMessageArgs: (String, String) = ("", "")
  var showLoadingDialogCalled = false
  var showLoadingDialogArg: String = ""
  var closeLoadingDialogCalled = false
  var enableAlgorithmDropdownCalled = false
  var enableRefreshScenarioButtonCalled = false

  override def repaint(): Unit = repaintCalled = true
  override def disableStepButton(): Unit = disableStepButtonCalled = true
  override def disableResetButton(): Unit = disableResetButtonCalled = true
  override def disableStartStopButton(): Unit = disableStartStopButtonCalled = true
  override def enableStepButton(): Unit = enableStepButtonCalled = true
  override def enableResetButton(): Unit = enableResetButtonCalled = true
  override def enableStartStopButton(): Unit = enableStartStopButtonCalled = true
  override def resetStartStopButton(): Unit = resetStartStopButtonCalled = true
  override def showInfoMessage(msg: String, title: String): Unit = {
    showInfoMessageCalled = true
    showInfoMessageArgs = (msg, title)
  }
  override def showErrorMessage(msg: String, title: String): Unit = {
    showErrorMessageCalled = true
    showErrorMessageArgs = (msg, title)
  }
  override def showLoadingDialog(msg: String): Unit = {
    showLoadingDialogCalled = true
    showLoadingDialogArg = msg
  }
  override def closeLoadingDialog(): Unit = closeLoadingDialogCalled = true
  override def enableAlgorithmDropdown(): Unit = enableAlgorithmDropdownCalled = true
  override def enableRefreshScenarioButton(): Unit = enableRefreshScenarioButtonCalled = true
}