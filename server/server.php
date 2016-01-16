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
                addGroupRequest();
                break;
            case "addToGroup":
                addToGroup();
                break;
            case "removeFromGroup":
                removeFromGroup();
                break;
            case "getGroups":
                getGroupsRequest();
                break;
            case "getGroup":
                getGroupRequest();
                break;
            case "getMembers":
                getMembersRequest();
                break;
            case "acceptMember":
                acceptMemberRequest();
                break;
            case "refuseMember":
                refuseMemberRequest();
                break;
            case "removeMember":
                removeMemberRequest();
                break;
            case "editGroup":
                editGroupRequest();
                break;
            case "getGroupWithRestrict":
                getGroupWithRestrictRequest();
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
			case "getDataFormAddSignalement":
                getDataFormAddSignalementRequest();
                break;
			case "addSignalement":
                addSignalementRequest();
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