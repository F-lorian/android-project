<?php
require_once('config.php');
require_once('functions.php');

/*
// connecting to mysql
$conn = mysql_connect(DB_HOST, DB_USER, DB_PASSWORD);
// selecting database
if(!mysql_select_db(DB_DATABASE))
  print "Not connected with database.";
*/

try {

    $dbh = new PDO('mysql:host='.DB_HOST.';dbname='.DB_DATABASE, DB_USER, DB_PASSWORD);

} catch (PDOException $e) {
    echo 'Échec lors de la connexion à la base de données: ' . $e->getMessage();
}
 
?>