<?php
// Database connection
$conn = mysqli_connect("localhost", "username", "password", "database");

if (!$conn) {
    die("Connection failed: " . mysqli_connect_error());
}

// Handle POST request to add mentor
if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $name = $_POST["name"];
    $email = $_POST["email"];
    // Add additional fields as needed
    
    // Insert data into mentors table
    $sql = "INSERT INTO mentors (name, email) VALUES ('$name', '$email')";
    
    if (mysqli_query($conn, $sql)) {
        echo "Mentor added successfully";
    } else {
        echo "Error: " . $sql . "<br>" . mysqli_error($conn);
    }
}

// Close connection
mysqli_close($conn);
?>
