<?php
// Database connection
$conn = mysqli_connect("localhost", "username", "password", "database");

if (!$conn) {
    die("Connection failed: " . mysqli_connect_error());
}

// Handle POST request to delete voice note
if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $note_id = $_POST["note_id"];
    // Add additional fields as needed
    
    // Delete data from voice_notes table
    $sql = "DELETE FROM voice_notes WHERE id = '$note_id'";
    
    if (mysqli_query($conn, $sql)) {
        echo "Voice note deleted successfully";
    } else {
        echo "Error: " . $sql . "<br>" . mysqli_error($conn);
    }
}

// Close connection
mysqli_close($conn);
?>
