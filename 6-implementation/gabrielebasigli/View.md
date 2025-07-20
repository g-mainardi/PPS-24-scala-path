# La view
La View ha il compito di presentare lo stato dello scenario e dell'agente all’utente e di aggiornarsi in base ai cambiamenti provenienti dal controller. Inoltre, intercetta gli input dell’utente (come lo spostamento di goal e start) e li inoltra al controller, senza applicare alcuna logica di elaborazione o trasformazione. In questo modo, la View rimane separata dalla logica applicativa, rispettando il principio di separazione delle responsabilità proprio dell’architettura MVC.
L'interfaccia `ControllableView` definisce ed espone tutti i metodi che sono richiamabili dal controller.
```scala
trait ControllableView(controller: DisplayableController) extends MainFrame:
  protected var loadingDialog: Option[Dialog] = None
  protected val controlPanel = ControlPanel()
  protected val scenarioSettingsPanel = ScenarioSettingsPanel(controller)
  def enableStepButton(): Unit = controlPanel.stepButton.enabled = true
  def disableStepButton(): Unit = controlPanel.stepButton.enabled = false
  def enableResetButton(): Unit = controlPanel.resetButton.enabled = true
  def disableResetButton(): Unit = controlPanel.resetButton.enabled = false
  def resetStartStopButton(): Unit = controlPanel.startStopButton.reset()
  def enableStartStopButton(): Unit = controlPanel.startStopButton.enabled = true
  def disableStartStopButton(): Unit = controlPanel.startStopButton.enabled = false
  def enableRefreshScenarioButton(): Unit = scenarioSettingsPanel.refreshScenarioButton.enabled = true
  def enableAlgorithmDropdown(): Unit = scenarioSettingsPanel.algorithmDropdown.enabled = true
  def resetAlgorithmDropdown(): Unit = scenarioSettingsPanel.algorithmDropdown.reset()
  def showInfoMessage(message: String, title: String): Unit =
    showPopupMessage(message, title, Dialog.Message.Info)
  def showErrorMessage(message: String, title: String): Unit =
    showPopupMessage(message, title, Dialog.Message.Error)
  def showLoadingDialog(message: String): Unit =
    loadingDialog = Some(ViewUtilities.showLoadingDialog(message))
  def closeLoadingDialog(): Unit = loadingDialog match
    case Some(dialog) => ViewUtilities closeLoadingDialog dialog
    case None => throw IllegalStateException("No loading dialog to close")
```
Il componente principale è la classe `View`, la quale estende il trait `ControllableView`. Questa classe si limita a mettere insieme i componenti definiti in `ViewUtilities`, `ViewPanels` (secondo un certo layout) e definisce la griglia, ovvero il componente su cui viene mostrato lo oscenario.
La griglia rimane in ascolto per eventuali click da parte dell'utente per spostare il goal o il punto di partenza e segnala la nuova posizione direttamente al controller. Sarà poi il controller che dirà alla view di ridisegnare il proprio stato (e quindi la nuova posizone di start e goal). Il `paintComponent` si occupa di disegnare le celle, la griglia, il goal, lo start, il percorso che compie l'agente e la posizione attuale dell'agente. 
```scala
class View(controller: DisplayableController, gridOffset: Int, cellSize: Int)
  extends ControllableView(controller):
  private val gridPanel: Panel = new Panel:
    preferredSize = new Dimension(200, 100)
    listenTo(mouse.clicks)
    reactions += {
      case e: event.MouseClicked =>
        val x = (e.point.x - gridOffset) / cellSize
        val y = (e.point.y - gridOffset) / cellSize
        if x >= 0 && y >= 0 && x < controller.scenario.nCols && y < controller.scenario.nRows then
          if scenarioSettingsPanel.moveGoalRadio.selected then {
            Simulation set UICommand.SetPosition(Simulation.SettablePosition.Goal(x, y))
          } else if scenarioSettingsPanel.moveStartRadio.selected then
            Simulation set UICommand.SetPosition(Simulation.SettablePosition.Init(x, y))
    }
    override def paintComponent(g: Graphics2D): Unit =
      super.paintComponent(g)
      given Graphics2D = g
      var tiles = controller.scenario.tiles
      drawCells(cellSize, gridOffset, tiles)
      drawGrid(cellSize, gridOffset, tiles)
      drawCircle(controller.goal.x, controller.goal.y, Color.RED, cellSize, gridOffset)
      drawCircle(controller.init.x, controller.init.y, Color.BLUE, cellSize, gridOffset)
      controller.agent foreach: agent =>
        drawCircle(agent.x, agent.y, Color.YELLOW, cellSize, gridOffset)
        drawPath(agent.path, cellSize, gridOffset)
        controlPanel.remainingSteps.text = agent.remainingSteps.toString

  contents = new BorderPanel:
    import BorderPanel.Position
    layout(gridPanel) = Position.Center
    layout(controlPanel) = Position.South
    layout(scenarioSettingsPanel) = Position.North
    layout(directionGrid) = Position.West
```

