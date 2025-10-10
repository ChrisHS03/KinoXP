import {deleteObjectAsJson, fetchAnyUrl, fetchSession} from "./modulejson.js";


let urlGetMovieID = getMovieIdFromUrl()
const urlGetMovie = "https://kinoxpback-h7arcge9c2ahdxfu.uksouth-01.azurewebsites.net/movie/"
const urlGetShows = "https://kinoxpback-h7arcge9c2ahdxfu.uksouth-01.azurewebsites.net/shows/"
const urlDeleteShow = "https://kinoxpback-h7arcge9c2ahdxfu.uksouth-01.azurewebsites.net/deleteshow/"

console.log("vi er inde i en film")

function getMovieIdFromUrl() {
    const params = new URLSearchParams(window.location.search);
    return params.get("id");
}

function displayMovie(movie) {
    console.log("vi henter values fra en film")
    document.getElementById("movieTitle").textContent = `${movie.movie_title}`
    document.getElementById("movieDescription").textContent = `${movie.movie_description}`;
    document.getElementById("movieDuration").textContent = ` ${movie.movie_duration}`;
    document.getElementById("movieActors").textContent = ` ${movie.movie_actors}`;
    document.getElementById("movieAgeRequirement").textContent = `${movie.movie_age_req}`;
    document.getElementById("movieStart").textContent = `${movie.movie_period_start}`;
    document.getElementById("movieEnd").textContent = ` ${movie.movie_period_end}`;
    document.getElementById("movieGenre").textContent = `${movie.movie_genre}`;
    const photo = document.getElementById("moviePhoto");
    photo.src = movie.movie_photo_href;
    photo.alt = movie.movie_title;
    photo.style.maxWidth = "200px";
}

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

function actionShowMovie() {
    fetchMovie()
}

actionShowMovie()

// -------- Dropdown Shows -----

const ddShows = document.getElementById("ddShows")
let shows = []

async function fetchShows() {
    console.log("er i fetch shows")
    shows = await fetchAnyUrl(urlGetShows + urlGetMovieID);
    if (shows) {
        shows.sort((a, b) => new Date(a.showTime) - new Date(b.showTime));
        shows.forEach(fillDropDown)
    } else {
        alert("Fejl ved kald til backend url=" + urlGetShows + " vil du vide mere så kig i Console")
    }
}

function fillDropDown(show) {
    const el = document.createElement("option")
    el.textContent = show.showTime
    el.value = show.showId
    ddShows.appendChild(el)

}

fetchShows()

// ---- Book tickets button

const pbBook = document.getElementById("pbBookTickets")

pbBook.onclick = async function (event) {
    window.location.href = `bookticket.html?id=${ddShows.value}`;
}

// ------ Create show  + delete btn


let currentUser = null;
const pbCreateShow = document.getElementById("pbCreateShow");
const pbDelete = document.getElementById("pbDeleteShow");

async function init() {
    currentUser = await fetchSession(); // vent på session-data
    console.log(currentUser?.role);

    // Skjul Create Show-knap hvis ikke EMPLOYEE
    if (!currentUser || currentUser.role !== "EMPLOYEE") {
        pbCreateShow.style.display = "none";
        pbDelete.style.display = "none";
    }


}
pbDelete.onclick = async function (event) {
    const selectedShowId = ddShows.value;
    const selectedOption = ddShows.querySelector(`option[value="${selectedShowId}"]`);
    ddShows.removeChild(selectedOption);

    await deleteObjectAsJson(urlDeleteShow + selectedShowId)
    console.log("Show fjernet fra dropdown");
}

pbCreateShow.onclick = async function (event) {
    window.location.href = `showform.html?id=${urlGetMovieID}`;
}

init()










