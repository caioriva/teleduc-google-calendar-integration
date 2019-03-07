<?php

class GoogleCalendarAPI {

    const GET_TIMEZONE_ERROR = "Error : Failed to get user's timezone";
    const CREATE_EVENT_ERROR = 'Error : Failed to create event';
    const GET_CALENDARS_LIST_ERROR = 'Error : Failed to get calendars list';
    const DELETE_EVENT_ERROR = 'Error : Failed to delete event';
    const UPDATE_EVENT_ERROR = 'Error : Failed to update event';

    public function __construct() {
        
    }

    public function getUserCalendarTimezone($accessToken) {

        $timezoneURL = 'https://www.googleapis.com/calendar/v3/users/me/settings/timezone';

        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $timezoneURL);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, TRUE);
        curl_setopt($ch, CURLOPT_HTTPHEADER, array('Authorization: Bearer ' . $accessToken));
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, FALSE);
        $data = json_decode(curl_exec($ch), TRUE);
        $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);

        if ($httpCode != 200) {

            throw new Exception(self::GET_TIMEZONE_ERROR);
        }

        return $data['value'];
    }

    public function createCalendarEvent($calendarId, $summary, $isDateEvent, $eventTime, $eventTimezone, $accessToken) {

        $eventsURL = 'https://www.googleapis.com/calendar/v3/calendars/' . $calendarId . '/events';

        $curlPost = array('summary' => $summary);

        if ($isDateEvent == TRUE) {

            $curlPost['start'] = array('date' => $eventTime['startDate']);
            $curlPost['end'] = array('date' => $eventTime['endDate']);
        } else {
            $curlPost['start'] = array('dateTime' => $eventTime['startTime'], 'timeZone' => $eventTimezone);
            $curlPost['end'] = array('dateTime' => $eventTime['endTime'], 'timeZone' => $eventTimezone);
        }

        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $eventsURL);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, TRUE);
        curl_setopt($ch, CURLOPT_POST, TRUE);
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, FALSE);
        curl_setopt($ch, CURLOPT_HTTPHEADER, array('Authorization: Bearer ' . $accessToken, 'Content-Type: application/json'));
        curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($curlPost));
        $data = json_decode(curl_exec($ch), TRUE);
        $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);

        if ($httpCode != 200) {

            throw new Exception(self::CREATE_EVENT_ERROR);
        }

        return $data['id'];
    }

    public function getCalendarsList($accessToken) {

        $urlParameters = array();
        $urlParameters['fields'] = 'items(id,summary,timeZone)';
        $urlParameters['minAccessRole'] = 'owner';

        $calendarsURL = 'https://www.googleapis.com/calendar/v3/users/me/calendarList?' . http_build_query($urlParameters);

        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $calendarsURL);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, TRUE);
        curl_setopt($ch, CURLOPT_HTTPHEADER, array('Authorization: Bearer ' . $accessToken));
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, FALSE);
        $data = json_decode(curl_exec($ch), TRUE);
        $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);

        if ($httpCode != 200) {

            throw new Exception(self::GET_CALENDARS_LIST_ERROR);
        }

        return $data['items'];
    }

    public function deleteCalendarEvent($eventId, $calendarId, $accessToken) {
        
        $eventsURL = 'https://www.googleapis.com/calendar/v3/calendars/' . $calendarId . '/events/' . $eventId;

        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $eventsURL);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, TRUE);
        curl_setopt($ch, CURLOPT_CUSTOMREQUEST, 'DELETE');
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, FALSE);
        curl_setopt($ch, CURLOPT_HTTPHEADER, array('Authorization: Bearer ' . $accessToken, 'Content-Type: application/json'));
        $data = json_decode(curl_exec($ch), true);
        $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
        
        if ($httpCode != 204) {
            
            throw new Exception(self::DELETE_EVENT_ERROR);
        }
    }
    
    public function updateCalendarEvent($eventId, $calendarId, $summary, $isDateEvent, $eventTime, $eventTimezone, $accessToken) {
		
                $eventsURL = 'https://www.googleapis.com/calendar/v3/calendars/' . $calendarId . '/events/' . $eventId;

		$curlPost = array('summary' => $summary);
                
		if($isDateEvent == TRUE) {
			$curlPost['start'] = array('date' => $eventTime['startDate']);
			$curlPost['end'] = array('date' => $eventTime['endDate']);
		}
		else {
			$curlPost['start'] = array('dateTime' => $eventTime['startTime'], 'timeZone' => $eventTimezone);
			$curlPost['end'] = array('dateTime' => $eventTime['endTime'], 'timeZone' => $eventTimezone);
		}
                
		$ch = curl_init();		
		curl_setopt($ch, CURLOPT_URL, $eventsURL);		
		curl_setopt($ch, CURLOPT_RETURNTRANSFER, TRUE);		
		curl_setopt($ch, CURLOPT_CUSTOMREQUEST, 'PUT');		
		curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, FALSE);
		curl_setopt($ch, CURLOPT_HTTPHEADER, array('Authorization: Bearer '. $accessToken, 'Content-Type: application/json'));	
		curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($curlPost));	
		$data = json_decode(curl_exec($ch), true);
		$httpCode = curl_getinfo($ch,CURLINFO_HTTP_CODE);
                
		if($httpCode != 200) {
                    
			throw new Exception(self::UPDATE_EVENT_ERROR);
                }
	}
}
