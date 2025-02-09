document.addEventListener("DOMContentLoaded", function () {
  function loadPlayersTable() {
    fetch("data/players.json")
      .then((response) => response.json())
      .then((data) => {
        document.getElementById("main-content").innerHTML = '<div id="table-container"></div>';
        new Tabulator("#table-container", {
          data: data,
          layout: "fitColumns",
          height: "100%",
          initialSort: [{ column: "points", dir: "desc" }],
          columns: [
            {
              title: "#",
              field: "rowNum",
              width: 40,
              formatter: "rownum",
              headerSort: false,
            },
            {
              title: "Name",
              field: "name",
              sorter: "string",
              widthGrow: 1,
              width: 180,
              formatter: "link", // for clickable names
              formatterParams: {
                labelField: "name",
                url: "#",
                target: "_self",
              },
              cellClick: function (e, cell) {
                const rowData = cell.getRow().getData();
                showPlayerDetails(rowData.name);
              },
            },
            { title: "Total points", field: "points", sorter: "number" },
            { title: "Maximum points", field: "maxPoints", sorter: "number" },
            { title: "Minimum points", field: "minPoints", sorter: "number" },
            {
              title: "Average points",
              field: "averagePoints",
              sorter: "number",
            },
            {
              title: "Standard deviation",
              field: "standardDeviationOfPoints",
              sorter: "number",
            },
            { title: "Wins", field: "wins", sorter: "number" },
            { title: "Draws", field: "draws", sorter: "number" },
            { title: "Losses", field: "losses", sorter: "number" },
            { title: "Fails", field: "fails", sorter: "number" },
            {
              title: "Squeaker wins",
              field: "squeakerWins",
              sorter: "number",
              headerTooltip: "Wins with scores of 201-99 or 202-98",
            },
            {
              title: "Squeaker losses",
              field: "squeakerLosses",
              sorter: "number",
              headerTooltip: "Losses with scores of 99-201 or 98-202",
            },
          ],
        });
      })
      .catch((error) => console.error("Error loading JSON data:", error));
  }

  function getStatusColor(value) {
    if (value === "Weaker") {
      return "hsl(120, 40%, 75%)"; // Soft Green
    } else if (value === "Challenger") {
      return "hsl(60, 40%, 85%)"; // Soft Yellow
    } else if (value === "Stronger") {
      return "hsl(0, 40%, 75%)"; // Soft Red
    }
    return "white";
  }

  function showPlayerDetails(playerName) {
    fetch("data/dissection/" + playerName + ".json")
      .then((response) => response.json())
      .then((data) => {
        document.getElementById("main-content").innerHTML =
          `<button id="backButton" class="btn btn-link">Back</button>` +
          `<h5> All other players versus ${playerName}</h5>` +
          `<div id="table-container"></div>`;
        document
          .getElementById("backButton")
          .addEventListener("click", loadPlayersTable);

        new Tabulator("#table-container", {
          data: data,
          layout: "fitColumns",
          height: "100%",
          initialSort: [{ column: "points", dir: "desc" }],
          columns: [
            {
              title: "#",
              field: "rowNum",
              width: 40,
              formatter: "rownum",
              headerSort: false,
            },
            {
              title: "Name",
              field: "name",
              sorter: "string",
              widthGrow: 1,
              width: 180,
            },
            { title: "Total points", field: "points", sorter: "number" },
            { title: "Maximum points", field: "maxPoints", sorter: "number" },
            { title: "Minimum points", field: "minPoints", sorter: "number" },
            {
              title: "Average points",
              field: "averagePoints",
              sorter: "number",
            },
            { title: "Wins", field: "wins", sorter: "number" },
            { title: "Draws", field: "draws", sorter: "number" },
            { title: "Losses", field: "losses", sorter: "number" },
            { title: "Fails", field: "fails", sorter: "number" },
            {
              title: "Squeaker wins",
              field: "squeakerWins",
              sorter: "number",
              headerTooltip: "Wins with scores of 201-99 or 202-98",
            },
            {
              title: "Squeaker losses",
              field: "squeakerLosses",
              sorter: "number",
              headerTooltip: "Losses with scores of 99-201 or 98-202",
            },
            {
              title: "League",
              field: "subjectivePlayerType",
              headerSort: false,
              headerTooltip: "If flipping the result of any 2 games cannot change who has won more games, then the player with more wins is assumed to be stronger.",
              formatter: function (cell) {
                var value = cell.getValue();
                var bgColor = getStatusColor(value);
                return `<div style="background-color: ${bgColor};">${value}</div>`;
              },
            },
          ],
        });
      })
      .catch((error) => console.error("Error loading JSON data:", error));
  }

  async function updateCharts() {
    const mainContent = document.getElementById("main-content");
    mainContent.innerHTML = "";
    try {
      const response = await fetch("data/competition.json");
      const data = await response.json();

      const chartContainer = document.createElement("div");
      chartContainer.style.display = "flex";
      chartContainer.style.justifyContent = "space-around";
      chartContainer.style.width = "100%";
      chartContainer.style.height = "100%";
      const chartDiv1 = document.createElement("div");
      chartDiv1.style.width = "45%";
      chartDiv1.style.height = "400px";
      const chartDiv2 = document.createElement("div");
      chartDiv2.style.width = "45%";
      chartDiv2.style.height = "400px";
      chartContainer.appendChild(chartDiv1);
      chartContainer.appendChild(chartDiv2);
      mainContent.appendChild(chartContainer);

      const canvas1 = document.createElement("canvas");
      canvas1.id = "pieChart1";
      chartDiv1.appendChild(canvas1);
      const canvas2 = document.createElement("canvas");
      canvas2.id = "pieChart2";
      chartDiv2.appendChild(canvas2);

      const ctx1 = canvas1.getContext("2d");
      const ctx2 = canvas2.getContext("2d");

      const chartConfig = {
        type: "pie",
        data: {
          labels: ["Player 1 win count", "Player 2 win count", "Draw count"],
          datasets: [
            {
              data: [
                data.firstPlayerWinCount,
                data.secondPlayerWinCount,
                data.drawCount,
              ],
              backgroundColor: ["#FF6384", "#36A2EB", "#FFCE56"],
              hoverBackgroundColor: ["#FF6384", "#36A2EB", "#FFCE56"],
            },
          ],
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
        },
      };
      new Chart(ctx1, chartConfig);

      const chartConfig2 = {
        type: "pie",
        data: {
          labels: ["Player 1 total points", "Player 2 total points"],
          datasets: [
            {
              data: [data.firstPlayerPoints, data.secondPlayerPoints],
              backgroundColor: ["cyan", "green"],
              hoverBackgroundColor: ["cyan", "green"],
            },
          ],
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
        },
      };
      new Chart(ctx2, chartConfig2);
    } catch (error) {
      console.error("Error fetching the JSON data:", error);
    }
  }

  document.getElementById("player-statistics").addEventListener("click", loadPlayersTable);
  document.getElementById("competition-statistics").addEventListener("click", updateCharts);
});