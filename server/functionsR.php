<?php

define("CONNECTION_SUCCESS", "connection");
define("CONNECTION_DENIED", "compte introuvable");
define("LOG_OUT", "deconnexion réussie");

define("USER_ADDED", "utilisateur ajouté");
define("USER_DELETED", "utilisateur supprimé");
define("USER_ADD_FAIL", "erreur lors de l'ajout de l'utilisateur");
define("USER_DELETE_FAIL", "erreur lors de la suppression de l'utilisateur");
define("USER_EXIST", "l'utilisateur existe");
define("PSEUDO_EXIST", "ce pseudo est déjà utilisé");
define("USER_NOT_FOUND", "utilisateur introuvable");
define("USER_IS_IN_GROUP", "l'utilisateur est dans ce groupe");
define("USER_IS_NOT_IN_GROUP", "l'utilisateur n'est pas dans ce groupe");

define("ADDED_TO_GROUP", "utilisateur ajouté au groupe");
define("REMOVED_FROM_GROUP", "utilisateur retiré du groupe");
define("ADD_TO_GROUP_FAIL", "erreur lors de l'ajout au groupe");
define("REMOVE_FROM_GROUP_FAIL", "erreur lors du retrait du groupe");

define("GROUP_ADDED", "groupe crée");
define("GROUP_DELETED", "groupe supprimé");
define("GROUP_ADD_FAIL", "erreur lors de l'ajout au groupe");
define("GROUP_DELETE_FAIL", "erreur lors de la suppression du groupe");
define("GROUP_EXIST", "le groupe existe");
define("GROUP_NOT_FOUND", "groupe introuvable");

define("SIGN_ADDED", "signalement ajouté");
define("SIGN_DELETED", "signalement supprimé");
define("SIGN_ADD_FAIL", "erreur lors de l'ajout du signalement");
define("SIGN_DELETE_FAIL", "erreur lors de la suppression du signalement");

define("NOTIFICATION_SEND", "notification envoyée");
define("SEND_FAIL", "erreur lors de l'envoi");
define("TO_SELECTION", "à la selection");
define("TO_ALL", "à tous les membres");

define("NO_USER_REGISTERED", "aucun membre n'est inscrit");

define("ADD_FAIL", "erreur lors de l'ajout");
define("DELETE_FAIL", "erreur lors de la suppression");

define("PARAMETERS_MISSING", "paramètre(s) manquant(s)");

function sendNotificationRequest(){
    $registrationIds  = $_POST["regIDs"];
    $message = $_POST["message"];
    
    if(isset($registrationIds) && isset($message)){
        
        if($registrationIds != ""){
            $registrationIds = explode('|',$registrationIds);
            $target = TO_SELECTION;
        }
        else{
            $registrationIds = null;
            $target = TO_ALL;
        }
        
        $res = sendNotification($registrationIds, $message);
        
        if($res == SUCCESS){
            echo NOTIFICATION_SEND." ".$target.'<br/> message :'.$message;
        } else if($res == DENIED){
            echo NO_USER_REGISTERED;
        }
        
        /*foreach($res as $id){
            echo $id.', ';
        }*/
        
        //print_r($res);
        
    } else {
       echo PARAMETERS_MISSING; 
    }
       
}

function getReplyMessage($state, $message, $data){
    
    $d = json_encode($data);
    return '{'
        .'"state":"'.$state.'",'
        .'"message":"'.$message.'",'
        .'"data":'.$d
        .'}';
}

function registerRequest(){
    
    if (isset($_POST["pseudo"]) 
         && isset($_POST["password"]) 
         && isset($_POST["regId"])) {

        // Store user details in db
        $res = register($_POST["pseudo"], $_POST["password"], $_POST["regId"]);
        if($res == ERROR){
            echo getReplyMessage(ERROR, USER_ADD_FAIL, array());
        } else if($res == DENIED){
            echo getReplyMessage(DENIED, PSEUDO_EXIST, array());
        } else {
            echo getReplyMessage(SUCCESS, USER_ADDED, array('pseudo'=>$_POST["pseudo"], 'id'=>$res));
        }

/*       $registration_ids = array($gcm_regid);
        $message = new Message();
        $message->setContent("inscription réussie");
        $message->setTitle("success");
        $result = sendNotification($registration_ids, $message);

        echo $result;*/
    } else {
        echo getReplyMessage(ERROR, PARAMETERS_MISSING, array());
    }
}

