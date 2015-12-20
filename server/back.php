<?php
require_once('loader.php');
require_once 'lib/vendor/autoload.php';

$loader = new Twig_Loader_Filesystem('./');
$twig = new Twig_Environment($loader, array(
    'debug ' => true 
));

if (isset($_POST["login"]) && isset($_POST["password"])) {
    adminConnection($_POST["login"], $_POST["password"]);
}

$loader = new Twig_Loader_Filesystem('./');
$twig = new Twig_Environment($loader, array(

    'debug ' => true 
));

//'cache' => 'cache/',

if(isLoggedAdmin()){

    echo $twig->render('index.html.twig');
}
else {
    
    echo $twig->render('connection.html.twig');
}
?>



