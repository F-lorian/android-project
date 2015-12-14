<?php
require_once('loader.php');


require_once 'lib/vendor/autoload.php';

$loader = new Twig_Loader_Filesystem('./');
$twig = new Twig_Environment($loader, array(

    'debug ' => true 
));

//'cache' => 'cache/',

//$users = array(array('name' => 'nom test'), array('name' => 'nom test 2'));
$users = getAllUsers();

echo $twig->render('index.html.twig', array('users' => $users));

?>



