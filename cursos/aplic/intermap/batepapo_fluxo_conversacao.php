<?php
/*
<!--
-------------------------------------------------------------------------------

    Arquivo : cursos/aplic/intermap/batepapo_fluxo_conversacao.php

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
  ARQUIVO : cursos/aplic/intermap/batepapo_fluxo_conversacao.php
  ========================================================== */

  include("batepapo.inc");

  $linha_curso=RetornaDadosCurso($sock,$cod_curso);

  echo("<form name=\"mapa\" action=\"batepapo_fluxo_conversacao2.php\" target=\"Intermap\" method=\"get\">\n");
  //echo(RetornaSessionIDInput()."\n");
  echo("<input type=\"hidden\" name=\"cod_curso\" value=\"".$cod_curso."\" />\n");
  echo("<input type=\"hidden\" name=\"todos\"     value=\"sim\" />\n");

  echo("<table id=\"tabelaInterna\" cellpadding=\"0\" cellspacing=\"0\" class=\"tabInterna\">\n");
  echo("  <tr class=\"head\">\n");
  // 64 - Sess�o:
  echo("    <td>".RetornaFraseDaLista($lista_frases,64)."</td>\n");
  echo("  </tr>\n");

  $sessoes=RetornaListaSessoes($sock);
  if (count($sessoes)>0)
  {
    echo("  <tr>\n");
    echo("    <td>\n");
    echo("      <select class=\"input\" name=\"cod_sessao\">\n");
    foreach ($sessoes as $cod => $linha)
    {
       echo("        <option value=\"".$linha['cod_sessao']."\">");
       echo(UnixTime2Data($linha['DataInicio']));
       // 16 - das
       echo(" - ".RetornaFraseDaLista($lista_frases,16)." ");
       echo(UnixTime2Hora($linha['DataInicio']));
       // 8 - as
       echo(" ".RetornaFraseDaLista($lista_frases,8)." ");
       echo(UnixTime2Hora($linha['DataFim']));
       echo(" - ");
       echo($linha['Assunto']);
       echo("        </option>\n");
    }
    echo("      </select>\n");
    echo("    </td>\n");
    echo("  </tr>\n");
  }
  else
  {
    echo("  <tr>\n");
    // 52 - Nenhuma sess�o foi realizada
    echo("    <td>".RetornaFraseDaLista($lista_frases,52)."</td>\n");
    echo("  </tr>\n");
  }

  // Fim Tabela Interna
  echo("</table>\n");

  if(count($sessoes)>0)
  {
    echo("<div align=\"right\">\n");
    // 53 - Ok
    echo("  <input class=\"input\" type=\"submit\" value='".RetornaFraseDaLista($lista_frases,53)."' />\n");
    echo("</div>\n");
  }

  echo("</form>\n");
?>
