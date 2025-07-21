# View
La view inizialmente era completatmente contenuta all'interno di un file, tuttavia con l'aggiunta di nuove funzionalità è stato necessario riorganizzarla ed adattare una struttura più modulare. Il file `View` ora contiene la definizione della griglia di base, usata per visualizzare lo scenario, e il layout dei pannelli definiti all'interno di `ViewPanels`. Quest'ultimo contiene:
-  il pannello di contollo `ControlPanel`, che espone tutti i controlli della simulazione come: start, stop, step, reset e velocità dell'animazione
-  il pannello `ScenarioSettingsPanel`, che permette all'utente di configurare i parametri della simulazione come: dimensione dello scenario, tipologia di scenario, algoritmo di generazion dello scenario
-  il pannello `DirectionGrid`, che permette all'utente di poter scegliere in quale direzione può muoversi l'agente
Questa separazione è stata fatta per separare nettamente tutto ciò che riguarda la configurazione della simulazione dal controllo di questa, rendendo tutto più modulare e l'aggiunta di ulteriori funzionalità più semplice.
Il modulo `ViewUtilities` invece contiene tutte le classi generiche che possono essere utili alla creazioni dei pannelli. Al suo internno infatti troviamo classi personalizzate di bottoni, dropbox (per aggiungere un placeholder) e così via. L'aspetto cruciale è che questo modulo contiene una serie di elementi che possono venire riutilizzati più volte all'iterno del progetto. Funziona quasi come una libreria per la GUI separata.

Infine, come ultimo aspettto, ma non per importanza, la view estende una interfaccia nominata `ConrollableView`, la quale espone una serie di metodi che permettono al controller di interfacciarsi con la view e di intervenire su alcune funzionalità di questa. Ad esempio sulla attivazione / disattivazione di uno o più pulsanti. Questa scelta è stata fatta per separare ancora di più la logica dell'applicazione dalla visualizzazione del suo stato, tutto in linea con i principii del pattern MVC.
Di seguito un uml completo dei componenti appena descritti.
<img width="2084" height="908" alt="diagram-15675314231273744946" src="https://github.com/user-attachments/assets/2c0c51b1-2185-4a55-aa08-0f7bfd30c7eb" />
Invece il seguente digramma casi d'uso, mostra l'interazione dell'utente con la view:
<img width="554" height="551" alt="UseCase-ScalaPath drawio" src="https://github.com/user-attachments/assets/e1186c2a-881e-4c3b-af6c-ec59f85cdc4a" />
