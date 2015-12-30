<?php

require_once('header.php');
if($isLogged){
    //echo $twig->render('index.html.twig');
    require_once('index.php');
    require_once('footer.php');
}
else {
    
    require_once('connection.php');
}

?>