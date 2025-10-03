import {fetchAnyUrl, postObjectAsJson, updateObjectAsJson, deleteObjectAsJson} from "./modulejson.js";


const urlGetMovies = "http://localhost:8080/movies"
const urlCreateMovies = "http://localhost:8080/createmovie"
const urlUpdateMovies = "http://localhost:8080/updatemovie"
const urlDeleteMovies = "http://localhost:8080/deletemovie"


const pbCreateMovieTable = document.getElementById("pbGetMovies")

const tblMovies = document.getElementById("tblMovies")


let movies = []

async function fetchMovies() {
    console.log("er i fetch movies")
    movies = await fetchAnyUrl(urlGetMovies);
    if (movies) {
        movies.forEach(createTable)
    } else {
        alert("Fejl ved kald til backend url=" + urlGetMovies + " vil du vide mere så kig i Console")
    }
}

function actionGetMovies() {
    fetchMovies()
}


function createTable(movie) {
    let cellCount = 0
    let rowCount = tblMovies.rows.length
    let row = tblMovies.insertRow(rowCount)

    console.log(movie)

    let cell = row.insertCell(cellCount++)
    cell.innerHTML = movie.movie_title

    cell = row.insertCell(cellCount++)
    cell.innerHTML = movie.movie_duration

    cell = row.insertCell(cellCount++)
    cell.innerHTML = movie.movie_actors

    cell = row.insertCell(cellCount++)
    cell.innerHTML = movie.movie_age_req

    cell = row.insertCell(cellCount++)
    cell.innerHTML = movie.movie_period_start

    cell = row.insertCell(cellCount++)
    cell.innerHTML = movie.movie_period_end

    cell = row.insertCell(cellCount++)
    cell.innerHTML = movie.movie_genre


}


pbCreateKommuneTable.addEventListener("click", actionGetKommuner)