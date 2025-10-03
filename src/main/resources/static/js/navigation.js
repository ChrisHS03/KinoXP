

async function checkSession() {
    try {
        const response = await fetch('http://localhost:8080/api/auth/session', {
            credentials: 'include'
        });

        const navLinks = document.getElementById('navLinks');

        if (response.ok) {
            const data = await response.json();
            navLinks.innerHTML = `
                <span>Velkommen, ${data.username}</span>
                <button onclick="logout()">Log ud</button>
            `;
        } else {
            navLinks.innerHTML = `
                <a href="login.html">Log ind</a>
                <a href="register.html">Opret konto</a>
            `;
        }
    } catch (error) {
        document.getElementById('navLinks').innerHTML = `
            <a href="login.html">Log ind</a>
            <a href="register.html">Opret konto</a>
        `;
    }
}

async function logout() {
    try {
        await fetch('http://localhost:8080/api/auth/logout', {
            method: 'POST',
            credentials: 'include'
        });
        window.location.reload();
    } catch (error) {
        console.error('Logout failed:', error);
    }
}

checkSession();