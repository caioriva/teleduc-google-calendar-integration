<?php
$bibliotecas = '../cursos/aplic/bibliotecas/';
$googleCalendar  = '../googlecalendar/googlecalendarapi/';
include($bibliotecas.'geral.inc');
include ('inicial.inc');
include($googleCalendar.'settings/GoogleAPIProvider.php');

session_start();
if ($_SERVER['REQUEST_METHOD'] == 'GET' && isset($_GET['code'])) {
    autorizarGoogleCalendarPorCodigo($_GET['code']);
} else {
    header('location: autorizar_google_calendar.php');
}
session_write_close();
?>

