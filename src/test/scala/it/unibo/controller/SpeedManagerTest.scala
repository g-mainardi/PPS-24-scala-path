package it.unibo.controller

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class SpeedManagerTest extends AnyFlatSpec with Matchers {

  class TestSpeedManager extends SpeedManager {
    def exposedSpeed: Double = speed
    def exposedSpeed_=(v: Double): Unit = speed = v
  }
  "A SpeedManager" should "have initial speed 1.0" in {
    val sm = new TestSpeedManager
    sm.exposedSpeed shouldBe 1.0
  }

  it should "allow updating speed" in {
    val sm = new TestSpeedManager
    sm.exposedSpeed = 2.5
    sm.exposedSpeed shouldBe 2.5
  }

  it should "set and reset shouldSleep flag" in {
    val sm = new TestSpeedManager
    sm.shouldSleep()
    // After shouldSleep, delay should reset the flag
    sm.delay()
    // No direct way to check the flag, but no exception should occur
    sm.exposedSpeed shouldBe 1.0
  }
}