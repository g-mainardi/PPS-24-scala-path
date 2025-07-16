# Architectural Design

Il progetto adotta un'architettura MVC: 
1. **Model**: contiene le entità che fanno parte della simulazione, come l'agente, lo scenario, le caselle, etc. 
2. **View**: mostra la simulazione e permette di configurare i parametri di esecuzione.
3. **Controller**: gestisce l'interazione tra il modello e la vista, orchestrando l'esecuzione della simulazione e le varie transizioni di stato.

Altri layer sono stati aggiunti per implementare le logiche di pathfinding, interagire con i planner o per gestire l'interazione con Prolog.
* **Planning**: contiene gli algoritmi di pathfinding e le classi per la configurazione dei planner. 
* **Integratinon**: specifico per l'integrazione con Prolog, contiene le classi per eseguire query e fare il parsing dei risultati. 

![architecture](../resources/architecture.png "Architecture Overview")

| [Previous Chapter](../3-requirements/index.md) | [Index](../index.md) | [Next Chapter](../5-detailed_design/index.md) |