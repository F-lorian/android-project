<?php
require_once('server.php');


require_once 'lib/vendor/autoload.php';

$loader = new Twig_Loader_Filesystem('./');
$twig = new Twig_Environment($loader, array(

    'debug ' => true 
));

//'cache' => 'cache/',

echo $twig->render('index.html.twig', array('users' => $users));

?>



