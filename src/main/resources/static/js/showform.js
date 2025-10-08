import {postObjectAsJson, fetchSession, fetchAnyUrl} from "./modulejson.js";

console.log("Jeg er i showform")

let urlGetMovieID = getMovieIdFromUrl()
const urlGetMovie = "http://localhost:8080/movie/"
const urlGetTheater = "http://localhost:8080/theater/"
const showCreateUrl = "http://localhost:8080/createshow"



function getMovieIdFromUrl() {
    const params = new URLSearchParams(window.location.search);
    return params.get("id");
}

async function fetchMovie() {
    let finalUrl = urlGetMovie + urlGetMovieID
    console.log(finalUrl)
    let movie = await fetchAnyUrl(finalUrl);
    console.log(movie)
    if (movie) {
       return movie
    } else {
        alert("Fejl ved kald til backend url=" + finalUrl + " vil du vide mere så kig i Console")
    }
}
async function fetchTheater(id) {
    let finalUrl = urlGetTheater + id
    console.log(finalUrl)
    let theater = await fetchAnyUrl(finalUrl);
    console.log(theater)
    if (theater) {
       return theater
    } else {
        alert("Fejl ved kald til backend url=" + finalUrl + " vil du vide mere så kig i Console")
    }
}

async function fetchMovieTitle() {
    let finalUrl = urlGetMovie + urlGetMovieID
    console.log(finalUrl)
    let movie = await fetchAnyUrl(finalUrl);
    console.log(movie)
    if (movie) {
        fillForm(movie)
    } else {
        alert("Fejl ved kald til backend url=" + finalUrl + " vil du vide mere så kig i Console")
    }
}




function fillForm(show) {
    document.getElementById("inpTitle").value = show.movie_title || "";

}

function checkForId() {
    if (urlGetMovieID) {
        fetchMovieTitle()
    }
}

checkForId()

document.addEventListener('DOMContentLoaded', createFormEventListener)

let showForm;

function createFormEventListener() {
    showForm = document.getElementById("formShow");
    showForm.addEventListener("submit", handleFormSubmit);
}
async function handleFormSubmit(event) {
    event.preventDefault();

    try {
        console.log("Theater:", showForm.inpTheater.value);
        console.log("Date:", showForm.inpDate.value);
        console.log("Time:", showForm.inpTime.value);
        console.log("Price:", showForm.inpPrice.value);

        // 🔹 Hent hele objekter fra backend først
        const movie = await fetchMovie();  // hele Movie-objektet fra /movie/{id}
        const theater = await fetchTheater(showForm.inpTheater.value); // hele Theater-objektet fra /theater/{id}

        // Tjek at vi faktisk har fået noget
        console.log("Fetched movie:", movie);
        console.log("Fetched theater:", theater);

        if (!movie || !theater) {
            alert("Kunne ikke hente movie eller theater-objekt!");
            return;
        }

        // 🔹 Byg nyt show-objekt som backend forventer
        const showData = {
            movie: movie, // hele movie-objektet
            theater: theater, // hele theater-objektet
            showTime: `${showForm.inpDate.value}T${showForm.inpTime.value}:00`,
            price: Number(showForm.inpPrice.value)
        };

        console.log("Sender showData til backend:", showData);

        // 🔹 Send objektet til backend
        const response = await postObjectAsJson(showCreateUrl, showData);

        if (response.ok) {
            alert("Show saved successfully");
            window.location.href = `showmovie.html?id=${movie.movie_id}`;
        } else if (response.status === 403) {
            alert("Access denied: You do not have permission to create a show.");
        } else {
            const errorText = await response.text();
            alert("Fejl ved oprettelse af show. Status: " + response.status + "\n" + errorText);
        }

    } catch (error) {
        console.error("Fejl i handleFormSubmit:", error);
        alert("Der opstod en fejl: " + error.message);
    }
}
