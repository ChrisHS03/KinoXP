import {postObjectAsJson} from "./modulejson.js";

console.log("Jeg er i movieForm")


document.addEventListener('DOMContentLoaded',createFormEventListener)
let movieForm;
const movieCreateUrl = "http://localhost:8080/createmovie"


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
        const responseData = await postObjectAsJson(movieCreateUrl, plainFormData);
        console.log(responseData)

        if (responseData.ok) {
            alert("Movie saved successfully!")
            window.location.href = `index.html`;
        } else {
            alert("Input type wrong. " + "Failed to save movie. Status: " + responseData.status);
        }
    } catch (error) {
        alert(error.message);
        console.error(error);
    }
    console.log(event)

}

