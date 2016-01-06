<?php
require_once('notification.php');

define("SUCCESS", "success");
define("ERROR", "error");
define("DENIED", "denied");


function isLoggedAdmin(){
    
   /* echo $_SESSION['id']; 
    echo $_SESSION['login'];
    echo $_SESSION['admin'];*/
    return (isset($_SESSION['id']) 
            && isset($_SESSION['login']) 
            && isset($_SESSION['admin']) 
            && $_SESSION['admin'] == true);
}

function adminConnection($login, $password){
    
    try {
        $result = array();
        $dbh = new PDO('mysql:host='.DB_HOST.';dbname='.DB_DATABASE, DB_USER, DB_PASSWORD);
        $stmt = $dbh->prepare("SELECT * FROM admin WHERE login = '$login' AND password = '$password'");
        $stmt->execute();
        while ($row = $stmt->fetch()) {
            $result[] = $row;
        }

        $NumOfRows = count($result);
        if ($NumOfRows == 1) {
            session_start ();
            $_SESSION['id'] = $result[0]['id'];
			$_SESSION['login'] = $result[0]['login'];
			$_SESSION['admin'] = true;
            return SUCCESS;
        } else {
            return ERROR;
        }
        
    } catch (PDOException $e) {
        echo "Erreur !: " . $e->getMessage() . "<br/>";
        die();
    }
}



	//Deconnexion
function deconnection($id, $pseudo){
    session_destroy(); // on detruit la session
    //header("location:" . ABSOLUTE_ROOT); // on redirige vers l'accueil
    try {
        $result = array();
        $dbh = new PDO('mysql:host='.DB_HOST.';dbname='.DB_DATABASE, DB_USER, DB_PASSWORD);
        $stmt = $dbh->prepare("UPDATE user u SET u.online = false WHERE u.pseudo='$pseudo' AND u.id='$id''");
        $stmt->execute();
        $dbh = null;
    
    } catch (PDOException $e) {
        echo "Erreur !: " . $e->getMessage() . "<br/>";
        die();
    }
}

function sendNotification($registrationIds, $message){
    $regIDs = null;
    if($registrationIds == null || count($registrationIds) == 0){
        $regIDs = getAllUsersGCMID();
        if($regIDs == null){
            return DENIED;
        }   
    }
    else{
        $regIDs = $registrationIds;
    }

   /* $notification = new Notification();
    $notification->setMessage($message);
    
    
    $notification->setRegistrationId($regIDs);
    $notification->send();*/
    
    return SUCCESS;
}

function isLogged(){
    return (isset($_SESSION['id']) && isset($_SESSION['pseudo']));
}


function connection($pseudo, $password, $regId) {
        
    try {
        $result = array();
        $dbh = new PDO('mysql:host='.DB_HOST.';dbname='.DB_DATABASE, DB_USER, DB_PASSWORD);
        $stmt = $dbh->prepare("SELECT * FROM user WHERE pseudo = '$pseudo' AND password = '$password'");
        $stmt->execute();
        /*while ($row = $stmt->fetch()) {
            $result[] = $row;
        }

        $NumOfRows = count($result);*/
        
        $NumOfRows = $stmt->rowCount();
        
        if ($NumOfRows == 1) {
            $_SESSION['id'] = $result[0]['id'];
            $_SESSION['pseudo'] = $result[0]['pseudo'];
			$_SESSION['mail'] = $result[0]['email'];
			
            $stmt = $dbh->prepare("UPDATE user u SET u.gcm_regid='$regId', u.online='false' WHERE pseudo = '$pseudo'");
            $dbh = null;
			
            return $result[0]['id'];
        } else {
            $dbh = null;
            return DENIED;
        }
		
		
        
    } catch (PDOException $e) {
        echo "Erreur !: " . $e->getMessage() . "<br/>";
        die();
    }
}

function register($pseudo, $password, $gcm_regid) {
        
    try {
        
        if(userExistPseudo($pseudo) == SUCCESS){
            return DENIED;
        }
        $result = array();
        $dbh = new PDO('mysql:host='.DB_HOST.';dbname='.DB_DATABASE, DB_USER, DB_PASSWORD);
        //$stmt = $dbh->prepare("INSERT INTO user (name, email, password, gcm_regid, created_at) VALUES ('$pseudo', '$email', '$password', '$gcm_regid', NOW())");
		$stmt = $dbh->prepare("INSERT INTO user (pseudo, password, gcm_regid) VALUES ('$pseudo', '$password', '$gcm_regid')");
		$stmt->execute();
        $dbh = null;
        
        
        $check = getUserByPseudo($pseudo);
        if (count($check) == 1) { 
            return $check[0]['id'];
        } else {
            return ERROR;
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
        }

        return $result;
        
    } catch (PDOException $e) {
        echo "Erreur !: " . $e->getMessage() . "<br/>";
        die();
    } 
}

