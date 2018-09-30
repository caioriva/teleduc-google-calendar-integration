<?php
/*
<!--
-------------------------------------------------------------------------------

    Arquivo :  pagina_inicial/alterar_senha.php

    TelEduc - Ambiente de Ensino-Aprendizagem a Dist�ncia
    Copyright (C) 2001  NIED - Unicamp

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License version 2 as
    published by the Free Software Foundation.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

    You could contact us through the following addresses:

    Nied - N�cleo de Inform�tica Aplicada � Educa��o
    Unicamp - Universidade Estadual de Campinas
    Cidade Universit�ria "Zeferino Vaz"
    Bloco V da Reitoria - 2o. Piso
    CEP:13083-970 Campinas - SP - Brasil

    http://www.nied.unicamp.br
    nied@unicamp.br

------------------------------------------------------------------------------
-->
*/
/*==========================================================
  ARQUIVO : pagina_inicial/autorizar_google_calendar.php
  ========================================================== */

$bibliotecas="../cursos/aplic/bibliotecas/";
include($bibliotecas."geral.inc");
include("inicial.inc");

require_once("../cursos/aplic/xajax_0.5/xajax_core/xajax.inc.php");

//Estancia o objeto XAJAX
$objAjax = new xajax();
$objAjax->configure("characterEncoding", 'ISO-8859-1');
$objAjax->setFlag("decodeUTF8Input",true);
$objAjax->configure('javascript URI', "../cursos/aplic/xajax_0.5");
$objAjax->configure('errorHandler', true);
//Registre os nomes das fun?es em PHP que voc?quer chamar atrav? do xajax
$objAjax->register(XAJAX_FUNCTION,"AtualizaSenhaUsuarioDinamic");

//Manda o xajax executar os pedidos acima.
$objAjax->processRequest();

$pag_atual = "alterar_senha.php";
include("../topo_tela_inicial.php");

/* Direciona para pagina de login caso nao esteja usuario conectado */
if (empty($_SESSION['login_usuario_s']))
{
    /* Obt� a raiz_www */
    //$sock = Conectar("");
    $query = "select diretorio from Diretorio where item = 'raiz_www'";
    $res = Enviar($sock,$query);
    $linha = RetornaLinha($res);
    $raiz_www = $linha[0];

    $caminho = $raiz_www."/pagina_inicial/autenticacao";

    header("Location: {$caminho}/autenticacao_cadastro.php");
    Desconectar($sock);
    exit;
}

$objAjax->printJavascript();

include("../menu_principal_tela_inicial.php");

echo("        <td width=\"100%\" valign=\"top\" id=\"conteudo\">\n");
/* Autorizar Google Calendar */
echo("          <h4>".RetornaFraseDaLista($lista_frases_configurar,1)." - ".RetornaFraseDaLista($lista_frases_configurar,534)."</h4>\n");
/* Fonte */
echo("          <div id=\"mudarFonte\">\n");
echo("            <a onclick=\"mudafonte(2)\" href=\"#\"><img width=\"17\" height=\"15\" border=\"0\" align=\"right\" alt=\"Letra tamanho 3\" src=\"../cursos/aplic/imgs/btFont1.gif\"/></a>\n");
echo("            <a onclick=\"mudafonte(1)\" href=\"#\"><img width=\"15\" height=\"15\" border=\"0\" align=\"right\" alt=\"Letra tamanho 2\" src=\"../cursos/aplic/imgs/btFont2.gif\"/></a>\n");
echo("            <a onclick=\"mudafonte(0)\" href=\"#\"><img width=\"14\" height=\"15\" border=\"0\" align=\"right\" alt=\"Letra tamanho 1\" src=\"../cursos/aplic/imgs/btFont3.gif\"/></a>\n");
echo("          </div>\n");
/* Voltar */
echo("                  <ul class=\"btsNav\"><li><span onclick=\"javascript:history.back(-1);\">&nbsp;&lt;&nbsp;".RetornaFraseDaLista($lista_frases_geral,509)."&nbsp;</span></li></ul>\n");
echo("          <table cellpadding=\"0\" cellspacing=\"0\"  id=\"tabelaExterna\" class=\"tabExterna\">\n");
echo("            <tr>");
echo("              <td valign=\"top\">\n");
echo("                <ul class=\"btAuxTabs\">\n");
/* Alterar dados pessoais */
echo("                  <li><a href=\"dados.php\" id=\"alterar_dados\">".RetornaFraseDaLista($lista_frases_configurar, 27)."</a></li>\n");
/* Alterar Login */
echo("                  <li><a href=\"alterar_login.php\" id=\"alterar_login\">".RetornaFraseDaLista($lista_frases_configurar, 65)."</a></li>\n");
/* Alterar Senha */
echo("                  <li><a href=\"alterar_senha.php\" id=\"alterar_senha\">".RetornaFraseDaLista($lista_frases_configurar, 2)."</a></li>\n");
/* Alterar Idioma */
echo("                  <li><a href=\"selecionar_lingua.php\" id=\"selecionar_idioma\">".RetornaFraseDaLista($lista_frases_configurar, 3)."</a></li>\n");
/* Autorizar Google Calendar */
echo("                  <li><a href=\"autorizar_google_calendar.php\" id=\"autorizar_google_calendar\">".RetornaFraseDaLista($lista_frases_configurar,534)."</a></li>\n");
echo("                </ul>\n");
echo("              </td>\n");
echo("            </tr>\n");
echo("            <tr>\n");
echo("              <td>\n");
echo("                <form name=\"form_google_calendar\" id=\"form_google_calendar\" action=\"\" method=\"post\" onsubmit=\"\">\n");
echo("                <table cellspacing=\"0\" class=\"tabInterna\">\n");
echo("                    <tr class=\"head\">\n");
echo("                      <td width=\"43%\">".RetornaFraseDaLista($lista_frases_configurar,533)."</td>\n");
echo("                      <td width=\"14%\">&nbsp;</td>\n");
echo("                    </tr>\n");
echo("                    <tr>\n");
/* Google Calendar Autorizado */
echo("                      <td valign='top' style='border:none; text-align:center;'>\n");
echo("                        <input type='checkbox' class='input' name='google_calendar'>\n");
echo("                      </td>\n");
echo("                      <td align=\"center\" >\n");
echo("                        <ul>\n");
echo("                          <li><input type='submit' class='input' value='".RetornaFraseDaLista($lista_frases_configurar,535)."' id='registar_alts' /></li>\n");
echo("                        </ul>\n");
echo("                      </td>\n");
echo("                    </tr>\n");
echo("                  </table>\n");
echo("                </form>\n");
echo("              </td>\n");
echo("            </tr>\n");
echo("          </table>\n");
echo("        </td>\n");
echo("      </tr>\n");
include("../rodape_tela_inicial.php");
echo("  </body>\n");
echo("</html>\n");

Desconectar($sock);

?>