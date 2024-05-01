<?php
// Database connection
$conn = mysqli_connect("localhost", "username", "password", "database");

if (!$conn) {
    die("Connection failed: " . mysqli_connect_error());
}

// Handle POST request to send chat message
if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $sender_id = $_POST["sender_id"];
    $receiver_id = $_POST["receiver_id"];
    $message = $_POST["message"];
    // Add additional fields as needed
    
    // Insert data into chat_messages table
    $sql = "INSERT INTO chat_messages (sender_id, receiver_id, message) VALUES ('$sender_id', '$receiver_id', '$message')";
    
    if (mysqli_query($conn, $sql)) {
        echo "Message sent successfully";
    } else {
        echo "Error: " . $sql . "<br>" . mysqli_error($conn);
    }
}

// Close connection
mysqli_close($conn);
?>
