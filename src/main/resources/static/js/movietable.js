import {fetchAnyUrl,deleteObjectAsJson} from "./modulejson.js";
console.log("vi er i movietable")

const urlGetMovies = "http://localhost:8080/movies"
const urlDeleteMovie = "http://localhost:8080/deletemovie/"

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

    row.style.cursor = "pointer";
    row.addEventListener("click", () => {
        window.location.href = `showmovie.html?id=${movie.movie_id}`;
    });

    console.log(movie)

    let cell = row.insertCell(cellCount++)
    cell.innerHTML = movie.movie_title

    cell = row.insertCell(cellCount++)
    cell.innerHTML = movie.movie_description

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

    cell = row.insertCell(cellCount++);
    cell.innerHTML = `<img src="${movie.movie_photo_href}" alt="Movie Photo" style="width:100px; height:auto;">`;

    const pbUpdate = document.createElement("input");
    pbUpdate.type = "button";
    pbUpdate.className = "btn1"
    pbUpdate.setAttribute("value", "Update Movie");
    cell.appendChild(pbUpdate);

    pbUpdate.onclick = async function (event) {
        event.stopPropagation()
        window.location.href = `movieform.html?id=${movie.movie_id}`;
    }

    const pbDelete = document.createElement("input");
    pbDelete.type = "button";
    pbDelete.className = "btn1"
    pbDelete.setAttribute("value", "Delete Movie");
    cell.appendChild(pbDelete);

    row.id = movie.movie_title
    pbDelete.onclick = async function (event) {
        event.stopPropagation()
        document.getElementById(movie.movie_title).remove()
        await deleteObjectAsJson(urlDeleteMovie+movie.movie_id)
    }
}

actionGetMovies()