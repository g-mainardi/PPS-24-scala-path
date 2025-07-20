# Testing

### TDD
We tried to adopt TDD when it felt natural to do so, and since we have done many refactorings the tests kept evolving with the code, in order to never be without coverage and feedbacks. We have to admit that it wasn't easy to follow the TDD approach because often we weren't sure on how to do things and how to write code, and writing a-priori tests sometimes has helped with that, sometimes it was just hard. 

### Coverage
At the start of the project, we set a goal of 60-70% of test coverage. We achievede that goal reaching 75%. 
We have focused more on quality than quantity, insted of writing a lot of tests we have tried to write good tests that actually test important feature of the application. In fact, many bugs have been discovered this way. 

We also managed to reach a pretty good coverage with fews tests, because we have tested all the features that involves important pieces of code and the integration between them. We tried to test all crucial features, what haven't been tested is often utiliy code that is not involved in main functionalities. We have raised the coverage also by building a `ViewMock` to test the `Controller`'s behavior. However, we haven't deeply tested the `View`.

<p align="center">
  <img src="../resources/coverage.png" alt="Coverage" title="Coverage" />
</p>

### Grid DSL
Lastly, one of us has built a graphical DSL to enhance Scenario's tests. The DSL uses some specific characters to build scenarios graphically in an ASCII-art like style, and then use those scenarios in tests. 
```scala
class TestScenarioWithClosedWalls extends Scenario(10, 10):
  private val p = Position(7, 8)
  override def generate(): Unit =
    _tiles = grid:
      F | F | F | F | F | F | F | F | F | F ||;
      F | F | TP(p) | F | F | F | F | F | F | F ||;
      F | F | F | F | F | F | F | F | F | F ||;
      F | F | F | F | F | F | F | F | F | F ||;
      F | F | F | F | F | F | F | F | F | F ||;
      F | F | F | F | F | F | F | F | F | F ||;
      F | F | F | F | F | F | W | W | W | W ||;
      F | F | F | F | F | F | W | F | F | W ||;
      F | F | F | F | F | F | W | F | F | W ||;
      F | F | F | F | F | F | W | W | W | W ||  
```


| [Previous Chapter](../6-implementation/index.md) | [Index](../index.md) | [Next Chapter](../8-process/index.md) |