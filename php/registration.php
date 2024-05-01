<?php
session_start();
$conn = mysqli_connect("localhost", "root", "", "login_register");

if (isset($_POST['submit'])) {
    $name = $_POST['name'];
    $email = $_POST['email'];
    $contact = $_POST['contact'];
    $country = $_POST['country'];
    $city = $_POST['city'];
    $password = $_POST['password'];

    // Hash the password for security
    $hashed_password = password_hash($password, PASSWORD_DEFAULT);

    if (isset($_FILES['upload']) && $_FILES['upload']['error'] === UPLOAD_ERR_OK) {
        $image_name = $_FILES['upload']['name'];
        $image_tmp = $_FILES['upload']['tmp_name'];
        $image_destination = "images/" . $image_name;

        if (move_uploaded_file($image_tmp, $image_destination)) {
            // Use prepared statement to prevent SQL injection
            $qry = "INSERT INTO `users` (`name`, `email`, `contact_no`, `country`, `city`, `password`, `image`)
            VALUES (?, ?, ?, ?, ?, ?, ?)";

            $stmt = mysqli_prepare($conn, $qry);
            mysqli_stmt_bind_param($stmt, "ssissss", $name, $email, $contact, $country, $city, $hashed_password, $image_name);

            if (mysqli_stmt_execute($stmt)) {
                $_SESSION['msg'] = "File Uploaded Successfully";
            } else {
                $_SESSION['msg'] = "Could not upload File";
            }

            mysqli_stmt_close($stmt);
        } else {
            $_SESSION['msg'] = "File upload failed";
        }

        header("location:" . $_SERVER['PHP_SELF']);
        exit();
    }
}
?>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>File Upload</title>
</head>
<body>
    <center>
        <table border="1">
            <form name="frm" action="<?php echo $_SERVER['PHP_SELF'];?>" method="post" enctype="multipart/form-data">
                <tr>
                    <td>Enter Your Name</td>
                    <td><input type="text" name="name"></td>
                </tr>
                <tr>
                    <td>Enter Your Email</td>
                    <td><input type="email" name="email"></td>
                </tr>
                <tr>
                    <td>Enter Your Contact Number</td>
                    <td><input type="text" name="contact"></td>
                </tr>
                <tr>
                    <td>Select Your Country</td>
                    <td><input type="text" name="country"></td>
                </tr>
                <tr>
                    <td>Enter Your City</td>
                    <td><input type="text" name="city"></td>
                </tr>
                <tr>
                    <td>Enter Your Password</td>
                    <td><input type="password" name="password"></td>
                </tr>
                <tr>
                    <td>Upload Your Image</td>
                    <td><input type="file" name="upload"></td>
                </tr>
                <tr>
                    <td colspan="2" align="center">
                        <input type="submit" name="submit" value="Upload">
                    </td>
                </tr>
            </form>   
        </table>
        <?php
        if(isset($_SESSION['msg'])) {
            echo $_SESSION['msg'];
            unset($_SESSION['msg']);
        }
        ?>
    </center>
</body>
</html>
