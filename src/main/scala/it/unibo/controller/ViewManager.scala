package it.unibo.controller

import it.unibo.view.View

import scala.swing.Swing.onEDT

trait ViewManager:
  private var _view: Option[View] = None

  final def attachView(v: View): Unit = _view = Some(v)
  
  final private def applyToView(viewAction: View => Unit): Unit = onEDT:
    _view foreach viewAction
    
  protected object View:
    def update(): Unit = applyToView: v =>
      v.repaint()
    
    def disableControls(): Unit = applyToView: v =>
      v.disableStepButton()
      v.disableResetButton()
      v.disableStartStopButton()

    def pause(): Unit = applyToView: v =>
      v.enableStepButton()
    
    def firstStep(): Unit = applyToView: v =>
      v.enableResetButton()
  
    def resume(): Unit = applyToView: v =>
      v.enableStartStopButton()
      v.enableResetButton()
      v.disableStepButton()
    
    def reset(): Unit = applyToView: v =>
      v.disableResetButton()
      v.enableStartStopButton()
      v.enableStepButton()
    
    def over(): Unit = applyToView: v =>
      v.disableStepButton()
      v.disableStartStopButton()
      v.showInfoMessage("Plan terminated! You can restart it or try a new one.", "End of plan")
        
    def noPlanFound(msg: String): Unit = applyToView: v =>
      v.closeLoadingDialog()
      v.showErrorMessage(msg, "No plan found")
        
    def planFound(msg: String): Unit = applyToView: v =>
      v.closeLoadingDialog()
      v.enableStepButton()
      v.enableStartStopButton()
      v.showInfoMessage(msg, "Plan found")
      
    def search(): Unit = applyToView: v =>
      v showLoadingDialog "Searching a plan..."

    def firstScenarioChoice(): Unit = applyToView: v =>
      v.enableAlgorithmDropdown()
      v.enableRefreshScenarioButton()
