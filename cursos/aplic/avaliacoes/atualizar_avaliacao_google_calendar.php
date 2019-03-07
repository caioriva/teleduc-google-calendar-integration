<?php
$bibliotecas = "../bibliotecas/";
$googleCalendar  = '../../../googlecalendar/googlecalendarapi/';

include($bibliotecas."geral.inc");
include('avaliacoes.inc');
include($googleCalendar.'settings/GoogleAPIProvider.php');
include($googleCalendar.'calendar/GoogleCalendarAPI.php');

if(isset($_GET['cod_ferramenta']) && isset($_GET['cod_atividade']) && isset($_GET['cod_avaliacao']) && isset($_GET['cod_curso']) && isset($_GET['cod_usuario']) && isset($_GET['id_agenda']) && isset($_GET['id_evento'])) {
    $sock = Conectar($_GET['cod_curso']);
    $titulo = RetornaTituloAvaliacao($sock, $_GET['cod_ferramenta'], $_GET['cod_atividade']);
    Desconectar($sock);

    atualizarAvaliacaoGoogleCalendar($_GET['cod_avaliacao'], $_GET['cod_curso'], $_GET['cod_usuario'], $titulo, $_GET['id_agenda'], $_GET['id_evento']);
}

header("location: ../../../pagina_inicial/exibe_cursos.php");