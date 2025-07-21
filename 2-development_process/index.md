# Development Process

## Scrum

La nostra strategia SCRUM-like ha seguito questi passaggi:
1. **Initial meeting**: l’incontro iniziale è durato 2 ore, durante il quale è stato assegnato il ruolo di *Product Owner*, incaricato di massimizzare il ritorno sull’investimento, a **Gabriele Basigli**; mentre **Giosuè Mainardi** ha assunto il ruolo di *Customer*, responsabile di fornire feedback sullo stato attuale del progetto. È stato anche definito un **Product Backlog**, un documento contenente ogni fase e la relativa difficoltà stimata necessaria per completare il prodotto.
2. **Sprint planning**: ogni sprint inizia con un meeting in cui viene discusso lo sprint precedente, per valutare il lavoro svolto, quello rimanente e come organizzarlo. Ogni sviluppatore sceglie i task dal *Product Backlog* su cui lavorare fino al successivo *Sprint Planning*.
3. **Standup meeting**: quotidianamente, il team si riunisce per dare aggiornamenti sul lavoro completato, in sospeso ed eventuale problematiche sorte.

---

## Version Control System
Prima dell’inizio dello sviluppo sono stati formalizzati degli standard di utilizzo per git e github, ovvero:

* **Branches**: lo sviluppo è stato suddiviso in diversi branch semantici, ognuno con una funzione e uno scopo precisi:
  * **main** branch: usato solo per il versionamento principale del progetto, sempre mantenuto in stato funzionante con tutti i test superati.
  * **develop** branch: usato per raccogliere il codice dai branch *feature* in una codebase comune, per testare il sistema nel suo insieme e risolvere conflitti.
  * **feature** branches: usati per sviluppare funzionalità specifiche, spesso da parte di un singolo sviluppatore.
  * **refactor** branches: usati per importanti refactor del codice o dell’architettura.
  * **gh-pages** branch: utilizzato esclusivamente per vesionare e gestire la documentazione del progetto.

* **Commit Messages**: per mantenere una cronologia dei commit ordinata e leggibile, abbiamo stabilito una nomenclatura precisa per i messaggi di commit:

  `type: message` dove "type" può essere uno dei seguenti:

  * **feat**: è stata aggiunta una nuova funzionalità
  * **fix**: è stato corretto un bug
  * **style**: è stato migliorato lo stile del codice
  * **refactor**: è stata riorganizzata la struttura del codice
  * **test**: aggiunta di nuovi test o correzione di test esistenti
  * **chore**: modifiche a librerie o configurazioni, manutenzione generale
  * **docs**: aggiornamenti alla documentazione

Inoltre, è stato utilizzato il **semantic versioning** per taggare specifici commit almeno alla fine di ogni sprint.  
Talvolta sono stati inseriti tag semantici anche a metà sprint per marcare uno stato funzionante del progetto.


### Automation
Come build tool è stato scelto `SBT` per la sua facilità d’uso con il linguaggio Scala.  
La **Continuous Integration** è stata implementata tramite **GitHub Actions**. Gli unit test vengono eseguiti ad ogni push.  
Invece, quando viene pushato un **tag** (utilizzando il **semantic versioning**), viene prodotto un **fat jar eseguibile** e salvato su GitHub come **artifact**.  
Infine, per misurare la **coverage**, è stato adottato il plugin `scoverage` per `SBT`, con l’obiettivo di raggiungere almeno il 60-70% di test coverage.

| [Previous Chapter](../1-introduction/index.md) | [Index](../index.md) | [Next Chapter](../3-requirements/index.md) |
