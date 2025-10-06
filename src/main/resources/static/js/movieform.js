import {postObjectAsJson, fetchAnyUrl, updateObjectAsJson} from "./modulejson.js";
console.log("Jeg er i movieForm")

let urlGetMovieID = getMovieIdFromUrl()
const urlGetMovie = "http://localhost:8080/movie/"

function getMovieIdFromUrl() {
    const params = new URLSearchParams(window.location.search);
    return params.get("id");
}

function fillForm(movie){
    document.getElementById("inpTitle").value = movie.movie_title || "";
    document.getElementById("inpDescription").value = movie.movie_description || "";
    document.getElementById("inpDuration").value = movie.movie_duration || "";
    document.getElementById("inpActors").value = movie.movie_actors || "";
    document.getElementById("inpAgeReq").value = movie.movie_age_req || "";
    document.getElementById("inpMovieStart").value = movie.movie_period_start || "";
    document.getElementById("inpMovieEnd").value = movie.movie_period_end || "";
    document.getElementById("inpGenre").value = movie.movie_genre || "";
    document.getElementById("inpPhotoHref").value = movie.movie_photo_href || "";

}

async function fetchMovie() {
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

function checkForId(){
    if(urlGetMovieID){
        fetchMovie()
    }
}

checkForId()

document.addEventListener('DOMContentLoaded',createFormEventListener)

let movieForm;
const movieCreateUrl = "http://localhost:8080/createmovie"
const movieUpdateUrl = "http://localhost:8080/updatemovie/"



function createFormEventListener() {
    movieForm = document.getElementById("formMovie");
    movieForm.addEventListener("submit", handleFormSubmit);
}


async function handleFormSubmit(event) {
    //Vi handler submitten her i stedet for default html behaviour
    event.preventDefault();
    try {
        console.log(movieForm)
        const formData = new FormData(movieForm);
        const plainFormData = Object.fromEntries(formData.entries());
        console.log(formData);

        let responseData;

        if (urlGetMovieID) {
            // UPDATE
            console.log("Opdaterer film med id:", urlGetMovieID);
            responseData = await updateObjectAsJson(movieUpdateUrl + urlGetMovieID, plainFormData);
        } else {
            // CREATE
            console.log("Opretter ny film");
            responseData = await postObjectAsJson(movieCreateUrl, plainFormData);
        }

        console.log(responseData);

        if (responseData.ok) {
            alert("Movie saved successfully!")
            window.location.href = `index.html`;
        } else if (responseData.status === 403) {
            // Kun EMPLOYEE har adgang
            alert("Access denied: You do not have permission to create a movie.");
        } else {
            alert("Input type wrong. " + "Failed to save movie. Status: " + responseData.status);
        }
    } catch (error) {
        alert(error.message);
        console.error(error);
    }
    console.log(event)

}

