package it.unibo.u12lab.code

import it.unibo.u12lab.code.Scala2P.{*, given}
import alice.tuprolog.{SolveInfo, Term}

import scala.swing.*
import scala.swing.event.*
import java.awt.{BasicStroke, Color, Graphics2D}
import java.awt.geom.{Ellipse2D, Rectangle2D}
import javax.swing.{JSpinner, SpinnerNumberModel, Timer}

class OffButton(text: String) extends Button(text):
  enabled = false

//noinspection ScalaWeakerAccess
object RobotPlannerGUI extends SimpleSwingApplication:
  given Conversion[String, Term]   = Term.createTerm(_)

  // GUI constants
  val GRID_SIZE: Int = 4; val CELL_SIZE: Int = 80; val CELL_OFFSET = 10
  val WINDOW_WIDTH: Int = GRID_SIZE * CELL_SIZE + 300
  val WINDOW_HEIGHT: Int = GRID_SIZE * CELL_SIZE + 300

  type Command = String
  type Plan = LazyList[Command]

  // Robot state
  var robotX = 0
  var robotY = 0
  var currentPlan: Plan = LazyList()
  var currentStep = 0
  var isExecuting = false

  // Logic Setup
  val INITIAL_MAX_MOVES = 6
  var plans: LazyList[Plan] = LazyList()
  
  // Engine Prolog
  val prologTheory: String = loadPrologTheory("src/main/prolog/planner.pl")
  val engine: Term => LazyList[SolveInfo] = mkPrologEngine(prologTheory)

  def currentPos: String = s"s($robotX, $robotY)"

  type Position = (Int, Int)
  extension (l: LazyList[SolveInfo])
    def getOutputPositions: LazyList[Position] = l map :
      s => (extractTerm(s, "X").toString.toInt, extractTerm(s, "Y").toString.toInt)
    def getFirstOutputPos: Position = l.getOutputPositions.head
    def setOutputPos(): Unit =
      val (x, y) = l.getFirstOutputPos
      robotX = x; robotY = y

  def allPositions: LazyList[Position]        = engine("validpos(s(X,Y))").getOutputPositions
  def goalPosition: Position                  = engine("goal(s(X,Y))").getFirstOutputPos
  def loosePositions: LazyList[Position]      = engine("loose(s(X,Y))").getOutputPositions
  def trampolinePositions: LazyList[Position] = engine("trampoline(s(X,Y), _)").getOutputPositions
  
  def checkTrampoline(): Unit = engine(s"trampoline($currentPos, s(X,Y))") match
    case l: LazyList[SolveInfo] if l.nonEmpty => l.setOutputPos()
    case _ => ()

  object MoveCommands:
    val moves: Set[Command] = 
      engine("move(Dirs, s(1,1), _)") extractSolutionsOf "Dirs" to Set
    val jumps: Set[Command] =
      (engine("jump(Dirs, s(1,1), _)") concat engine("jump(Dirs, s(2,2), _)")) extractSolutionsOf "Dirs" to Set

    def unapply(s: Command): Option[String] =
      if moves contains s then Some("move")
      else if jumps contains s then Some("jump")
      else None

  def executeCommand(cmd: Command): Unit = cmd match
    case MoveCommands(pred) => engine(s"$pred($cmd, $currentPos, s(X, Y))").setOutputPos()

  object NoPlansException extends Exception
  def generatePlans(maxMoves: Int): Unit =
    println(s"Generating plan with max $maxMoves moves...")
    val results: LazyList[SolveInfo] = engine(s"planV2($maxMoves, Plan)")
    if results.isEmpty then throw NoPlansException
    results foreach { r => if !r.isSuccess then throw NoPlansException }
    plans = results extractSolutionsOf "Plan" map {extractListFromTerm(_)}
