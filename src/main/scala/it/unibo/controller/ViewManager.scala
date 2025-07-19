package it.unibo.controller

import it.unibo.view.ControllableView

import scala.swing.Swing.onEDT

trait ViewManager[V <: ControllableView]:
  private var _view: Option[V] = None

  final def attachView(v: V): Unit = _view = Some(v)
  
  protected def exec(op: => Unit): Unit = onEDT(op)

  final private def applyToView(viewAction: V => Unit): Unit = exec:
    _view foreach viewAction
    
  def view: Option[V] = _view

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
      v.resetStartStopButton()
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
