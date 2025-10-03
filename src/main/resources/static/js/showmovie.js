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
