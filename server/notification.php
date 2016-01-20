<?php

class Message 
{   
    private $msg;
    
    public function __construct()
	{
		$this->msg = $msg = array
        (
            'message' => null,
            'type' => null
        );

	}
    
    public function setContent($message){
        $this->msg['message'] = $message;
    }
    public function setType($type){
        $this->msg['type'] = $type;
    }
    
    public function getTab(){
        return $this->msg;
    }
    
    public function isValid(){
        if( $this->msg['message'] != null
           && $this->msg['type'] != null){
            
            return true;
        }
        return false;
    }
}

class Notification
{
	private $msg;
	private $fields;
	private $headers;
    
	public function __construct()
	{
		$this->msg = null;
		$this->fields=$fields = array
        (
            'registration_ids' 	=> '',
            'data'			=> ''
        );
		$this->headers = array
        (
            'Authorization: key=' . API_ACCESS_KEY,
            'Content-Type: application/json'
        );
	}
    
    public function setMessage($message){
        $this->msg = $message;
        $this->fields['data'] = $this->msg->getTab();
    }
    
     public function setRegistrationId($registrationIds){
        $this->fields['registration_ids'] = $registrationIds;
    }

    public function send(){
        if($this->msg != null && $this->msg->isValid()){
                
            $ch = curl_init();
            curl_setopt( $ch,CURLOPT_URL, 'https://android.googleapis.com/gcm/send' );
            curl_setopt( $ch,CURLOPT_POST, true );
            curl_setopt( $ch,CURLOPT_HTTPHEADER, $this->headers );
            curl_setopt( $ch,CURLOPT_RETURNTRANSFER, true );
            curl_setopt( $ch,CURLOPT_SSL_VERIFYPEER, false );
            curl_setopt( $ch,CURLOPT_POSTFIELDS, json_encode($this->fields) );
            $result = curl_exec($ch );
            curl_close( $ch );
            //echo $result;
            
            return 'success : notification was sent';
        
        } else {
            return 'error : non valid message';
        }
    }

}
?>