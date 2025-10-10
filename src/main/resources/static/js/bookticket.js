import { fetchAnyUrl, postObjectAsJson } from './modulejson.js';

// Hent showId fra URL
const urlParams = new URLSearchParams(window.location.search);
const showId = urlParams.get('id');

let show = null;
let theater = null;
let bookedSeats = [];
let selectedSeats = [];

// Hent show info
async function loadShowInfo() {
    try {
        show = await fetchAnyUrl(`https://kinoxpback-h7arcge9c2ahdxfu.uksouth-01.azurewebsites.net/api/shows/${showId}`);

        if (!show) {
            alert('Show not found');
            return;
        }

        theater = show.theater;

        // Vis show info
        document.getElementById('movieTitle').textContent = show.movie.movie_title;
        document.getElementById('showDetails').innerHTML = `
            <strong>Theater:</strong> ${theater.name}<br>
            <strong>Time:</strong> ${new Date(show.showTime).toLocaleString('da-DK')}<br>
            <strong>Price:</strong> ${show.price} DKK/Ticket
        `;

        // Hent bookede sæder
        await loadBookedSeats();

        // Generer sædekort
        generateSeatGrid();

    } catch (error) {
        console.error('Error loading. ', error);
        alert('Error loading show information');
    }
}

// Hent bookede sæder
async function loadBookedSeats() {
    try {
        bookedSeats = await fetchAnyUrl(`https://kinoxpback-h7arcge9c2ahdxfu.uksouth-01.azurewebsites.net/api/shows/${showId}/booked-seats`);
        console.log('Booked seats: ', bookedSeats);
    } catch (error) {
        console.error('Error loading booked seats. ', error);
        bookedSeats = [];
    }
}

// Generer sædekort
function generateSeatGrid() {
    const grid = document.getElementById('seatGrid');
    grid.innerHTML = '';

    const rows = theater.seatRows;
    const seatsPerRow = theater.seatsPerRow;

    for (let row = 1; row <= rows; row++) {
        const rowDiv = document.createElement('div');
        rowDiv.className = 'row';

        // Række label
        const label = document.createElement('div');
        label.className = 'row-label';
        label.textContent = `Row ${row}:`;
        rowDiv.appendChild(label);

        // Sæder i rækken
        for (let seat = 1; seat <= seatsPerRow; seat++) {
            const seatButton = document.createElement('button');
            seatButton.className = 'seat';
            seatButton.textContent = seat;
            seatButton.dataset.row = row;
            seatButton.dataset.seat = seat;

            // Tjek om sædet er optaget
            const isOccupied = bookedSeats.some(
                s => s.seatRows === row && s.seatNumber === seat
            );

            if (isOccupied) {
                seatButton.classList.add('occupied');
                seatButton.disabled = true;
            } else {
                seatButton.addEventListener('click', () => toggleSeat(row, seat, seatButton));
            }

            rowDiv.appendChild(seatButton);
        }

        grid.appendChild(rowDiv);
    }
}

// Toggle sæde valg
function toggleSeat(row, seat, button) {
    const seatIndex = selectedSeats.findIndex(
        s => s.seatRow === row && s.seatNumber === seat
    );

    if (seatIndex > -1) {
        // Fjern fra valgte
        selectedSeats.splice(seatIndex, 1);
        button.classList.remove('selected');
    } else {
        // Tilføj til valgte
        selectedSeats.push({ seatRow: row, seatNumber: seat });
        button.classList.add('selected');
    }

    updateSelectedInfo();
}

// Opdater info om valgte sæder
function updateSelectedInfo() {
    const count = selectedSeats.length;
    const total = count * show.price;

    document.getElementById('seatCount').textContent = count;
    document.getElementById('totalPrice').textContent = total;

    if (count === 0) {
        document.getElementById('seatList').textContent = 'No seats chosen';
        document.getElementById('bookButton').disabled = true;
    } else {
        const seatText = selectedSeats
            .map(s => `Row ${s.seatRow}, Seat ${s.seatNumber}`)
            .join(' | ');
        document.getElementById('seatList').textContent = seatText;
        document.getElementById('bookButton').disabled = false;
    }
}

// Book sæder
document.getElementById('bookButton').addEventListener('click', async () => {
    if (selectedSeats.length === 0) {
        return;
    }

    const bookButton = document.getElementById('bookButton');
    bookButton.disabled = true;
    bookButton.textContent = 'Booking...';

    try {
        const response = await postObjectAsJson('https://kinoxpback-h7arcge9c2ahdxfu.uksouth-01.azurewebsites.net/api/bookings', {
            showId: parseInt(showId),
            seats: selectedSeats
        });

        const messageDiv = document.getElementById('message');

        if (response.ok) {
            const data = await response.json();
            messageDiv.className = 'message success';
            messageDiv.textContent = ` ${data.message}! Booking ID: ${data.bookingId}`;

            // Disable alle valgte sæder
            selectedSeats.forEach(s => {
                const btn = document.querySelector(`button[data-row="${s.seatRow}"][data-seat="${s.seatNumber}"]`);
                if (btn) {
                    btn.classList.remove('selected');
                    btn.classList.add('occupied');
                    btn.disabled = true;
                }
            });

            selectedSeats = [];
            updateSelectedInfo();

            setTimeout(() => {
                window.location.href = 'index.html';
            }, 3000);

        } else {
            const errorText = await response.text();
            messageDiv.className = 'message error';
            messageDiv.textContent = ` ${errorText}`;
            bookButton.disabled = false;
            bookButton.textContent = 'Book tickets';
        }

    } catch (error) {
        console.error('Booking error. ', error);
        const messageDiv = document.getElementById('message');
        messageDiv.className = 'message error';
        messageDiv.textContent = ' Booking error. Try again.';
        bookButton.disabled = false;
        bookButton.textContent = 'Book tickets';
    }
});

// Start applikationen
loadShowInfo();