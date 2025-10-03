

import { postObjectAsJson } from './modulejson.js';

document.getElementById('registerForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    const registerResponse = await postObjectAsJson(
        'http://localhost:8080/api/auth/register-customer',
        { username, password }
    );

    if (registerResponse.ok) {
        document.getElementById('message').textContent = 'Konto oprettet! Logger ind...';


        const loginResponse = await postObjectAsJson(
            'http://localhost:8080/api/auth/login',
            { username, password }
        );

        if (loginResponse.ok) {
            setTimeout(() => {
                window.location.href = 'index.html';
            }, 1000);
        } else {
            document.getElementById('message').textContent = 'Konto oprettet, men login fejlede. Gå til login siden.';
            setTimeout(() => {
                window.location.href = 'login.html';
            }, 2000);
        }
    } else {
        const errorText = await registerResponse.text();
        document.getElementById('message').textContent = 'Fejl: ' + errorText;
    }
});