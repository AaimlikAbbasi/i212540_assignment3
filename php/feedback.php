<?php
// Database connection
$conn = mysqli_connect("localhost", "username", "password", "database");

if (!$conn) {
    die("Connection failed: " . mysqli_connect_error());
}

// Handle POST request to submit feedback
if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $user_id = $_POST["user_id"];
    $mentor_id = $_POST["mentor_id"];
    $feedback = $_POST["feedback"];
    // Add additional fields as needed
    
    // Insert data into feedback table
    $sql = "INSERT INTO feedback (user_id, mentor_id, feedback) VALUES ('$user_id', '$mentor_id', '$feedback')";
    
    if (mysqli_query($conn, $sql)) {
        echo "Feedback submitted successfully";
    } else {
        echo "Error: " . $sql . "<br>" . mysqli_error($conn);
    }
}

// Close connection
mysqli_close($conn);
?>
