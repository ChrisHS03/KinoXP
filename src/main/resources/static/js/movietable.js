import {fetchAnyUrl, deleteObjectAsJson, fetchSession} from "./modulejson.js";

console.log("vi er i movietable")


const urlGetMovies = "https://kinoxpback-h7arcge9c2ahdxfu.uksouth-01.azurewebsites.net/movies"
const urlDeleteMovie = "https://kinoxpback-h7arcge9c2ahdxfu.uksouth-01.azurewebsites.net/deletemovie/"
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

    // Skjul Create Movie btn hvis ikke EMPLOYEE
    const btnCreateMovie = document.getElementById("btnCreateMovie");
    if (!currentUser || currentUser.role !== "EMPLOYEE") {
        btnCreateMovie.style.display = "none";
    }


    await fetchMovies();
}

init(); // start alt
function createTable(movie) {
    const card = document.createElement("div");
    card.className = "movie-card";

    // Buttons for EMPLOYEE
    if (currentUser && currentUser.role === "EMPLOYEE") {
        const buttonContainer = document.createElement("div");
        buttonContainer.className = "button-container";

        const pbUpdate = document.createElement("input");
        pbUpdate.type = "button";
        pbUpdate.className = "btn1";
        pbUpdate.value = "Update Movie";
        pbUpdate.onclick = (event) => {
            event.stopPropagation();
            window.location.href = `movieform.html?id=${movie.movie_id}`;
        }

        const pbDelete = document.createElement("input");
        pbDelete.type = "button";
        pbDelete.className = "btn1";
        pbDelete.value = "Delete Movie";
        pbDelete.onclick = async (event) => {
            event.stopPropagation();
            card.remove();
            await deleteObjectAsJson(urlDeleteMovie + movie.movie_id);
        }

        buttonContainer.appendChild(pbUpdate);
        buttonContainer.appendChild(pbDelete);
        card.appendChild(buttonContainer);
    }

    // Movie poster
    const img = document.createElement("img");
    img.src = movie.movie_photo_href;
    img.alt = movie.movie_title;
    img.style.cursor = "pointer";
    img.onclick = () => {
        window.location.href = `showmovie.html?id=${movie.movie_id}`;
    }
    card.appendChild(img);

    // Movie title
    const title = document.createElement("h2");
    title.textContent = movie.movie_title;
    card.appendChild(title);

    tblMovies.appendChild(card);
}


