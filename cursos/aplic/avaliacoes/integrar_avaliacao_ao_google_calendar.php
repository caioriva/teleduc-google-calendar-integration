<?php
$bibliotecas = "../bibliotecas/";
$googleCalendar  = '../../../googlecalendar/googlecalendarapi/';

include($bibliotecas."geral.inc");
include('avaliacoes.inc');
include($googleCalendar.'settings/GoogleAPIProvider.php');
include($googleCalendar.'calendar/GoogleCalendarAPI.php');

if(isset($_GET['cod_avaliacao']) && isset($_GET['cod_curso']) && isset($_GET['cod_usuario']) && isset($_GET['titulo'])) {
    integrarAvaliacaoGoogleCalendar($_GET['cod_avaliacao'], $_GET['cod_curso'], $_GET['cod_usuario'], $_GET['titulo']);
}

header("location: ../../../pagina_inicial/exibe_cursos.php");

