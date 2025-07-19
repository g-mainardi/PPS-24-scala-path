package it.unibo.controller

import it.unibo.view.MockControllableView
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TestViewManager extends AnyFlatSpec with Matchers:

  class ViewManagerMock extends ViewManager[MockControllableView]:
    override protected def exec(op: => Unit): Unit = op
    export View.*
  
  def setup: (ViewManagerMock, MockControllableView) =
    val mockVM = new ViewManagerMock
    val mockView = new MockControllableView(new MockDisplayableController)
    mockVM attachView mockView
    (mockVM, mockView)

  "ViewManager" should "" in :
    val (mockVM, mockView) = setup

    mockVM.view should not be empty

  "ViewManager.View" should "call repaint on update" in :
    val (mockVM, mockView) = setup
    
    mockVM.update()
    mockView.repaintCalled shouldBe true

  it should "call disable controls" in :
    val (mockVM, mockView) = setup

    mockView.disableStepButtonCalled shouldBe false
    mockView.disableResetButtonCalled shouldBe false
    mockView.disableStartStopButtonCalled shouldBe false

    mockVM.disableControls()
    mockView.disableStepButtonCalled shouldBe true
    mockView.disableResetButtonCalled shouldBe true
    mockView.disableStartStopButtonCalled shouldBe true

  it should "call pause and firstStep" in :
    val (mockVM, mockView) = setup

    mockVM.pause()
    mockView.enableStepButtonCalled shouldBe true
    mockVM.firstStep()
    mockView.enableResetButtonCalled shouldBe true

  it should "call resume" in :
    val (mockVM, mockView) = setup
    
    mockVM.resume()
    mockView.enableStartStopButtonCalled shouldBe true
    mockView.enableResetButtonCalled shouldBe true
    mockView.disableStepButtonCalled shouldBe true

  it should "call reset" in :
    val (mockVM, mockView) = setup

    mockVM.reset()
    mockView.disableResetButtonCalled shouldBe true
    mockView.resetStartStopButtonCalled shouldBe true
    mockView.enableStartStopButtonCalled shouldBe true
    mockView.enableStepButtonCalled shouldBe true

  it should "call over" in :
    val (mockVM, mockView) = setup

    mockVM.over()
    mockView.disableStepButtonCalled shouldBe true
    mockView.disableStartStopButtonCalled shouldBe true
    mockView.showInfoMessageCalled shouldBe true
    mockView.showInfoMessageArgs._2 shouldBe "End of plan"

  it should "call noPlanFound" in :
    val (mockVM, mockView) = setup

    val msg = "No plan"
    mockVM.noPlanFound(msg)
    mockView.closeLoadingDialogCalled shouldBe true
    mockView.showErrorMessageCalled shouldBe true
    mockView.showErrorMessageArgs._1 shouldBe msg
    mockView.showErrorMessageArgs._2 shouldBe "No plan found"

  it should "call planFound" in :
    val (mockVM, mockView) = setup

    val msg = "Found"
    mockVM planFound msg
    mockView.closeLoadingDialogCalled shouldBe true
    mockView.enableStepButtonCalled shouldBe true
    mockView.enableStartStopButtonCalled shouldBe true
    mockView.showInfoMessageCalled shouldBe true
    mockView.showInfoMessageArgs._1 shouldBe msg
    mockView.showInfoMessageArgs._2 shouldBe "Plan found"

  it should "call search" in :
    val (mockVM, mockView) = setup
    
    mockVM.search()
    mockView.showLoadingDialogCalled shouldBe true
    mockView.showLoadingDialogArg shouldBe "Searching a plan..."

  it should "call firstScenarioChoice" in :
    val (mockVM, mockView) = setup
    
    mockVM.firstScenarioChoice()
    mockView.enableAlgorithmDropdownCalled shouldBe true
    mockView.enableRefreshScenarioButtonCalled shouldBe true
