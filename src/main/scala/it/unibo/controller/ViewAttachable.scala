package it.unibo.controller

import it.unibo.view.View

import scala.swing.Swing.onEDT

trait ViewAttachable:
  private var _view: Option[View] = None

  final def attachView(v: View): Unit = _view = Some(v)
  
  final protected def applyToView(viewAction: View => Unit): Unit = onEDT:
    _view foreach viewAction
    
  protected def updateView(): Unit = applyToView: v =>
    v.repaint()
    
  protected def disableControls(): Unit = applyToView: v =>
    v.disableStepButton()
    v.disableResetButton()
    v.disableStartButton()
    v.disablePauseResumeButton()
