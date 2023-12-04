# Anleitung für das Ausführen der Simulationsprogramme VacuumCleaner und TestSeries



Im Anhang finden sich zwei executable jar files. Diese heißen VacuumCleaner und TestSeries.

1) VacuumCleaner.jar lässt sich per Doppelklick auf das Icon starten woraufhin sich eine graphische Benutzeroberfläche öffnet. Dieses Programm ist dazu gedacht mit den verschiedenen Umgebungen zu experimentieren und diese zu testen. Unter „Select Map“ kann eine von 7 vordefinierten Umgebungen für den Testlauf eingestellt werden. Unter „Select Algorithm“ kann zwischen „Random Walk“, „ZigZag“ und „Spiral“ ein Algorithmus ausgewählt werden, welcher von dem Roboter auf der Testumgebung ausgeführt werden soll. Beim Klick auf den Button „Random Map“ wird eine Umgebung mit zufälligen Hindernissen generiert. Die Anzahl  sowie die Größe der Hindernisse variiert, wobei die Anzahl von 0 bis 20 beschränkt ist. Nachdem alle Einstellungen vorgenommen wurden, kann mit einem Klick auf „Start“ der Algorithmus ausgeführt werden.

2) TestSeries.jar ist ein automatisiertes Testprogramm, welches eine Reihe an aufeinanderfolgenden Tests anstößt. Dieses Programm kann nur über das Windows Terminal gestartet werden. Hierfür wird das Terminal geöffnet und zum Ordner VacuumCleaner navigiert. Von hier aus kann das Programm über den Befehl:

java -jar TestSeries.jar $iterations $obstacles

gestartet werden, wobei $iterations und $obstacles Platzhalter für die Anzahl an Umgebungen, die getestet werden sollen und die Anzahl der Hindernisse, die die Umgebungen haben sollen, sind (z. B. java -jar TestSeries.jar 10 10 für 10 Durchläufe aller Algorithmen in Umgebungen mit jeweils 10 Hindernissen).

Die Ergebnisse werden hierbei im Ordner results als Textdateien gespeichert, wobei jeder Durchlauf für jeden Algorithmus separat gespeichert wird. Die Benamungen der Textdateien setzen sich wie folgt zusammen:

$algorithm + _ $obstacles + _ $iteration

Ein Spiral Algorithmus der in einer Umgebung mit 10 Hindernissen getestet wird und sich in der 1. Iteration befindet hieße dementsprechend: Spiral_10_1.txt.
![image](https://github.com/l3risch/VacuumCleaner/assets/40214757/ad5bc523-a0c9-4383-b71c-4905f1564051)
