<?php

define("CONNECTION_SUCCESS", "connection");
define("CONNECTION_DENIED", "compte introuvable");

define("USER_ADDED", "utilisateur ajouté");
define("USER_DELETED", "utilisateur supprimé");
define("USER_ADD_FAIL", "erreur lors de l'ajout de l'utilisateur");
define("USER_DELETE_FAIL", "erreur lors de la suppression de l'utilisateur");
define("USER_EXIST", "l'utilisateur existe");
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
        }
        else if($res == DENIED){
            echo NO_USER_REGISTERED;
        }
        
        /*foreach($res as $id){
            echo $id.', ';
        }*/
        
        //print_r($res);
        
    }else{
       echo PARAMETERS_MISSING; 
    }
       
}

function registerRequest(){
    // return json response 
    $json = array();

    $pseudo  = $_POST["pseudo"];
    $email = $_POST["email"];
    $password = $_POST["password"];
    $gcm_regid = $_POST["regId"];
    
    $success = "true";
    $error = "erreur lors de l'inscription";
    
    if (isset($pseudo) 
         && isset($email) 
         && isset($password) 
         && isset($gcm_regid)) {

        // Store user details in db
        $res = register($name, $email, $password, $gcm_regid);
        if(res){
            echo USER_ADDED;
        } else {
            echo USER_ADD_FAIL;
        }

/*       $registration_ids = array($gcm_regid);
        $message = new Message();
        $message->setContent("inscription réussie");
        $message->setTitle("success");
        $result = sendNotification($registration_ids, $message);

        echo $result;*/
    } else {
        echo PARAMETERS_MISSING;
    }
}

function deleteUserRequest(){
    $email  = $_POST["email"];
    
    if (isset($email)) {
        $res = deleteUser($email);
        
        if($res == SUCCESS){
            echo USER_DELETED;
        }
        else if($res == ERROR){
            echo USER_DELETE_FAIL;
        }
        else if($res == DENIED){
            echo USER_NOT_FOUND;
        }
    } else {
        echo PARAMETERS_MISSING;
    }
    
}

function connectionRequest(){
    $pseudo  = $_POST["pseudo"];
    $password  = $_POST["password"];
    
    if (isset($pseudo) && isset($password)) {
        $res = connection($pseudo, $password);
        
        if($res == SUCCESS){
            echo CONNECTION_SUCCESS;
        }
        else if($res == ERROR){
            echo CONNECTION_DENIED;
        }
    } else {
        echo PARAMETERS_MISSING;
    }
}

/**
 * Get user by email
 */
function getUserByEmailRequest() {
    $email  = $_POST["email"];
    if (isset($email)) {
        echo (json_encode(getUserByEmail($email)));
    } else {
        echo PARAMETERS_MISSING;
    }
}

/**
 * search user
 */
function searchUserRequest() {
    $w  = $_POST["w"];
    $page  = $_POST["page"];
    if (isset($w)) {
       echo (json_encode(searchUser($w, $page)));
    } else {
        echo PARAMETERS_MISSING;
    }
}

function userExistRequest(){
    $email  = $_POST["email"];
    $pseudo  = $_POST["pseudo"];
    
    if (isset($pseudo)) {
        $res = userExistPseudo($pseudo);
    } else if (isset($email)) {
        $res = userExistMail($email);
    } 
    
    if(isset($res)){
        if($res == SUCCESS){
            echo USER_EXIST;
        }
        else if($res == ERROR){
            echo USER_NOT_FOUND;
        } 
        
    } else {
        echo PARAMETERS_MISSING;
    }
    
}

function groupExistRequest(){
    $group  = $_POST["name"];
    $creator = $_POST["creator"];
    
    if (isset($name) && isset($creator)) {
        $res = groupExist($name, $creator); 
        
        if($res == SUCCESS){
            echo GROUP_EXIST;
        }
        else if($res == ERROR){
            echo GROUP_NOT_FOUND;
        }
    } else {
        echo PARAMETERS_MISSING;
    }
}

function isInGroupRequest(){
    $user_id  = $_POST["user_id"];
    $group_id = $_POST["group_id"];
    
    if (isset($user_id) && isset($group_id)) {
        $res = isInGroup($user_id, $group_id);
        
        if($res == SUCCESS){
            echo USER_IS_IN_GROUP;
        }
        else if($res == ERROR){
            echo USER_IS_NOT_IN_GROUP;
        }
    } else {
        echo PARAMETERS_MISSING;
    }
}

function addToGroupRequest(){
    $user_id  = $_POST["user_id"];
    $group_id = $_POST["group_id"];
    
    if (isset($user_id) && isset($group_id)) {
        $res = addToGroup($user_id, $group_id);
        
        if($res == SUCCESS){
            echo ADDED_TO_GROUP;
        }
        else if($res == ERROR){
            echo ADD_TO_GROUP_FAIL;
        }
    } else {
        echo PARAMETERS_MISSING;
    }
}
?>