/**
 * Get user by pseudo
 */
function getUserByPseudo($pseudo) {
    try {
        $result = array();
        $dbh = new PDO('mysql:host='.DB_HOST.';dbname='.DB_DATABASE, DB_USER, DB_PASSWORD);
        $stmt = $dbh->prepare("SELECT * FROM user WHERE pseudo = '$pseudo' LIMIT 1");
        $stmt->execute();
        while ($row = $stmt->fetch()) {
            $result[] = $row;
        }

        return $result;
        
    } catch (PDOException $e) {
        echo "Erreur !: " . $e->getMessage() . "<br/>";
        die();
    } 
}

/**
 * search user
 */
function searchUser($w, $page) {
    try {
        $result = array();
        $dbh = new PDO('mysql:host='.DB_HOST.';dbname='.DB_DATABASE, DB_USER, DB_PASSWORD);
        $stmt = $dbh->prepare("SELECT * FROM user WHERE email LIKE '%$w%' OR pseudo LIKE '%$w%' OR gcm_regid LIKE '%$w%' ");
        $stmt->execute();
        while ($row = $stmt->fetch()) {
            $result[] = $row;
        }

        return $result;
        
    } catch (PDOException $e) {
        echo "Erreur !: " . $e->getMessage() . "<br/>";
        die();
    } 
}



// Getting all registered users
function getAllUsers($page) {
        
    try {
        $result = array();
        $dbh = new PDO('mysql:host='.DB_HOST.';dbname='.DB_DATABASE, DB_USER, DB_PASSWORD);
        $stmt = $dbh->prepare("SELECT email, pseudo, gcm_regid FROM user");
        $stmt->execute();
        while ($row = $stmt->fetch()) {
            $result[] = $row;
        }
        if(count($result) == 0){
            return null;
        }
        //echo (json_encode($result));
        return $result;
        
    } catch (PDOException $e) {
        echo "Erreur !: " . $e->getMessage() . "<br/>";
        die();
    } 
}

// Getting all registered users
function getAllUsersGCMID() {
        
    try {
        $result = array();
        $dbh = new PDO('mysql:host='.DB_HOST.';dbname='.DB_DATABASE, DB_USER, DB_PASSWORD);
        $stmt = $dbh->prepare("SELECT gcm_regid FROM user");
        $stmt->execute();
        while ($row = $stmt->fetch()) {
            $result[] = $row[0];
        }
        
        if(count($result) == 0){
            return null;
        }
        //echo (json_encode($result));
        return $result;
        
    } catch (PDOException $e) {
        echo "Erreur !: " . $e->getMessage() . "<br/>";
        die();
    } 
}



function userExistMail($email) {
     
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
            return SUCCESS;
        } else {
            return ERROR;
        }
        
    } catch (PDOException $e) {
        echo "Erreur !: " . $e->getMessage() . "<br/>";
        die();
    }
}

function userExistPseudo($pseudo) {
     
    try {
        $result = array();
        $dbh = new PDO('mysql:host='.DB_HOST.';dbname='.DB_DATABASE, DB_USER, DB_PASSWORD);
        $stmt = $dbh->prepare("SELECT pseudo FROM user WHERE pseudo = '$pseudo' LIMIT 1");
        $stmt->execute();
        while ($row = $stmt->fetch()) {
            $result[] = $row;
        }

        $NumOfRows = count($result);
        if ($NumOfRows > 0) {
            return SUCCESS;
        } else {
            return DENIED;
        }
        
    } catch (PDOException $e) {
        echo "Erreur !: " . $e->getMessage() . "<br/>";
        die();
    }
}

function userExistRegId($regID) {
     
    try {
        $result = array();
        $dbh = new PDO('mysql:host='.DB_HOST.';dbname='.DB_DATABASE, DB_USER, DB_PASSWORD);
        $stmt = $dbh->prepare("SELECT gcm_regid FROM user WHERE gcm_regid = '$regID' LIMIT 1");
        $stmt->execute();
        while ($row = $stmt->fetch()) {
            $result[] = $row;
        }

        $NumOfRows = count($result);
        if ($NumOfRows > 0) {
            return SUCCESS;
        } else {
            return ERROR;
        }
        
    } catch (PDOException $e) {
        echo "Erreur !: " . $e->getMessage() . "<br/>";
        die();
    }
}

