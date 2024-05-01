<?php
// Establish database connection
$conn = mysqli_connect("localhost", "root", "", "login_register");

// Check if the connection is successful
if (!$conn) {
    die("Connection failed: " . mysqli_connect_error());
}

// Check if file is uploaded
if(isset($_FILES['upload'])) {
    // Retrieve file details
    $file_name = $_FILES['upload']['name'];
    $file_tmp = $_FILES['upload']['tmp_name'];
    
    // Specify upload directory
    $upload_directory = "images/";
    
    // Generate a unique filename to avoid conflicts
    $filename = "IMG" . rand() . ".jpg";
    
    // Move uploaded file to the upload directory
    if(move_uploaded_file($file_tmp, $upload_directory . $filename)) {
        // File uploaded successfully, proceed with database insertion
        $qry = "INSERT INTO image (image) VALUES ('$filename')";
        $res = mysqli_query($conn, $qry);
        
        if($res) {
            echo "File Uploaded Successfully";
        } else {
            echo "Could not upload File";
        }
    } else {
        echo "Failed to move uploaded file";
    }
} else {
    echo "No file uploaded";
}

// Close database connection
mysqli_close($conn);
?>
