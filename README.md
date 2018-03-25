# Recherche nach Musik-Events

Phil Taubert  

Projekt im Modul Semantic Web - HTWK Leipzig  
 Prof. Dr. rer. nat. Thomas Riechert

## Fragestellung

Welche Festivals und Konzerte mit Interpreten aus dem Genre Alternative Rock und einer maximalen Anfahrtszeit von zwei Stunden ab Leipzig finden in den Semesterferien nach dem Sommersemester 2017 (30.07.2017 bis 24.09.2017) statt?

## Datenquellen

* Musik-Events (Interpreten, Location)
  * Eventful - api.eventful.com/
* Genres (Tags) der Interpreten
  * LastFM - last.fm/api
* Anfahrtsdauer zur Location
  * Google Directions
  * developers.google.com/maps
  
## Datenquellen

* Abruf aller Events
* Extraktion der Interpreten
* Abruf der Tags zu Interpreten
* Extraktion der Locations
* Abruf der Anfahrtsdauer zu Locations
- Abruf im XML-Format mit Batch/Java
- erste Aufbereitung mit Java

## RDF

* Erstellung eines Vokabulars mit Protegé
* Aufbereitung und Umwandlung der XML-Daten mit Google Refine
* Export als RDF-Turtle (.ttl)
* Importieren und Abfrage der Daten mit Protegé oder Stardog

## Abfrage / Erwartungen

* SPARQL Abfrage -> Suche alle Events
  * deren Interpreten einen Tag “Alternative Rock” besitzen
  * deren Location eine Anfahrtszeit von kleiner bzw. gleich zwei Stunden hat
* Anzeige weniger passender Konzerte und Festivals

## Dokumentation

Die gesamte Dokumentation ist im [Projekt](https://github.com/Tapematch/SemanticWeb/blob/master/Dokumentation_ptaubert_68246.pdf) zu finden.