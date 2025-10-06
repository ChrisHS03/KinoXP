import {fetchAnyUrl} from "./modulejson.js";


let urlGetMovieID = getMovieIdFromUrl()
const urlGetMovie = "http://localhost:8080/movie/"

console.log("vi er inde i en film")

function getMovieIdFromUrl() {
    const params = new URLSearchParams(window.location.search);
    return params.get("id");
}

function displayMovie(movie) {
    console.log("vi henter values fra en film")
    document.getElementById("movieTitle").textContent = `Title: ${movie.movie_title}`
    document.getElementById("movieDescription").textContent = `Description: ${movie.movie_description}`;
    document.getElementById("movieDuration").textContent = `Duration: ${movie.movie_duration}`;
    document.getElementById("movieActors").textContent = `Actors: ${movie.movie_actors}`;
    document.getElementById("movieAgeRequirement").textContent = `Age requirement: ${movie.movie_age_req}`;
    document.getElementById("movieStart").textContent = `Movie period start: ${movie.movie_period_start}`;
    document.getElementById("movieEnd").textContent = `Movie period end: ${movie.movie_period_end}`;
    document.getElementById("movieGenre").textContent = `Genre: ${movie.movie_genre}`;
    const photo = document.getElementById("moviePhoto");
    photo.src = movie.movie_photo_href;
    photo.alt = movie.movie_title;
    photo.style.maxWidth = "200px";}

async function fetchMovie() {
    let finalUrl = urlGetMovie + urlGetMovieID
    console.log(finalUrl)
    let movie = await fetchAnyUrl(finalUrl);
    console.log(movie)
    if (movie) {
        displayMovie(movie)
    } else {
        alert("Fejl ved kald til backend url=" + finalUrl + " vil du vide mere så kig i Console")
    }
}

function actionShowMovie(){
    fetchMovie()
}

actionShowMovie()

//-----------------------------------------

// Select the table and its parent container
const tblShows = document.getElementById("tblShows");
const div1 = document.querySelector(".div1");

// Create navigation buttons
const navContainer = document.createElement("div");
navContainer.style.display = "flex";
navContainer.style.justifyContent = "center";
navContainer.style.alignItems = "center";
navContainer.style.margin = "10px 0";
navContainer.style.gap = "10px";

// Create arrow buttons and week label
const prevBtn = document.createElement("button");
prevBtn.textContent = "⬅️";
const nextBtn = document.createElement("button");
nextBtn.textContent = "➡️";
const weekLabel = document.createElement("span");
weekLabel.style.fontWeight = "bold";
weekLabel.style.fontSize = "1.2em";

navContainer.appendChild(prevBtn);
navContainer.appendChild(weekLabel);
navContainer.appendChild(nextBtn);
div1.insertBefore(navContainer, tblShows);

// Keep track of the current week offset (0 = this week)
let currentWeekOffset = 0;

// Function to get Monday of a given week offset
function getMonday(offset = 0) {
    const today = new Date();
    const day = today.getDay(); // 0 (Sun) → 6 (Sat)
    const diff = today.getDate() - day + (day === 0 ? -6 : 1); // Adjust to Monday
    const monday = new Date(today.setDate(diff));
    monday.setDate(monday.getDate() + offset * 7);
    return monday;
}

// Function to generate the week’s dates
function getWeekDates(offset = 0) {
    const monday = getMonday(offset);
    const week = [];
    for (let i = 0; i < 7; i++) {
        const date = new Date(monday);
        date.setDate(monday.getDate() + i);
        week.push(date);
    }
    return week;
}

// Function to render the table header with real calendar dates
function renderWeek(offset = 0) {
    const days = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"];
    const weekDates = getWeekDates(offset);

    // Clear existing rows (keep header row)
    tblShows.innerHTML = `
        <tr>
            ${days.map(day => `<th>${day}</th>`).join('')}
        </tr>
    `;

    // Add a new row for the dates
    const dateRow = document.createElement("tr");
    weekDates.forEach(date => {
        const td = document.createElement("td");
        td.textContent = date.toLocaleDateString('en-GB', { day: '2-digit', month: 'short' }); // e.g., 06 Oct
        dateRow.appendChild(td);
    });
    tblShows.appendChild(dateRow);

    // Update week label
    const start = weekDates[0].toLocaleDateString('en-GB', { day: '2-digit', month: 'short' });
    const end = weekDates[6].toLocaleDateString('en-GB', { day: '2-digit', month: 'short' });
    weekLabel.textContent = `${start} – ${end}`;
}

// Event listeners for navigation
prevBtn.addEventListener("click", () => {
    currentWeekOffset--;
    renderWeek(currentWeekOffset);
});
nextBtn.addEventListener("click", () => {
    currentWeekOffset++;
    renderWeek(currentWeekOffset);
});

// Initial render
renderWeek();