function deleteUserRequest(){
    
    if (isset($_POST["regID"])) {
        $res = deleteUserByRegId($_POST["regID"]);
    } else if ($_POST["email"]) {
        $res = deleteUserByMail($_POST["email"]);
    } 
    
    if(isset($res)){
        if($res == SUCCESS){
            echo getReplyMessage(SUCCESS, USER_DELETED, array());
        }
        else if($res == ERROR){
            echo getReplyMessage(ERROR, USER_DELETE_FAIL, array());
        }
        else if($res == DENIED){
            echo getReplyMessage(DENIED, USER_NOT_FOUND, array());
        }
        
    } else {
        echo getReplyMessage(ERROR, PARAMETERS_MISSING, array());
    }
    
}

function connectionRequest(){
    if (isset($_POST["pseudo"]) && isset($_POST["password"])) {
        $res = connection($_POST["pseudo"], $_POST["password"], $_POST["regId"]);
        
        if($res == DENIED){
            echo getReplyMessage(DENIED, CONNECTION_DENIED, array());
        }
        else {
            echo getReplyMessage(SUCCESS, CONNECTION_SUCCESS, array('pseudo'=>$_POST["pseudo"], 'id'=>$res));
        }
    } else {
        echo getReplyMessage(ERROR, PARAMETERS_MISSING, array());
    }
}


function deconnectionRequest(){
    if (isset($_POST["pseudo"]) && isset($_POST["id"])) {
        $res = deconnection($_POST["id"], $_POST["pseudo"]);
        echo getReplyMessage(SUCCESS, LOG_OUT, array());
   
    } else {
        echo getReplyMessage(ERROR, PARAMETERS_MISSING, array());
    }
}

/**
 * Get user by email
 */
function getUserByEmailRequest() {
    if (isset($_POST["email"])) {
        echo (json_encode(getUserByEmail($_POST["email"])));
    } else {
        echo getReplyMessage(ERROR, PARAMETERS_MISSING, array());
    }
}

/**
 * search user
 */
function searchUserRequest() {
    if (isset($_POST["w"])) {
       echo (json_encode(searchUser($_POST["w"], $_POST["page"])));
    } else {
        echo getReplyMessage(ERROR, PARAMETERS_MISSING, array());
    }
}

function getAllUsersRequest() {
    if (isset($_POST["page"])) {
        echo (json_encode(getAllUsers($_POST["page"])));
    } else {
        echo getReplyMessage(ERROR, PARAMETERS_MISSING, array());
    }
}

function userExistRequest(){
    if (isset($_POST["pseudo"])) {
        $res = userExistPseudo($_POST["pseudo"]);
    } else if (isset($_POST["email"])) {
        $res = userExistMail($_POST["email"]);
    } 
    
    if(isset($res)){
        if($res == SUCCESS){
            echo getReplyMessage(SUCCESS, USER_EXIST, array());
        }
        else if($res == DENIED){
            echo getReplyMessage(DENIED, USER_NOT_FOUND, array());
        } 
        
    } else {
        echo getReplyMessage(ERROR, PARAMETERS_MISSING, array());
    } 
}

function groupExistRequest(){
    if (isset($_POST["name"]) && isset($_POST["creator"])) {
        $res = groupExist($_POST["name"], $_POST["creator"]); 
        
        if($res == SUCCESS){
            echo getReplyMessage(SUCCESS, GROUP_EXIST, array());
        }
        else if($res == DENIED){
            echo getReplyMessage(DENIED, GROUP_NOT_FOUND, array());
        }
    } else {
        echo getReplyMessage(ERROR, PARAMETERS_MISSING, array());
    }
}

function isInGroupRequest(){
    
    if (isset($_POST["user_id"]) && isset($_POST["group_id"])) {
        $res = isInGroup($_POST["user_id"], $_POST["group_id"]);
        
        if($res == SUCCESS){
            echo getReplyMessage(SUCCESS, USER_IS_IN_GROUP, array());
        }
        else if($res == DENIED){
            echo getReplyMessage(DENIED, USER_IS_NOT_IN_GROUP, array());
        }
    } else {
        echo getReplyMessage(ERROR, PARAMETERS_MISSING, array());
    }
}

function addToGroupRequest(){
    
    if (isset($_POST["user_id"]) && isset($_POST["group_id"])) {
        $res = addToGroup($_POST["user_id"], $_POST["group_id"]);
        
        if($res == SUCCESS){
            echo getReplyMessage(SUCCESS, ADDED_TO_GROUP, array());
        }
        else if($res == ERROR){
            echo getReplyMessage(ERROR, ADD_TO_GROUP_FAIL, array());
        }
    } else {
        echo getReplyMessage(ERROR, PARAMETERS_MISSING, array());
    }
}
?>