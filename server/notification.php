<?php

class Message 
{   
    private $msg;
    
    public function __construct()
	{
		$this->msg = $msg = array
        (
            'message' 	=> null,
            'title'		=> null,
            'subtitle'	=> null,
            'tickerText'	=> null,
            'vibrate'	=> null,
            'sound'		=> null,
            'largeIcon'	=> null,
            'smallIcon'	=> null
        );

	}
    
    public function setContent($message){
        $this->msg['message'] = $message;
    }
    public function setTitle($title){
        $this->msg['title'] = $title;
    }
    public function setSubtitle($subtitle){
        $this->msg['subtitle'] = $subtitle;
    }
    public function setTickerText($tickerText){
        $this->msg['tickerText'] = $tickerText;
    }
    public function setVibrate($vibrate){
        $this->msg['vibrate'] = $vibrate;
    }
    public function setSound($sound){
        $this->msg['sound'] = $sound;
    }
    public function setLargeIcon($largeIcon){
        $this->msg['largeIcon'] = $largeIcon;
    }
    public function setSmallIcon($smallIcon){
        $this->msg['smallIcon'] = $smallIcon;
    }
    
    public function getTab(){
        return $this->msg;
    }
    
    public function isValid(){
        if( $this->msg['message'] != null
           && $this->msg['title'] != null
           && $this->msg['subtitle'] != null
           && $this->msg['tickerText'] != null
           && $this->msg['vibrate'] != null
           && $this->msg['sound'] != null
           && $this->msg['largeIcon'] != null
           && $this->msg['smallIcon'] != null){
            
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
                
            /*$ch = curl_init();
            curl_setopt( $ch,CURLOPT_URL, 'https://android.googleapis.com/gcm/send' );
            curl_setopt( $ch,CURLOPT_POST, true );
            curl_setopt( $ch,CURLOPT_HTTPHEADER, $headers );
            curl_setopt( $ch,CURLOPT_RETURNTRANSFER, true );
            curl_setopt( $ch,CURLOPT_SSL_VERIFYPEER, false );
            curl_setopt( $ch,CURLOPT_POSTFIELDS, json_encode( $fields ) );
            $result = curl_exec($ch );
            curl_close( $ch );
            echo $result;*/
            
            echo 'success : notification was sent';
        
        } else {
            echo 'error : non valid message';
        }
    }

}
?>