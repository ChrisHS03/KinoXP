async function postObjectAsJson(url, object) {
    const objectAsJsonString = JSON.stringify(object)
    console.log(objectAsJsonString)
    const fetchOptions = {
        method: "POST",
        credentials: "include",
        headers: {
            "Content-Type": "application/json",
        },
        body: objectAsJsonString
    }
    const response = await fetch(url, fetchOptions)
    return response
}

async function updateObjectAsJson(url, object) {
    const objectAsJsonString = JSON.stringify(object)
    console.log(objectAsJsonString)
    const fetchOptions = {
        method: "PUT",
        credentials: "include",
        headers: {
            "Content-Type": "application/json",
        },
        body: objectAsJsonString
    }
    const response = await fetch(url, fetchOptions)
    return response
}



async function deleteObjectAsJson(url) {
    console.log(url)
    const fetchOptions = {
        method: "DELETE",
        credentials: "include",
        headers: {
            "Content-Type": "application/json",
        },
        body: ""
    }
    const response = await fetch(url, fetchOptions)
    return response
}


function fetchAnyUrl(url) {
    return fetch(url, {credentials: "include"}).then(response => response.json().catch(error => console.error("Handled error xx", error)))
}

/* metoden fetchAnyUrl kan skrives sådan her hvis man vil være konsistent med resten af metoderne

async function fetchAnyUrl(url) {
    const fetchOptions = {
        method: "GET",
        credentials: "include",
        headers: {
            "Content-Type": "application/json",
        }
    }
    const response = await fetch(url, fetchOptions)
    return response.json()
}

 */




async function fetchSession() {
    const urlSession = "https://kinoxpback-h7arcge9c2ahdxfu.uksouth-01.azurewebsites.net/api/auth/session";
    try {
        const response = await fetch(urlSession, {
            method: "GET",
            credentials: "include" // vigtige for cookies/session
        });

        if (!response.ok) {
            console.log("User not signed in");
            return null;
        }

        const user = await response.json();
        console.log("Session user:", user);
        return user;
    } catch (err) {
        console.error("Error fetching session:", err);
        return null;
    }
}


export {postObjectAsJson, fetchAnyUrl, updateObjectAsJson, deleteObjectAsJson, fetchSession}