package it.unibo.prologintegration

import it.unibo.model.BasePlanner
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TestBasePlanner extends AnyFlatSpec with Matchers {
  "BasePlanner" should "find a valid path" in :
    val pathOpt = BasePlanner((0, 0), (2, 2), 5).plan
    pathOpt should not be None

  "BasePlanner" should "use all its moves" in:
    val pathOpt = BasePlanner((0, 0), (2, 2), 5).plan
    pathOpt.get.length shouldBe 5

  "BasePlanner" should "not find a valid path" in:
    val pathOpt = BasePlanner((0, 0), (10, 10), 3).plan
    pathOpt shouldBe None
}