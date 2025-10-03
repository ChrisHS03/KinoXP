async function postObjectAsJson(url, object) {
    const objectAsJsonString = JSON.stringify(object)
    console.log(objectAsJsonString)
    const fetchOptions = {
        method: "POST",
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
        headers: {
            "Content-Type": "application/json",
        },
        body: ""
    }
    const response = await fetch(url, fetchOptions)
    return response
}


function fetchAnyUrl(url) {
    return fetch(url).then(response => response.json().catch(error => console.error("Handled error xx", error)))
}

export {postObjectAsJson, fetchAnyUrl, updateObjectAsJson, deleteObjectAsJson}