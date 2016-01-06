<?php   

require_once('loader.php');



if(isset($dbh)){
    $dbh = null;

    //traitement de l'action demandée/////////////////////////
    if(isset($_REQUEST['action'])){
        
        switch($_REQUEST['action']){
            case "register":
                registerRequest();
                break;
            case "connection":
                connectionRequest();
                break;
            case "deconnection":
                deconnectionRequest();
                break;
            case "getUserByEmail":
                getUserByEmailRequest();
                break;
            case "searchUser":
                searchUserRequest();
                break;
            case "getAllUsers":
                getAllUsersRequest();
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
            case "sendNotification":
                sendNotificationRequest();
                break;
            case "deleteUser":
                deleteUserRequest();
                break;
            case "userExist":
                userExistRequest();
                break;
            case "isInGroup":
                isInGroupRequest();
                break;
            case "groupExist":
                groupExistRequest();
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
    


///////////////////////////////////////

}
 
?>