function deleteUserByMail($email){
    
    try {
         
        if(userExistMail($email)){
            $result = array();
            $dbh = new PDO('mysql:host='.DB_HOST.';dbname='.DB_DATABASE, DB_USER, DB_PASSWORD);
            $stmt = $dbh->prepare("DELETE FROM user WHERE email = '$email' LIMIT 1");
            $stmt->execute();
            
            if (!userExistMail($email)) { 
                return SUCCESS;
            } else {
                return ERROR;
            }
        }
        return DENIED;
        
    } catch (PDOException $e) {
        echo "Erreur !: " . $e->getMessage() . "<br/>";
        die();
    }
    
}

function deleteUserByRegId($regID){
    
    try {
         
        if(userExistRegId($regID)){
            $result = array();
            $dbh = new PDO('mysql:host='.DB_HOST.';dbname='.DB_DATABASE, DB_USER, DB_PASSWORD);
            $stmt = $dbh->prepare("DELETE FROM user WHERE gcm_regid = '$regID' LIMIT 1");
            $stmt->execute();
            
            if (!userExistRegId($regID)) { 
                return SUCCESS;
            } else {
                return ERROR;
            }
        }
        return DENIED;
        
    } catch (PDOException $e) {
        echo "Erreur !: " . $e->getMessage() . "<br/>";
        die();
    }
    
}


function addGroup(){
    $name  = $_POST["name"];
    $type = $_POST["type"];
    $creator = $_POST["user_id"];
    
    try {
         
        if(!groupExist($name, $creator)){
            $result = array();
            $dbh = new PDO('mysql:host='.DB_HOST.';dbname='.DB_DATABASE, DB_USER, DB_PASSWORD);
            $stmt = $dbh->prepare("INSERT INTO group (name, type, creator) VALUES ('$name', '$type', '$creator')");
            $stmt->execute();
            
            if (groupExist($name, $creator)) { 
                return SUCCESS;
            } else {
                return ERROR;
            }
        }
        echo DENIED;
        
    } catch (PDOException $e) {
        echo "Erreur !: " . $e->getMessage() . "<br/>";
        die();
    }
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
            return SUCCESS;
        } else {
            return DENIED;
        }
 
    } catch (PDOException $e) {
        echo "Erreur !: " . $e->getMessage() . "<br/>";
        die();
    }
}

function addToGroup($user_id, $group_id){ 
    
     try {
         
        if(!isInGroup($user_id, $group_id)){
            $result = array();
            $dbh = new PDO('mysql:host='.DB_HOST.';dbname='.DB_DATABASE, DB_USER, DB_PASSWORD);
            $stmt = $dbh->prepare("INSERT INTO user_in_group (user, group) VALUES ('$user_id', '$group_id')");
            $stmt->execute();
            $dbh = null;
            
            if (isInGroup($user_id, $group_id)) { 
                return SUCCESS;
            } else {
                return ERROR;
            }
        }
        return DENIED;
        
    } catch (PDOException $e) {
        echo "Erreur !: " . $e->getMessage() . "<br/>";
        die();
    }
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
            return SUCCESS;
        } else {
            return DENIED;
        }
        
    } catch (PDOException $e) {
        echo "Erreur !: " . $e->getMessage() . "<br/>";
        die();
    }
}

function removeFromGroup(){
    $user_id  = $_POST["user_id"];
    $group_id = $_POST["group_id"];
    
     try {
         
        if(isInGroup($user_id, $group_id)){
            $result = array();
            $dbh = new PDO('mysql:host='.DB_HOST.';dbname='.DB_DATABASE, DB_USER, DB_PASSWORD);
            $stmt = $dbh->prepare("DELETE FROM user_in_group WHERE user = '$user_id' AND group = '$group_id'");
            $stmt->execute();
            $dbh = null;
            
            if (!isInGroup($user_id, $group_id)) { 
                return SUCCESS;
            } else {
                return ERROR;
            }
        }
        echo DENIED;
        
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
    
     try {
         
        $result = array();
        $dbh = new PDO('mysql:host='.DB_HOST.';dbname='.DB_DATABASE, DB_USER, DB_PASSWORD);
        $stmt = $dbh->prepare("INSERT INTO signalement (content, note, date, diffusion, type) VALUES ('$content', '$note', '$date', '$diffusion', '$type')");
        $stmt->execute();
        $dbh = null;

        /*if (!isInGroup($user_id, $group_id)) { 
            return SUCCESS;
        } else {
            return ERROR;
        }*/
         
        echo SUCCESS;
        
    } catch (PDOException $e) {
        echo "Erreur !: " . $e->getMessage() . "<br/>";
        die();
    }
}

function deleteSignalement(){
    $sign_id  = $_POST["sign_id"];

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
         
        echo SUCCESS;
        
    } catch (PDOException $e) {
        echo "Erreur !: " . $e->getMessage() . "<br/>";
        die();
    }
}

?>