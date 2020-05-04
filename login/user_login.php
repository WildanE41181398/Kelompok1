<?php
if($_SERVER['REQUEST_METHOD']=='POST'){

    include 'DatabaseConfig.php';

    $con = mysqli_connect($HostName,$HostUser,$HostPass,$DatabaseName);

    $email = $_POST['email'];
    $password = $_POST['password'];

    $Sql_Query = "select * from user where email = '$email' and password = '$password' ";

    $check = mysqli_fetch_array(mysqli_query($con,$Sql_Query));

    if(isset($check)){
        echo "Data Sesuai";
    }
    else{
        echo "Email atau Password yang dimasukkan salah";
    }
    }else{
        echo "Cek Lagi";
    }
    

mysqli_close($con);

?>