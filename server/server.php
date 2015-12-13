<?php   

require_once('loader.php');



if(isset($dbh)){

/*
    $dbh = new PDO('mysql:host=localhost;dbname=dgback', "gara", "gara123");
    $result = array();
    //echo ($_REQUEST['request']);

    $stmt = $dbh->prepare($_REQUEST['request']);
    $stmt->execute();
    while ($row = $stmt->fetch()) {
        $result[] = $row;
        //echo $row;
    }


    echo (json_encode($result));
    $dbh = null;
*/

    //traitement de l'action demandée/////////////////////////
    if(isset($_REQUEST['action'])){
        
        switch($_REQUEST['action']){
            case "register":
                register();
                break;
            case "getUserByEmail":
                getUserByEmail($_POST["email"]);
                break;
            case "addGroup":
                addGroup();
                break;
            case "addToGroup":
                addToGroup();
                break;
            case "removeFromGroup":
                removeFromGroup();
                break;
            case "addSignalement":
                addSignalement();
                break;
            case "sendNotificationPost":
                sendNotificationPost();
                break;
        }
    }

    //notification/////////////////////////
/*
    $message = new Message();

    $content 	= 'here is a message. message';
    $title		= 'This is a title. title';
    $subtitle	= 'This is a subtitle. subtitle';
    $tickerText	= 'Ticker text here...Ticker text here...Ticker text here';
    $vibrate	= 1;
    $sound		= 1;
    $largeIcon	= 'large_icon';
    $smallIcon	= 'small_icon';

    $message->setContent($content);
    $message->setTitle($title);
    $message->setSubtitle($subtitle);
    $message->setTickerText($tickerText);
    $message->setVibrate($vibrate);
    $message->setSound($sound);
    $message->setLargeIcon($largeIcon);
    $message->setSmallIcon($smallIcon);

    $registrationIds = array( $_REQUEST['id'] );

    sendNotification($registrationIds, $message);*/
    
//$users = array(array('name' => 'nom test'), array('name' => 'nom test 2'));
$users = getAllUsers();

///////////////////////////////////////

}
 
?>