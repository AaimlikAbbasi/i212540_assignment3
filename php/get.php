<?php
$conn = mysqli_connect("localhost", "root", "", "login_register");

if (!$conn) {
    die("Connection failed: " . mysqli_connect_error());
}

$qry = "SELECT * FROM users";
$result = mysqli_query($conn, $qry);

$data = array();
if (mysqli_num_rows($result) > 0) {
    while ($res = mysqli_fetch_assoc($result)) {
        $data[] = $res;
    }
}

echo json_encode($data);

mysqli_close($conn);
?>
