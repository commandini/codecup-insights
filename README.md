## CodeCup Insights

This project can be used for generating and presenting various statistics
about [CodeCup](https://www.codecup.nl/intro.php) competitions.

## How it works

The statistics files have been prepared via Java (```core/Main.java```). It loads the respective CSV file(s) retrieved
from main CodeCup site, creates modeled entities like players and competitions based on the CSV data,
computes a bunch of statistics for those entities and serializes them to files under the __data__ folder. Those files
form the base for various tables and charts of the user interface which is
prepared via simple HTML, CSS and JavaScript.

Frameworks/libraries used:
* [Opencsv](https://opencsv.sourceforge.net)
* [Jackson](https://github.com/FasterXML/jackson)
* [Bootstrap](https://getbootstrap.com)
* [Tabulator](https://tabulator.info)
* [Chart.js](https://www.chartjs.org)

## Running locally

Clone the repository. Java version >= 17 and Gradle version 8.10 is recommended.
The project can be built via:

```shell
gradle clean build
```

Running ```core/Main.java``` will compute the statistics and serialize them.
(Or you can just start with the existing files in the data folder.)
A simple HTTP server can be started via:

```shell
python3 -m http.server
```

Statistics page can be viewed at http://localhost:8000 once the server is launched successfully.

#### Author(s)

* [Fakih Karademir](https://github.com/commandini)