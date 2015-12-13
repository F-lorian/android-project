<?php
require_once('notification.php');

function sendNotification($registrationIds, $message){
    $notification = new Notification();
    $notification->setMessage($message);
    $notification->setRegistrationId($registrationIds);
    $notification->send();
}

function sendNotificationPost(){
    $registrationIds  = $_POST["regIDs"];
    $message = $_POST["message"];
    
    if(isset($registrationIds) && isset($message)){
        
        $registrationIds = explode('|',$registrationIds);
         /*$notification = new Notification();
        $notification->setMessage($message);
        $notification->setRegistrationId($registrationIds);
        $notification->send();*/
        
         echo 'notification envoyée: <br/> message :'.$message.'<br/>à :';
        foreach($registrationIds as $id){
            echo $id.' ';
        }
    }else{
       echo 'erreur'; 
    }
       
}


function storeUser($name, $email, $password, $gcm_regid) {
        
    $result = array();
    $dbh = new PDO('mysql:host='.DB_HOST.';dbname='.DB_DATABASE, DB_USER, DB_PASSWORD);
    $stmt = $dbh->prepare("INSERT INTO gcm_users
                        (name, email, gcm_regid, created_at) VALUES ('$name', '$email', '$gcm_regid', NOW())");
    $result = $stmt->execute();
    
    $result = array();
    $stmt = $dbh->prepare("SELECT * FROM user WHERE email = '$email' LIMIT 1");
    $result = $stmt->execute();
    while ($row = $stmt->fetch()) {
        $result[] = $row;
    }
         
    // return user details 
    if (mysql_num_rows($result) > 0) { 
        return $result;
    } else {
        return false;
    }
    
}

/**
 * Get user by email
 */
function getUserByEmail($email) {
    $result = array();
    $dbh = new PDO('mysql:host='.DB_HOST.';dbname='.DB_DATABASE, DB_USER, DB_PASSWORD);
    $stmt = $dbh->prepare("SELECT * FROM user WHERE email = '$email' LIMIT 1");
    $stmt->execute();
    while ($row = $stmt->fetch()) {
        $result[] = $row;
        //echo $row;
    }
    
    //echo (json_encode($result));
    return $result;
   
}

// Getting all registered users
function getAllUsers() {
    $result = array();
    $dbh = new PDO('mysql:host='.DB_HOST.';dbname='.DB_DATABASE, DB_USER, DB_PASSWORD);
    $stmt = $dbh->prepare("SELECT * FROM user");
    $stmt->execute();
    while ($row = $stmt->fetch()) {
        $result[] = $row;
        //echo $row;
    }
    
    //echo (json_encode($result));
    return $result;
}

// Validate user
function userExist($email) {
    
    $result = array();
    $dbh = new PDO('mysql:host='.DB_HOST.';dbname='.DB_DATABASE, DB_USER, DB_PASSWORD);
    $stmt = $dbh->prepare("SELECT email FROM user WHERE email = '$email' LIMIT 1");
    $stmt->execute();
    while ($row = $stmt->fetch()) {
        $result[] = $row;
        //echo $row;
    }
    
    $NumOfRows = mysql_num_rows($result);
    if ($NumOfRows > 0) {
        // user existed
        return true;
    } else {
        // user not existed
        return false;
    }
}


function register($name, $email, $password, $gcm_regid){
    // return json response 
    $json = array();

    $name  = $_POST["name"];
    $email = $_POST["email"];
    $password = $_POST["password"];
    $gcm_regid = $_POST["regId"];
    

    /**
     * Registering a user device in database
     * Store reg id in users table
     */
    if (isset($name) 
         && isset($email) 
         && isset($password) 
         && isset($gcm_regid)) {

        // Store user details in db
        $res = storeUser($name, $email, $password, $gcm_regid);

        $registration_ids = array($gcm_regid);
        $message = new Message();
        $message->setContent("inscription réussie");
        $message->setTitle("success");

        $result = sendNotification($registration_ids, $message);

        echo $result;
    } else {
        // user details not found
    }
}


function addGroup(){
    $name  = $_POST["name"];
    $type = $_POST["type"];
}

function addToGroup(){
    $user_id  = $_POST["user_id"];
    $group_id = $_POST["group_id"];
}

function removeFromGroup(){
    $user_id  = $_POST["user_id"];
    $group_id = $_POST["group_id"];
}

function addSignalement(){
    $content  = $_POST["content"];
    $note = $_POST["note"];
    $diffusion = $_POST["diffusion"];
    $type = $_POST["type"];
}

?>