## ViewPanels
Contiene la definizione dei pannelli utilizzati dalla `View`. Tra questi, il `ControlPanel`, è responsabile della gestione dell'interazione dell'utente con la simulazione. Questo pannello fornisce i pulsanti Start, Stop, Step e uno slider per regolare la velocità della simulazione. Tali controlli non contengono logica interna, ma si limitano a notificare il controller, che gestisce la logica applicativa sottostante. 

Il pannello `ScenarioSettingsPanel`, anch'esso definito all'interno del modulo `ViewPanels`, permette di configurare lo scenario della simulazione. Al suo interno sono presenti controlli per selezionare lo scenario attivo, il tipo di algoritmo da utilizzare per il calcolo del percorso e un pulsante per rigenerare lo scenario. Ancora una volta, tutte le azioni effettuate dall'utente tramite questo pannello vengono inoltrate al controller, che si occupa dell’elaborazione e dell’aggiornamento dello stato del modello, come da principio MVC.

## ViewUtilities
Questo modulo raccoglie un insieme di classi, utility grafiche e componenti riutilizzabili pensati per supportare la `View`.
Al suo interno si trovano metodi per il disegno di celle, griglie, percorsi (con frecce orientate secondo le direzioni cardinali e diagonali), nonché per la gestione di dialoghi (come popup informativi o di caricamento). Vengono inoltre definite alcune classi grafiche personalizzate, tra cui bottoni a due stati, pulsanti di selezione, caselle di testo che accettano solo numeri, e combo box con placeholder illustrata nel codice seguente. 
```scala
class ComboBoxWithPlaceholder[A](placeholder: String, items: Seq[A], onSelect: Int => Unit) extends ComboBox(Seq(placeholder) ++ items):
    selection.index = 0
    listenTo(selection)
    reactions += {
      case SelectionChanged(_) =>
        if selection.index == 0 && peer.getItemAt(0) == placeholder then
          ()
        else
          if selection.index > 0 && peer.getItemAt(0) == placeholder then
            val selected = selection.item
            selection.index -= 1
            peer.setModel(ComboBox.newConstantModel(items))
            selection.item = selected
          onSelect(selection.index)
    }
    def reset(): Unit =
      deafTo(selection)
      peer.setModel(ComboBox.newConstantModel(Seq(placeholder) ++ items))
      selection.index = 0
      listenTo(selection)
```
La flessibilità del linguaggio Scala nel trattare le funzioni come valori di primo ordine ha reso più facile la creazione di componenti GUI riutilizzabili e personalizzabili, come la classe SelectionButton. In questo caso, i comportamenti associati ai due stati del pulsante non sono hardcoded all'interno della classe, ma vengono invece passati come funzioni al momento di creazione dell'istanza. 
```scala
class SelectionButton(label1: String = "", label2: String = "", onState1:() => Unit = () => {}, onState2: () => Unit = () => {}) extends Button(label1):
    private var state = true // true: label1, false: label2
    reactions += {
      case ButtonClicked(_) =>
        if state then
          onState1()
          text = label2
          background = Color.LIGHT_GRAY
        else
          onState2()
          text = label1
          background = null
        state = !state
    }
```

[Index](../index.md)
