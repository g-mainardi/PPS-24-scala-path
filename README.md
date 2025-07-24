[![Test](https://github.com/g-mainardi/PPS-24-scala-path/actions/workflows/test.yml/badge.svg)](https://github.com/g-mainardi/PPS-24-scala-path/actions/workflows/test.yml)

[![Build](https://github.com/g-mainardi/PPS-24-scala-path/actions/workflows/build.yml/badge.svg)](https://github.com/g-mainardi/PPS-24-scala-path/actions/workflows/build.yml)

[![Test coverage](https://codecov.io/gh/g-mainardi/PPS-24-scala-path/graph/badge.svg?token=06P0TEB653)](https://codecov.io/gh/g-mainardi/PPS-24-scala-path)

<a href="https://g-mainardi.github.io/PPS-24-scala-path/"><img src="https://github.com/user-attachments/assets/787c57a9-c3ee-455a-826d-8b7eb1ed4d4b" alt="Pages badge" height="72"/></a>

<div align="center">
  <a href="https://github.com/g-mainardi/PPS-24-scala-path/">
    <img height="372" alt="ScalaPath icon" src="https://github.com/user-attachments/assets/f4e60231-b299-4653-a534-7166569ff729" />
  </a>
</div>

<div align="center">
  
  ## Pathfinding engine in Scala and Prolog 
  <img height="500" alt="ScalaPath app" src="https://github.com/user-attachments/assets/980a98d1-b5a4-4113-914a-0a54aef918fc" />
</div>

# ScalaPath guida utente

## Avvio

All’avvio dell’applicazione l’utente si ritroverà con con una griglia vuota, nessuno scenario selezionato e nessun algoritmo di ricerca selezionato. Le dimensioni di default della griglia sono 10x10 (colonne x righe). Prima di avviare la ricerca del percorso (che all’avvio si troverà bloccata), è necessario modificare le impostazioni della simulazione. 

## Cambio delle dimensioni dello scenario

Per cambiare le dimensioni dello scenario basterà selezionare e modificare i rispettivi campi in altro a sinistra, di fianco alla scritta “Dimensions”. Alla pressione del tasto invio, l’utente vedrà subito la griglia aggiornare la propria dimensione.

## Selezione dello scenario

Per selezionare uno scenario è necessario recarsi sulla selezione a tendina che contiene la scritta “Select scenario…”. Quando viene selezionato uno scenario (con un semplice click), questo viene immediatamente generato. Il tasto con l’icona di refresh viene abilitato se viene selezionato almeno uno scenario e permette di rigenerare lo scenario corrente. 

## Spostare il goal e start

L’utente ha la possibilità di spostare la posizione di start e goal semplicemente cliccando con il mouse sulla nuova posizione all’interno della griglia, avendo cura di selezionare a fianco alla scritta “change” se si intende spostare lo Start o il Goal.

## Selezione delle direzioni dell’agent

L’utente ha la possibilità di poter modificare le direzioni in cui l’agente può muoversi. Tramite i pulsanti sulla sinistra dello scenario. Di default tutte le direzioni sono abilitate, l’utente dovrà quindi selezionare quelle che vuole disabilitare.

## Selezione dell’algoritmo di ricerca

L’ultimo step necessario, per far partire la simulazione, è quello della selezione dell’algoritmo di ricerca. Una volta selezionato, parte il solver. In caso di successo nella ricerca del piano, comparirà un popup di conferma, da quel momento in poi sarà possibile far partire la simulazione.

## Controllo della simulazione

Una volta terminata la configurazione è possibile far partire la simulazione tramite il tasto “Start” e fermarla con il tasto “Stop”, è possibile inoltre controllare la velocità della simulazione tramite l’apposito slider in basso a destra. In alternativa, è possibile procedere passo per passo con la simulazione premendo il tasto “Step”. Nel caso di una modifica qualsiasi alle impostazioni della simulazione, per ricalcolare il percorso è necessario ri-selezionare l’algoritmo di ricerca dalla apposita dropdown.
