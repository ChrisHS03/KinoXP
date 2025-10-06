import {fetchAnyUrl, deleteObjectAsJson, fetchSession} from "./modulejson.js";

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


let currentUser = null;

async function init() {
    currentUser = await fetchSession(); // vent på session-data
    console.log(currentUser?.role);

    // Skjul Create Movie-knap hvis ikke EMPLOYEE
    const btnCreateMovie = document.getElementById("btnCreateMovie");
    if (!currentUser || currentUser.role !== "EMPLOYEE") {
        btnCreateMovie.style.display = "none";
    }

    // Hent film
    await fetchMovies();
}

init(); // start alt



function createTable(movie) {
    let cellCount = 0
    let rowCount = tblMovies.rows.length
    let row = tblMovies.insertRow(rowCount)

    row.style.cursor = "pointer";
    row.addEventListener("click", () => {
        window.location.href = `showmovie.html?id=${movie.movie_id}`;
    });

    console.log(movie)
    let cell = row.insertCell(cellCount++);
    cell.innerHTML = `<img src="${movie.movie_photo_href}" alt="Movie Photo" style="width:100px; height:auto;">`;

    cell = row.insertCell(cellCount++)
    cell.innerHTML = movie.movie_title

    cell = row.insertCell(cellCount++)
    const pbBook = document.createElement("input");
    pbBook.type = "button";
    pbBook.className = "btn1"
    pbBook.setAttribute("value", "Book tickets");
    cell.appendChild(pbBook);

    pbBook.onclick = async function (event) {
        event.stopPropagation()
        window.location.href = `bookticket.html?id=${movie.movie_id}`;
    }

    if (currentUser && currentUser.role === "EMPLOYEE") {

        cell = row.insertCell(cellCount++)
        const pbUpdate = document.createElement("input");
        pbUpdate.type = "button";
        pbUpdate.className = "btn1"
        pbUpdate.setAttribute("value", "Update Movie");

        pbUpdate.onclick = async function (event) {
            event.stopPropagation()
            window.location.href = `movieform.html?id=${movie.movie_id}`;
        }

        cell.appendChild(pbUpdate);

        cell = row.insertCell(cellCount++)
        const pbDelete = document.createElement("input");
        pbDelete.type = "button";
        pbDelete.className = "btn1"
        pbDelete.setAttribute("value", "Delete Movie");
        cell.appendChild(pbDelete);

    row.id = movie.movie_title
    pbDelete.onclick = async function (event) {
        event.stopPropagation()
        document.getElementById(movie.movie_title).remove()
        await deleteObjectAsJson(urlDeleteMovie + movie.movie_id)
    }
}  }