//    println(s"I have generated ${plans.size} plans!")
    println(s"The plans are $plans")

  def top: MainFrame = new MainFrame:
    title = "Robot Planner GUI"
    preferredSize = new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT)
    
    val gridPanel: Panel = new Panel:
      override def paintComponent(g: Graphics2D): Unit =
        super.paintComponent(g)
        drawGridLines(g)
        drawSpecialCells(g)
        drawRobot(g)
        drawGridLabels(g)

    object ControlPanel extends FlowPanel:
      val planButton = new Button(s"Plan with max number of moves:")
      val maxMoves = SpinnerNumberModel(INITIAL_MAX_MOVES, 0, 100, 1)
      val movesNSetter: Component = new Component:
        override lazy val peer = JSpinner(maxMoves)
      val switchPlanButton: Button = OffButton("Switch Plan")
      val stepButton: Button = OffButton("Step")
      val executeButton: Button = OffButton("Execute Plan")
      contents ++= List(planButton, movesNSetter, switchPlanButton, stepButton, executeButton)
    import ControlPanel.*
    contents = new BorderPanel:
      import BorderPanel.Position
      add(gridPanel, Position.Center)
      add(ControlPanel, Position.South)

    // Event handlers
    listenTo(planButton, switchPlanButton, stepButton, executeButton)
    reactions += {
      case ButtonClicked(`planButton`) =>
        switchPlanButton.enabled = false
        stepButton.enabled = false
        executeButton.enabled = false
        try
          generatePlans(maxMoves.getValue.asInstanceOf[Int])
          initPlan()
          switchPlanButton.enabled = true
        catch
          case NoPlansException => println("No valid solutions found!"); plans = LazyList()
          case e: Exception => println(s"Error: ${e.getMessage}"); plans = LazyList()
      case ButtonClicked(`switchPlanButton`) =>
        initPlan()
      case ButtonClicked(`stepButton`) =>
        executeStep()
        executeButton.enabled = false
      case ButtonClicked(`executeButton`) =>
        if !isExecuting then
          startPlanExecution()
          stepButton.enabled = false
          executeButton.enabled = false
    }
    
    def drawGridLines(g: Graphics2D): Unit =
      g setColor Color.BLACK
      g setStroke new BasicStroke(2)
      0 to GRID_SIZE foreach { i =>
        val pos = i * CELL_SIZE + 50
        g drawLine(50, pos, GRID_SIZE * CELL_SIZE + 50, pos)
        g drawLine(pos, 50, pos, GRID_SIZE * CELL_SIZE + 50)
      }

    def drawGridLabels(g: Graphics2D): Unit =
      g setColor Color.GRAY
      allPositions foreach { (x, y) =>
        val xPos = y * CELL_SIZE + 55
        val yPos = x * CELL_SIZE + 65
        g drawString(s"($x,$y)", xPos, yPos)
      }

    def drawSpecialCells(g: Graphics2D): Unit =
      def makeCell(x: Int, y: Int): Rectangle2D =
        new Rectangle2D.Double(y * CELL_SIZE + 55, x * CELL_SIZE + 55, CELL_SIZE - CELL_OFFSET, CELL_SIZE - CELL_OFFSET)
      g setColor Color.GREEN
      val (goal_x, goal_y) = goalPosition
      g fill makeCell(goal_x, goal_y)

      g setColor Color.RED
      loosePositions foreach {g fill makeCell(_, _)}

      g setColor Color.BLUE
      trampolinePositions foreach {g fill makeCell(_, _)}

    def drawRobot(g: Graphics2D): Unit =
      g setColor Color.ORANGE
      val robotCircle = new Ellipse2D.Double(
        robotY * CELL_SIZE + 65, 
        robotX * CELL_SIZE + 65, 
        CELL_SIZE - 30, 
        CELL_SIZE - 30
      )
      g fill robotCircle
      
      g setColor Color.BLACK
      g setStroke new BasicStroke(2)
      g draw robotCircle

    def initPlan(): Unit =
      currentStep = 0
      resetRobot()
      if plans.nonEmpty
      then
        currentPlan = plans.head
        plans = plans.tail
        println(s"Current plan: ${currentPlan.mkString(", ")}")
        stepButton.enabled = true
        executeButton.enabled = true
      else
        println(s"There are no more plans...")
        switchPlanButton.enabled = false

    def resetRobot(): Unit =
      robotX = 0
      robotY = 0
      isExecuting = false
      gridPanel.repaint()

    def executeStep(): Unit =
      if currentStep < currentPlan.length then
        val cmd: Command = currentPlan(currentStep)
        executeCommand(cmd)
        checkTrampoline()
        currentStep += 1
        println(s"Executed: $cmd ($currentStep/${currentPlan.length})")
        gridPanel.repaint()
        if currentStep >= currentPlan.length then
          println("Plan completed!")
          stepButton.enabled = false

    def startPlanExecution(): Unit =
      var timer: Option[Timer] = None
      if (currentPlan.nonEmpty && !isExecuting)
        isExecuting = true
        timer = Some{new Timer(1000, _ => execute())}
        timer foreach{_.start()}
      def execute(): Unit =
        if currentStep < currentPlan.length then executeStep()
        else
          timer foreach {_.stop()}
          isExecuting = false
