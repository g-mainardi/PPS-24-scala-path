package it.unibo

import it.unibo.controller.ScalaPathController
import it.unibo.view.View
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.matchers.should.Matchers.shouldBe

import scala.swing.Frame

class TestScalaPath extends AnyFlatSpec with Matchers

//  "ScalaPath" should "initialize view with correct parameters" in:
//    val view = ScalaPath.view
//    view shouldBe a[View]

//  it should "have view as top frame" in:
//    val topFrame = ScalaPath.top
//    topFrame shouldBe ScalaPath.view
//    topFrame shouldBe a[Frame]
//
//  it should "attach view to controller" in:
//    ScalaPathController.view should not be empty
//
//  it should "be a SimpleSwingApplication" in:
//    ScalaPath shouldBe a[scala.swing.SimpleSwingApplication]
//
//  it should "start and stop controller" in :
//    noException should be thrownBy:
//
//      val controllerThread = new Thread(() => ScalaPath main Array.empty)
//      controllerThread.start()
//
//      Thread sleep 1000
//
//      controllerThread join 5000
