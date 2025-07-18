package it.unibo.view

import it.unibo.controller.DisplayableController
import it.unibo.view.ViewPanels.{ControlPanel, ScenarioSettingsPanel}
import it.unibo.view.ViewUtilities.showPopupMessage

import scala.swing.{Dialog, MainFrame}

trait ControllableView(controller: DisplayableController) extends MainFrame:
  protected var loadingDialog: Option[Dialog] = None
  protected val controlPanel = ControlPanel()
  protected val scenarioSettingsPanel = ScenarioSettingsPanel(controller)

  def enableStepButton(): Unit = controlPanel.stepButton.enabled = true

  def disableStepButton(): Unit = controlPanel.stepButton.enabled = false

  def enableResetButton(): Unit = controlPanel.resetButton.enabled = true

  def disableResetButton(): Unit = controlPanel.resetButton.enabled = false

  def resetStartStopButton(): Unit = controlPanel.startStopButton.reset()

  def enableStartStopButton(): Unit = controlPanel.startStopButton.enabled = true

  def disableStartStopButton(): Unit = controlPanel.startStopButton.enabled = false

  def enableRefreshScenarioButton(): Unit = scenarioSettingsPanel.refreshScenarioButton.enabled = true

  def enableAlgorithmDropdown(): Unit = scenarioSettingsPanel.algorithmDropdown.enabled = true

  def resetAlgorithmDropdown(): Unit = scenarioSettingsPanel.algorithmDropdown.reset()

  def showInfoMessage(message: String, title: String): Unit =
    showPopupMessage(message, title, Dialog.Message.Info)

  def showErrorMessage(message: String, title: String): Unit =
    showPopupMessage(message, title, Dialog.Message.Error)

  def showLoadingDialog(message: String): Unit =
    loadingDialog = Some(ViewUtilities.showLoadingDialog(message))

  def closeLoadingDialog(): Unit = loadingDialog match
    case Some(dialog) => ViewUtilities closeLoadingDialog dialog
    case None => throw IllegalStateException("No loading dialog to close")

