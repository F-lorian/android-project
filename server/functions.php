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
        
        //echo 'notification envoyée: <br/> message :'.$message.'<br/>à :';
        /*foreach($registrationIds as $id){
            echo $id.' ';
        }*/
        
        echo 'notification envoyée';
        
    }else{
       echo 'erreur'; 
    }
       
}


function storeUser($name, $email, $password, $gcm_regid) {
        
    try {
        $result = array();
        $dbh = new PDO('mysql:host='.DB_HOST.';dbname='.DB_DATABASE, DB_USER, DB_PASSWORD);
        $stmt = $dbh->prepare("INSERT INTO gcm_users (name, email, gcm_regid, created_at) VALUES ('$name', '$email', '$gcm_regid', NOW())");
        $stmt->execute();
        $dbh = null;
        
        if (userExist($email)) { 
            return true;
        } else {
            return false;
        }
        
    } catch (PDOException $e) {
        echo "Erreur !: " . $e->getMessage() . "<br/>";
        die();
    }
    
}

/**
 * Get user by email
 */
function getUserByEmail($email) {
    try {
        $result = array();
        $dbh = new PDO('mysql:host='.DB_HOST.';dbname='.DB_DATABASE, DB_USER, DB_PASSWORD);
        $stmt = $dbh->prepare("SELECT * FROM user WHERE email = '$email' LIMIT 1");
        $stmt->execute();
        while ($row = $stmt->fetch()) {
            $result[] = $row;
            //echo $row;
        }

        return $result;
        
    } catch (PDOException $e) {
        echo "Erreur !: " . $e->getMessage() . "<br/>";
        die();
    } 
}

/**
 * Get user by email
 */
function getUserByEmailRequest() {
    
    $email  = $_POST["email"];
    
    echo (json_encode(getUserByEmail($email)));
}

// Getting all registered users
function getAllUsers() {
        
    try {
        $result = array();
        $dbh = new PDO('mysql:host='.DB_HOST.';dbname='.DB_DATABASE, DB_USER, DB_PASSWORD);
        $stmt = $dbh->prepare("SELECT * FROM user");
        $stmt->execute();
        while ($row = $stmt->fetch()) {
            $result[] = $row;
        }

        //echo (json_encode($result));
        return $result;
        
    } catch (PDOException $e) {
        echo "Erreur !: " . $e->getMessage() . "<br/>";
        die();
    } 
}

function userExistRequest(){
    $email  = $_POST["email"];
    
    echo userExist($email);
    
}

function userExist($email) {
     
    try {
        $result = array();
        $dbh = new PDO('mysql:host='.DB_HOST.';dbname='.DB_DATABASE, DB_USER, DB_PASSWORD);
        $stmt = $dbh->prepare("SELECT email FROM user WHERE email = '$email' LIMIT 1");
        $stmt->execute();
        while ($row = $stmt->fetch()) {
            $result[] = $row;
        }

        $NumOfRows = count($result);
        if ($NumOfRows > 0) {
            return true;
        } else {
            return false;
        }
        
    } catch (PDOException $e) {
        echo "Erreur !: " . $e->getMessage() . "<br/>";
        die();
    }
}


function register($name, $email, $password, $gcm_regid){
    // return json response 
    $json = array();

    $name  = $_POST["name"];
    $email = $_POST["email"];
    $password = $_POST["password"];
    $gcm_regid = $_POST["regId"];
    
    $success = "true";
    $error = "erreur lors de l'inscription";
    
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
        if(res){
            echo $success;
        } else {
            echo $error;
        }

/*        $registration_ids = array($gcm_regid);
        $message = new Message();
        $message->setContent("inscription réussie");
        $message->setTitle("success");

        $result = sendNotification($registration_ids, $message);*/

        echo $result;
    } else {
        echo $error;
    }
}

function deleteUser(){
    $email  = $_POST["email"];
    
    $success = "true";
    $error = "erreur lors de la suppression";
    
    try {
         
        if(userExist($email)){
            $result = array();
            $dbh = new PDO('mysql:host='.DB_HOST.';dbname='.DB_DATABASE, DB_USER, DB_PASSWORD);
            $stmt = $dbh->prepare("DELETE FROM user WHERE email = '$email' LIMIT 1");
            //$stmt->execute();
            
            if (!userExist($email)) { 
                echo $success;
            } else {
                echo $error;
            }
            return;
        }
        echo $error.': utilisateur introuvable';
        
    } catch (PDOException $e) {
        echo "Erreur !: " . $e->getMessage() . "<br/>";
        die();
    }
    
}

function addGroup(){
    $name  = $_POST["name"];
    $type = $_POST["type"];
    $creator = $_POST["user_id"];
    
    $success = "true";
    $error = "erreur lors de la création du groupe";
    
    try {
         
        if(!groupExist($name, $creator)){
            $result = array();
            $dbh = new PDO('mysql:host='.DB_HOST.';dbname='.DB_DATABASE, DB_USER, DB_PASSWORD);
            $stmt = $dbh->prepare("INSERT INTO group (name, type, creator) VALUES ('$name', '$type', '$creator')");
            $stmt->execute();
            
            if (groupExist($name, $creator)) { 
                echo $success;
            } else {
                echo $error;
            }
            return;
        }
        echo $error.': le groupe existe déjà';
        
    } catch (PDOException $e) {
        echo "Erreur !: " . $e->getMessage() . "<br/>";
        die();
    }
}

