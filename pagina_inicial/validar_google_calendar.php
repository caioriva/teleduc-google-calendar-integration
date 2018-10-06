<?php
$bibliotecas = '../cursos/aplic/bibliotecas/';
$googleCalendar  = '../googlecalendar/googlecalendarapi/';
include($bibliotecas.'geral.inc');
include ('inicial.inc');
include($googleCalendar.'settings/GoogleAPIProvider.php');

session_start();
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    if (isset($_POST['google_calendar'])) {
        autorizarGoogleCalendar();
    } else {
        desautorizarGoogleCalendar();
    }
}
session_write_close();
?>