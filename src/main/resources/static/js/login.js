

import { postObjectAsJson } from './modulejson.js';

document.getElementById('loginForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    const response = await postObjectAsJson(
        'https://kinoxpback-h7arcge9c2ahdxfu.uksouth-01.azurewebsites.net/api/auth/login',
        { username, password }
    );

    if (response.ok) {
        document.getElementById('message').textContent = 'Login successful!';
        window.location.href = 'index.html';
    } else {
        document.getElementById('message').textContent = 'Wrong username or password';
    }
});
