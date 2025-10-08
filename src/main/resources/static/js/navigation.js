

async function checkSession() {
    try {
        const response = await fetch('http://localhost:8080/api/auth/session', {
            credentials: 'include'
        });

        const navLinks = document.getElementById('navLinks');

        if (response.ok) {
            const data = await response.json();
            navLinks.innerHTML = `
                <span>Welcome ${data.username}</span>
                <button class="btn1" onclick="logout()">Logout</button>
            `;
        } else {
            navLinks.innerHTML = `
                <a class="btn1" href="login.html">Login</a>
                <a class="btn1" href="register.html">Register</a>
            `;
        }
    } catch (error) {
        document.getElementById('navLinks').innerHTML = `
            <a class="btn1" href="login.html">Login</a>
            <a class="btn1" href="register.html">Register</a>
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