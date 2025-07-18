package it.unibo.controller

import it.unibo.model.scenario.{EmptyScenario, Scenario}
import it.unibo.view.{MockView, View}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TestViewManager extends AnyFlatSpec with Matchers {

  // TODO: view manager test

  object TestManager extends ViewManager {
    def exposedView: this.View.type = this.View
  }

//  "ViewManager.View" should "call repaint on update" in {
//    val mockController = new MockDisplayableController
//    val mockView = new MockView(mockController, 0, 0)
//    TestManager.attachView(mockView)
//    TestManager.exposedView.update()
//    mockView.repaintCalled shouldBe true
//  }
//
//  it should "call disable controls" in {
//    val mockController = new MockDisplayableController
//    val mockView = new MockView(mockController, 0, 0)
//    TestManager.attachView(mockView)
//    TestManager.exposedView.disableControls()
//    mockView.disableStepButtonCalled shouldBe true
//    mockView.disableResetButtonCalled shouldBe true
//    mockView.disableStartStopButtonCalled shouldBe true
//  }
//
//  it should "call pause and firstStep" in {
//    val mockController = new MockDisplayableController
//    val mockView = new MockView(mockController, 0, 0)
//    TestManager.attachView(mockView)
//    TestManager.exposedView.pause()
//    mockView.enableStepButtonCalled shouldBe true
//    TestManager.exposedView.firstStep()
//    mockView.enableResetButtonCalled shouldBe true
//  }
//
//  it should "call resume" in {
//    val mockController = new MockDisplayableController
//    val mockView = new MockView(mockController, 0, 0)
//    TestManager.attachView(mockView)
//    TestManager.exposedView.resume()
//    mockView.enableStartStopButtonCalled shouldBe true
//    mockView.enableResetButtonCalled shouldBe true
//    mockView.disableStepButtonCalled shouldBe true
//  }
//
//  it should "call reset" in {
//    val mockController = new MockDisplayableController
//    val mockView = new MockView(mockController, 0, 0)
//    TestManager.attachView(mockView)
//    TestManager.exposedView.reset()
//    mockView.disableResetButtonCalled shouldBe true
//    mockView.resetStartStopButtonCalled shouldBe true
//    mockView.enableStartStopButtonCalled shouldBe true
//    mockView.enableStepButtonCalled shouldBe true
//  }
//
//  it should "call over" in {
//    val mockController = new MockDisplayableController
//    val mockView = new MockView(mockController, 0, 0)
//    TestManager.attachView(mockView)
//    TestManager.exposedView.over()
//    mockView.disableStepButtonCalled shouldBe true
//    mockView.disableStartStopButtonCalled shouldBe true
//    mockView.showInfoMessageCalled shouldBe true
//    mockView.showInfoMessageArgs._2 shouldBe "End of plan"
//  }
//
//  it should "call noPlanFound" in {
//    val mockController = new MockDisplayableController
//    val mockView = new MockView(mockController, 0, 0)
//    TestManager.attachView(mockView)
//    val msg = "No plan"
//    TestManager.exposedView.noPlanFound(msg)
//    mockView.closeLoadingDialogCalled shouldBe true
//    mockView.showErrorMessageCalled shouldBe true
//    mockView.showErrorMessageArgs._1 shouldBe msg
//    mockView.showErrorMessageArgs._2 shouldBe "No plan found"
//  }
//
//  it should "call planFound" in {
//    val mockController = new MockDisplayableController
//    val mockView = new MockView(mockController, 0, 0)
//    TestManager.attachView(mockView)
//    val msg = "Found"
//    TestManager.exposedView.planFound(msg)
//    mockView.closeLoadingDialogCalled shouldBe true
//    mockView.enableStepButtonCalled shouldBe true
//    mockView.enableStartStopButtonCalled shouldBe true
//    mockView.showInfoMessageCalled shouldBe true
//    mockView.showInfoMessageArgs._1 shouldBe msg
//    mockView.showInfoMessageArgs._2 shouldBe "Plan found"
//  }
//
//  it should "call search" in {
//    val mockController = new MockDisplayableController
//    val mockView = new MockView(mockController, 0, 0)
//    TestManager.attachView(mockView)
//    TestManager.exposedView.search()
//    mockView.showLoadingDialogCalled shouldBe true
//    mockView.showLoadingDialogArg shouldBe "Searching a plan..."
//  }
//
//  it should "call firstScenarioChoice" in {
//    val mockController = new MockDisplayableController
//    val mockView = new MockView(mockController, 0, 0)
//    TestManager.attachView(mockView)
//    TestManager.exposedView.firstScenarioChoice()
//    mockView.enableAlgorithmDropdownCalled shouldBe true
//    mockView.enableRefreshScenarioButtonCalled shouldBe true
//  }
}