<?php

class GoogleAPIProvider {

    // Project's Google API Console JSON file (Must be renamed to 'project-credential')  
    const CREDENTIAL_FILE = __DIR__ . '/project-credential.json';
    const GET_TOKEN_URL = 'https://www.googleapis.com/oauth2/v4/token';
    
    // Error messages
    const RECEIVE_ACCESS_TOKEN_ERROR = 'Error: Failed to receive access token';

    private static $instance;
    
    // Project information
    private $clientId;
    private $clientSecret;
    private $clientRedirectURL;
    
    // API's URL
    private $googlePlusOnlineURL;
    private $googlePlusOfflineURL;
    private $googleCalendarURL;

    private function __construct() {
        $data = json_decode(file_get_contents(self::CREDENTIAL_FILE));
        $this->clientId = $data->web->client_id;
        $this->clientSecret = $data->web->client_secret;
        $this->clientRedirectURL = $data->web->redirect_uris[0];

        $this->googleCalendarURL = $login_url = 'https://accounts.google.com/o/oauth2/auth?scope='
                . urlencode('https://www.googleapis.com/auth/calendar')
                . '&redirect_uri=' . urlencode($this->clientRedirectURL)
                . '&response_type=code&client_id=' . $this->clientId
                . '&access_type=offline';

        $this->googlePlusOnlineURL = 'https://accounts.google.com/o/oauth2/v2/auth?scope='
                . urlencode('https://www.googleapis.com/auth/userinfo.profile ' .
                        'https://www.googleapis.com/auth/userinfo.email ' .
                        'https://www.googleapis.com/auth/plus.me')
                . '&redirect_uri=' . urlencode($this->clientRedirectURL)
                . '&response_type=code&client_id=' . $this->clientId
                . '&access_type=online';

        $this->googlePlusOfflineURL = 'https://accounts.google.com/o/oauth2/v2/auth?scope='
                . urlencode('https://www.googleapis.com/auth/userinfo.profile ' .
                        'https://www.googleapis.com/auth/userinfo.email ' .
                        'https://www.googleapis.com/auth/plus.me')
                . '&redirect_uri=' . urlencode($this->clientRedirectURL)
                . '&response_type=code&client_id=' . $this->clientId
                . '&access_type=offline';
    }

    public static function getInstance() {
        if (!isset(self::$instance)) {
            self::$instance = new GoogleAPIProvider();
        }

        return self::$instance;
    }

    function getClientId() {
        return $this->clientId;
    }

    function getClientSecret() {
        return $this->clientSecret;
    }

    function getClientRedirectURL() {
        return $this->clientRedirectURL;
    }

    function getGooglePlusOnlineURL() {
        return $this->googlePlusOnlineURL;
    }

    function getGooglePlusOfflineURL() {
        return $this->googlePlusOfflineURL;
    }

    function getGoogleCalendarURL() {
        return $this->googleCalendarURL;
    }

    public function getAccessTokenByAuthorizationCode($code) {
        
        $curlPostFields = 'client_id=' . $this->clientId . '&redirect_uri=' . $this->clientRedirectURL
                . '&client_secret=' . $this->clientSecret . '&code=' . $code
                . '&grant_type=authorization_code';

        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, self::GET_TOKEN_URL);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, TRUE);
        curl_setopt($ch, CURLOPT_POST, TRUE);
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, FALSE);
        curl_setopt($ch, CURLOPT_POSTFIELDS, $curlPostFields);

        $data = json_decode(curl_exec($ch), TRUE);
        $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
        curl_close($ch);

        if ($httpCode != 200) {
            
            throw new Exception(self::RECEIVE_ACCESS_TOKEN_ERROR);
        }

        return $data;
    }

    public function getAccessTokenByRefreshToken($refreshToken) {

        $curlPostFields = 'client_id=' . $this->clientId . '&client_secret='
                . $this->clientSecret . '&refresh_token=' . $refreshToken
                . '&grant_type=refresh_token';
        
        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, self::GET_TOKEN_URL);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, TRUE);
        curl_setopt($ch, CURLOPT_POST, TRUE);
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, FALSE);
        curl_setopt($ch, CURLOPT_POSTFIELDS, $curlPostFields);
        
        $data = json_decode(curl_exec($ch), TRUE);
        $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
        curl_close($ch);
        
        if ($httpCode != 200) {
            
            throw new Exception(self::RECEIVE_ACCESS_TOKEN_ERROR);
        }

        return $data;
    }
    
    public function getNewAccessTokenIfExpired($expiry, $refreshToken) {

        $data = NULL;
        
        if ($expiry == NULL || time() > $expiry) {

            $data = $this->getAccessTokenByRefreshToken($refreshToken);
        }

        return $data;
    }
}
