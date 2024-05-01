<?php
// Database connection
$conn = mysqli_connect("localhost", "username", "password", "database");

if (!$conn) {
    die("Connection failed: " . mysqli_connect_error());
}

// Handle POST request to book mentor
if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $user_id = $_POST["user_id"];
    $mentor_id = $_POST["mentor_id"];
    $booking_date = $_POST["booking_date"];
    // Add additional fields as needed
    
    // Insert data into bookings table
    $sql = "INSERT INTO bookings (user_id, mentor_id, booking_date) VALUES ('$user_id', '$mentor_id', '$booking_date')";
    
    if (mysqli_query($conn, $sql)) {
        echo "Booking successful";
    } else {
        echo "Error: " . $sql . "<br>" . mysqli_error($conn);
    }
}

// Close connection
mysqli_close($conn);
?>
