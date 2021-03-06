<?php
/*
<!--
-------------------------------------------------------------------------------

    Arquivo : cursos/aplic/index2.php

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
  ARQUIVO : cursos/aplic/index2.php
  ========================================================== */

  $bibliotecas="bibliotecas/";
  include($bibliotecas."geral.inc");

  $cod_usuario_global=VerificaAutenticacao($cod_curso);

  $sock=Conectar($cod_curso);

  $cod_usuario = RetornaCodigoUsuarioCurso($sock, $cod_usuario_global, $cod_curso);

  VerificaAcessoAoCurso($sock,$cod_curso,$cod_usuario);
 
  if ($cod_curso_s!=$cod_curso)
  {
    $cod_curso_s=$cod_curso;
    MarcaAcesso($sock,$cod_usuario,"");
  }

  if ($ativar_visao_aluno=="sim")
  {
    $_SESSION['visao_aluno_s']=true;
  }
  if ($desativar_visao_aluno=="sim")
  {
    $_SESSION['visao_aluno_s']=false;
  }

  if (CompletarDadosCurso($sock,$cod_curso) && ECoordenador($sock,$cod_curso,$cod_usuario))
  {
    Desconectar($sock);
    echo("<script language=javascript>\n");
    echo("  document.location='administracao/alterar_dados_curso.php?cod_curso=".$cod_curso."';\n");
    echo("</script>\n");
  }
  else
  {
    Desconectar($sock);
    if((!((isset($_GET['prosseguir'])) && ($_GET['prosseguir'] == "true"))) && ((isset($_SERVER['HTTP_REFERER'])) && ($_SERVER['HTTP_REFERER'] != ""))){
      header("Location:".$_SERVER['HTTP_REFERER']);

    }else{
      header("Location:agenda/agenda.php?cod_curso=".$cod_curso);
    }
  }
?>