function groupExistRequest(){
    $name  = $_POST["name"];
    $creator = $_POST["user_id"];
    
    echo isInGroup($name, $creator);
    
}

function groupExist($name, $creator) {
    try {
         
        $result = array();
        $dbh = new PDO('mysql:host='.DB_HOST.';dbname='.DB_DATABASE, DB_USER, DB_PASSWORD);
        $stmt = $dbh->prepare("SELECT * FROM group WHERE name = '$name' AND creator = '$creator' LIMIT 1");
        $stmt->execute();
        while ($row = $stmt->fetch()) {
            $result[] = $row;
        }

        $NumOfRows = count($result);
        if ($NumOfRows > 0) {
            return true;
        } else {
            return false;
        }
 
    } catch (PDOException $e) {
        echo "Erreur !: " . $e->getMessage() . "<br/>";
        die();
    }
}

function addToGroup(){
    $user_id  = $_POST["user_id"];
    $group_id = $_POST["group_id"];
    
    $success = 'utilisateur ajouté au groupe';
    $error = 'erreur lors de l\'ajout au groupe';
    
     try {
         
        if(!isInGroup($user_id, $group_id)){
            $result = array();
            $dbh = new PDO('mysql:host='.DB_HOST.';dbname='.DB_DATABASE, DB_USER, DB_PASSWORD);
            $stmt = $dbh->prepare("INSERT INTO user_in_group (user, group) VALUES ('$user_id', '$group_id')");
            $stmt->execute();
            $dbh = null;
            
            if (isInGroup($user_id, $group_id)) { 
                echo $success;
            } else {
                echo $error;
            }
            return;
        }
        echo $error.': l\'utilisateur appartient déjà au groupe';
        
    } catch (PDOException $e) {
        echo "Erreur !: " . $e->getMessage() . "<br/>";
        die();
    }
}

function isInGroupRequest(){
    $user_id  = $_POST["user_id"];
    $group_id = $_POST["group_id"];
    
    echo isInGroup($user_id, $group_id);
    
}

function isInGroup($user_id, $group_id){
     try {
        $result = array();
        $stmt = $dbh->prepare("SELECT * FROM user_in_group WHERE user = '$user_id' AND group = '$group_id' LIMIT 1");
        $stmt->execute();
        while ($row = $stmt->fetch()) {
            $result[] = $row;
        }
        $dbh = null;
         
        if (count($result) > 0) { 
            return true;
        } else {
            return false;
        }
        
    } catch (PDOException $e) {
        echo "Erreur !: " . $e->getMessage() . "<br/>";
        die();
    }
}

function removeFromGroup(){
    $user_id  = $_POST["user_id"];
    $group_id = $_POST["group_id"];
    
    $success = 'utilisateur retiré du groupe';
    $error = 'erreur lors de la suppression du groupe';
    
     try {
         
        if(isInGroup($user_id, $group_id)){
            $result = array();
            $dbh = new PDO('mysql:host='.DB_HOST.';dbname='.DB_DATABASE, DB_USER, DB_PASSWORD);
            $stmt = $dbh->prepare("DELETE FROM user_in_group WHERE user = '$user_id' AND group = '$group_id'");
            $stmt->execute();
            $dbh = null;
            
            if (!isInGroup($user_id, $group_id)) { 
                echo $success;
            } else {
                echo $error;
            }
            return;
        }
        echo $error.': l\'utilisateur n\'est pas dans le groupe';
        
    } catch (PDOException $e) {
        echo "Erreur !: " . $e->getMessage() . "<br/>";
        die();
    }
}

function addSignalement(){
    $content  = $_POST["content"];
    $note = $_POST["note"];
    $date = date('Y-m-d h:m:s');
    $diffusion = $_POST["diffusion"];
    $type = $_POST["type"];
    
    $success = "signalement ajouté";
    $error = "erreur lors de l'ajout du signalement";
    
     try {
         
        $result = array();
        $dbh = new PDO('mysql:host='.DB_HOST.';dbname='.DB_DATABASE, DB_USER, DB_PASSWORD);
        $stmt = $dbh->prepare("INSERT INTO signalement (content, note, date, diffusion, type) VALUES ('$content', '$note', '$date', '$diffusion', '$type')");
        $stmt->execute();
        $dbh = null;

      /*  if (!isInGroup($user_id, $group_id)) { 
            echo $success;
        } else {
            echo $error;
        }*/
         
        echo $success;
        
    } catch (PDOException $e) {
        echo "Erreur !: " . $e->getMessage() . "<br/>";
        die();
    }
}

function deleteSignalement(){
    $sign_id  = $_POST["sign_id"];

    $success = "signalement ajouté";
    $error = "erreur lors de l'ajout du signalement";
    
     try {
         
        $result = array();
        $dbh = new PDO('mysql:host='.DB_HOST.';dbname='.DB_DATABASE, DB_USER, DB_PASSWORD);
        $stmt = $dbh->prepare("DELETE FROM signalement WHERE id = '$sign_id'");
        $stmt->execute();
        $dbh = null;

      /*  if (!isInGroup($user_id, $group_id)) { 
            echo $success;
        } else {
            echo $error;
        }*/
         
        echo $success;
        
    } catch (PDOException $e) {
        echo "Erreur !: " . $e->getMessage() . "<br/>";
        die();
    }
}

?>