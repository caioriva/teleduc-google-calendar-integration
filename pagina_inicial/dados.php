<?php 
/* 
<!--  
-------------------------------------------------------------------------------

    Arquivo : pagina_inicial/dados.php

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
  ARQUIVO : pagina_inicial/dados.php
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
  $objAjax->register(XAJAX_FUNCTION,"AtualizaDadosUsuarioDinamic");

  //Manda o xajax executar os pedidos acima.
  $objAjax->processRequest();

  $pag_atual = "dados.php";
  include("../topo_tela_inicial.php");

  /* Caso o usuário naum esteja logado, direciona para páigna de login */
  if (empty($_SESSION['login_usuario_s']))
  {
    /* Obt� a raiz_www */
    //$sock = Conectar("");
    $query = "select diretorio from Diretorio where item = 'raiz_www'";
    $res = Enviar($sock,$query);
    $linha = RetornaLinha($res);
    $raiz_www = $linha[0];

    $caminho = $raiz_www."/pagina_inicial";

    header("Location: {$caminho}/autenticacao_cadastro.php");
    Desconectar($sock);
    exit;
  }

   // instanciar o objeto, passa a lista de frases por parametro
  $feedbackObject =  new FeedbackObject($lista_frases);
  //adicionar as acoes possiveis, 1o parametro é a ação, o segundo é o número da frase para ser impressa se for "true", o terceiro caso "false"
  //193 - Para ter acesso aos cursos, e necessario preencher seus dados pessoais
  $feedbackObject->addAction("preencherDados", 193, 193);

  $lista_escolaridade=RetornaListaEscolaridade($sock);
  $linha=FichaUsuario($sock,$_SESSION['cod_usuario_global_s']);

  /* 
  ==================
  Funcoes JavaScript
  ==================
  */

  //GeraJSVerificacaoData();
 
  echo("    <script type=\"text/javascript\">\n");

  //caminho utilizado no calendario
  echo("    var pathToImages = '../cursos/aplic/js-css/';\n");

  echo("    function Iniciar()\n");
  echo("    {\n");
  $feedbackObject->returnFeedback($_GET['acao'], $_GET['atualizacao']);
  echo("      document.formulario.nome_usuario.focus();\n");
  echo("      startList();\n");
  echo("    }\n\n");

  // Validação do RG:
  echo("    function RGValido(numero){\n");
  echo("      var arrayNumero = numero.split('');");
  echo("      if(numero.replace(/[ ]+/g, \"\").length == 0){\n");
  echo("        return false;\n");
  echo("      }\n");
  echo("      return true;\n");
  echo("    }\n");

  /* *********************************************************************
  Funcao Verificar - JavaScript. Verifica um a um cada campo do formulario
    Entrada: Nenhuma. Funcao espec�fica do formulario desta pagina
    Saida:   Boolean, para controle do onSubmit;
             true, se nao houver erros no formulario, 
             false, se houver.
  */
  echo("    function verificar()\n");
  echo("    {\n");
  echo("      nome_usuario = document.formulario.nome_usuario.value;\n");
  echo("      data = document.formulario.data.value;\n");
  echo("      email = document.formulario.email.value;\n");
  echo("      rg = document.formulario.rg.value;\n");
  echo("      endereco = document.formulario.endereco.value;\n");
  echo("      cidade = document.formulario.cidade.value;\n");
  echo("      estado = document.formulario.estado.value;\n");
  echo("      pais = document.formulario.pais.value;\n");
  echo("      if (nome_usuario == '')\n");
  echo("      {\n");
  /* 50 - O campo */ /* 32 - Nome */ /* 51 - n�o pode ser vazio */
  echo("        alert('".RetornaFraseDaLista($lista_frases_configurar,50)." ".RetornaFraseDaLista($lista_frases_configurar,32)." ".RetornaFraseDaLista($lista_frases_configurar,51).".');\n");
  echo("        document.formulario.nome_usuario.focus();\n");
  echo("        return false;\n");
  echo("      }\n");

  echo("      if (!RGValido(rg)){\n");
  /* 50 - O campo *//* 33 - RG parece estar errado */
  echo("        alert('".RetornaFraseDaLista($lista_frases_configurar,50)." ".RetornaFraseDaLista($lista_frases_configurar,33).".');\n");
  echo("        document.formulario.rg.focus();\n");
  echo("        return false;\n");
  echo("      }\n");

   // Verificação da Data.
  echo("      var ErroData = 0;\n");

  // Formato da data é valido?
  echo("      var DataValida = /^((0[1-9]|[12]\d)\/(0[1-9]|1[0-2])|30\/(0[13-9]|1[0-2])|31\/(0[13578]|1[02]))\/\d{4}$/;\n");
  echo("      if (!DataValida.test(data)){\n");
  echo("        ErroData = 1;\n");
  echo("      }\n");
  echo("      var data = data.split(\"/\");\n");
  echo("      var Hoje = new Date();\n");

  // Data no futuro?
  echo("      if (Hoje.getFullYear() < data[2]){\n");
  echo("        ErroData = 2;\n");
  echo("      } else if ((Hoje.getFullYear() == data[2]) && (Hoje.getMonth() <= data[1])){\n");
  echo("        ErroData = 2;\n");
  echo("      }\n");
  // 1 = Data Invalida
  // 2 = Data Nascimento no Futuro
  echo("      if (ErroData == 1){\n");
  echo("        alert('".RetornaFraseDaLista($lista_frases_configurar,3).".');\n");
  echo("        document.formulario.data.focus();\n");
  echo("        return false;\n");
  echo("      } else if (ErroData == 2){\n");
  echo("        alert('".RetornaFraseDaLista($lista_frases_configurar,3).".');\n");
  echo("        document.formulario.data.focus();\n");
  echo("        return false;\n");
  echo("      }\n");

  // Verifica se o e-mail é valido
  echo("      var EmailValido = /^[\w!#$%&'*+\/=?^`{|}~-]+(\.[\w!#$%&'*+\/=?^`{|}~-]+)*@(([\w-]+\.)+[A-Za-z]{2,6}|\[\d{1,3}(\.\d{1,3}){3}\])$/;\n");
  echo("      if (!EmailValido.test(email)){\n");
  /* 52 - O e-mail parece estar errado */
  echo("        alert('".RetornaFraseDaLista($lista_frases_configurar,52).".');\n");
  echo("        document.formulario.email.focus();\n");
  echo("        return false;\n");
  echo("      }\n");

  echo("      if (endereco == '')\n");
  echo("      {\n");
  /* 50 - O campo */ /* 40 - Endere�o */ /* 51 - n�o pode ser vazio */
  echo("        alert('".RetornaFraseDaLista($lista_frases_configurar,50)." ".RetornaFraseDaLista($lista_frases_configurar,40)." ".RetornaFraseDaLista($lista_frases_configurar,51).".');\n");
  echo("        document.formulario.endereco.focus();\n");
  echo("        return false;\n");
  echo("      }\n");
  echo("      if (cidade == '')\n");
  echo("      {\n");
  /* 50 - O campo */ /* 41 - Cidade */ /* 51 - n�o pode ser vazio */
  echo("        alert('".RetornaFraseDaLista($lista_frases_configurar,50)." ".RetornaFraseDaLista($lista_frases_configurar,41)." ".RetornaFraseDaLista($lista_frases_configurar,51).".');\n");
  echo("        document.formulario.cidade.focus();\n");
  echo("        return false;\n");
  echo("      }\n");
  echo("      if (estado == '')\n");
  echo("      {\n");
  /* 50 - O campo */ /* 42 - Estado */ /* 51 - n�o pode ser vazio */
  echo("        alert('".RetornaFraseDaLista($lista_frases_configurar,50)." ".RetornaFraseDaLista($lista_frases_configurar,42)." ".RetornaFraseDaLista($lista_frases_configurar,51).".');\n");
  echo("        document.formulario.estado.focus();\n");
  echo("        return false;\n");
  echo("      }\n");
  echo("      if (pais == '')\n");
  echo("      {\n");
  /* 50 - O campo */ /* 43 - Pa�s */ /* 51 - n�o pode ser vazio */
  echo("        alert('".RetornaFraseDaLista($lista_frases_configurar,50)." ".RetornaFraseDaLista($lista_frases_configurar,43)." ".RetornaFraseDaLista($lista_frases_configurar,51).".');\n");
  echo("        document.formulario.pais.focus();\n");
  echo("        return false;\n");
  echo("      }\n");
  echo("      return true;\n");
  echo("    }\n");

  echo("    function confereDados()\n");
  echo("    {\n");
  echo("      if(verificar())\n");
  echo("        xajax_AtualizaDadosUsuarioDinamic(xajax.getFormValues('formulario'));\n");
  echo("      return false;");
  echo("    }\n\n");

  echo("    function trataEnvio()\n");
  echo("    {\n");
  //177 - Dados atualizados com sucesso!
  echo("      mostraFeedback('".RetornaFraseDaLista($lista_frases,177)."', 'true');\n");
  if(isset($cod_curso))
    echo("      window.location = '../cursos/aplic/index.php?cod_curso=".$cod_curso."';\n");
  else if(!PreencheuDadosPessoais($sock))
    echo("      window.location = 'exibe_cursos.php';\n");
  echo("    }\n\n");

  echo("    function VerificaNumero(campo)\n");
  echo("    {\n");
  echo("      var digits=\"0123456789\";\n");
  echo("      var campo_temp;\n"); 
  echo("      for (var i=0;i<campo.value.length;i++)\n");
  echo("      {\n");
  echo("        campo_temp=campo.value.substring(i,i+1);\n"); 
  echo("        if (digits.indexOf(campo_temp)==-1)\n");
  echo("        {\n");
  echo("          campo.value = campo.value.substring(0,i);\n");
  echo("          break;\n");
  echo("        }\n");
  echo("      }\n");
  echo("    }\n\n");

  echo("    </script> \n");

  $objAjax->printJavascript();

  include("../menu_principal_tela_inicial.php");

  echo("        <td width=\"100%\" valign=\"top\" id=\"conteudo\">\n");

  /*1 - Configurar - 27 - Alterar dados pessoais*/
  echo("          <h4>".RetornaFraseDaLista($lista_frases_configurar,1)." - ".RetornaFraseDaLista($lista_frases_configurar,27)."</h4>\n");

  // 3 A's - Muda o Tamanho da fonte
  echo("          <div id=\"mudarFonte\">\n");
  echo("            <a onclick=\"mudafonte(2)\" href=\"#\"><img width=\"17\" height=\"15\" border=\"0\" align=\"right\" alt=\"Letra tamanho 3\" src=\"../cursos/aplic/imgs/btFont1.gif\"/></a>\n");
  echo("            <a onclick=\"mudafonte(1)\" href=\"#\"><img width=\"15\" height=\"15\" border=\"0\" align=\"right\" alt=\"Letra tamanho 2\" src=\"../cursos/aplic/imgs/btFont2.gif\"/></a>\n");
  echo("            <a onclick=\"mudafonte(0)\" href=\"#\"><img width=\"14\" height=\"15\" border=\"0\" align=\"right\" alt=\"Letra tamanho 1\" src=\"../cursos/aplic/imgs/btFont3.gif\"/></a>\n");
  echo("          </div>\n");

   /* 509 - Voltar */
  echo("                  <ul class=\"btsNav\"><li><span onclick=\"javascript:history.back(-1);\">&nbsp;&lt;&nbsp;".RetornaFraseDaLista($lista_frases_geral,509)."&nbsp;</span></li></ul>\n");
    
  echo("          <!-- Tabelao -->\n");
  echo("          <table cellpadding=\"0\" cellspacing=\"0\"  id=\"tabelaExterna\" class=\"tabExterna\">\n");
  echo("            <tr>");
  echo("              <td valign=\"top\">\n");
  echo("                <ul class=\"btAuxTabs\">\n");
  /* 27 - Alterar dados pessoais*/
  echo("                  <li><a href=\"dados.php\" id=\"alterar_dados\">".RetornaFraseDaLista($lista_frases_configurar, 27)."</a></li>\n");
  /* 65 - Alterar Login */
  echo("                  <li><a href=\"alterar_login.php\" id=\"alterar_login\">".RetornaFraseDaLista($lista_frases_configurar, 65)."</a></li>\n");
  /* 2 - Alterar Senha */
  echo("                  <li><a href=\"alterar_senha.php\" id=\"alterar_senha\">".RetornaFraseDaLista($lista_frases_configurar, 2)."</a></li>\n");
  /* 3 - Alterar Idioma */
  echo("                  <li><a href=\"selecionar_lingua.php\" id=\"selecionar_idioma\">".RetornaFraseDaLista($lista_frases_configurar, 3)."</a></li>\n");
  /* Autorizar Google Calendar */
  echo("                  <li><a href=\"autorizar_google_calendar.php\" id=\"autorizar_google_calendar\">".RetornaFraseDaLista($lista_frases_configurar,534)."</a></li>\n");
  echo("                </ul>\n");
  echo("              </td>\n");
  echo("            </tr>\n");
  echo("            <tr>\n");
  echo("              <td>\n");
  echo("                <table cellspacing=\"0\" class=\"tabInterna\">\n");
  echo("                  <tr class=\"head\">\n");
  /* 27 - Alterar dados pessoais */
  echo("                    <td>".RetornaFraseDaLista($lista_frases_configurar,27)."</td>\n");
  echo("                  </tr>\n");
  
  echo("                  <tr>\n");
  echo("                    <td class=\"alLeft\" style=\"border:none\">\n");
   
  /* 28 - Confira no formul�rio abaixo os seus dados */
  /* 29 - Modifique aqueles que forem necess�rios e pressione o bot�o */
  /* 27 - Alterar dados pessoais */
  /* 31 - para registrar os novos dados digitados */
  
  echo("                      <p>\n");
  
  echo("                         &nbsp;&nbsp;&nbsp;".RetornaFraseDaLista($lista_frases_configurar,28).". ".RetornaFraseDaLista($lista_frases_configurar,29)." '".RetornaFraseDaLista($lista_frases_configurar,27)."' ".RetornaFraseDaLista($lista_frases_configurar,31).".\n");
  echo("                      </p>\n");
  
  echo("                    </td>\n");
  echo("                  </tr>\n");
  echo("                  <tr>\n");
  echo("                    <td align=\"left\" style=\"padding-left:225px;\" >\n");

  $nome=$linha['nome'];
  $rg=$linha['rg'];
  $endereco=$linha['endereco'];
  $cidade=$linha['cidade'];
  $estado=$linha['estado'];
  $pais=$linha['pais'];
  $telefone=$linha['telefone'];
  $email=$linha['email'];
  $data_nasc=UnixTime2Data($linha['data_nasc']); 
  $sexo=$linha['sexo'];

  if ($sexo=="") $sexo='M';

  $local_trab=$linha['local_trab'];
  $profissao=$linha['profissao'];
  $informacoes=$linha['informacoes'];
  $escolaridade=$linha['cod_escolaridade'];

  echo("                      <form name=\"formulario\" id=\"formulario\" action=\"\" method=\"post\" onsubmit=\"return(confereDados());\">\n");
  echo("					  <input type=\"hidden\" name=\"texto_feedback\" value=\"".RetornaFraseDaLista($lista_frases,177)."\"/>\n");
  echo("                        <table>\n");
  echo("                          <tr>\n");
  /* 32 - Nome */
  echo("                            <td style=\"border:none; text-align:right;\">\n");

  echo("                              &nbsp;".RetornaFraseDaLista($lista_frases_configurar,32)." (*):\n");
  echo("                            </td>\n");
  echo("                            <td width=\"90%\" style=\"border:none\">\n");

  echo("                              <input class=\"input\" type=\"text\" size=\"30\" maxlength=\"128\" name=\"nome_usuario\" value='".$nome."' />\n");
  echo("                            </td>\n");
  echo("                          </tr>\n");
  echo("                          <tr>\n");
  /* 72 - RG */
  echo("                            <td style=\"border:none; text-align:right;\">\n");
  echo("                              &nbsp;".RetornaFraseDaLista($lista_frases_configurar,72)." (*):\n");
  echo("                            </td>\n");
  echo("                            <td style=\"border:none\">\n");
  echo("                              <input class=\"input\" type=\"text\" size=\"11\" maxlength=\"11\" name=\"rg\" value='".$rg."' />\n");
  echo("                            </td>\n");
  echo("                          </tr>\n");
  echo("                          <tr>\n");
  echo("                            <td style=\"border:none; text-align:right;\">\n");
  /* 34 - Data de nascimento */
  echo("                              &nbsp;".RetornaFraseDaLista($lista_frases_configurar,34).":\n");
  echo("                            </td>\n");
  echo("                            <td style=\"border:none\">\n");
  echo("                              <input class=\"input\" type=\"text\" size=\"10\" maxlength=\"10\" id=\"data\" name=\"data_nascimento\" value=\"".$data_nasc."\" /><img src=\"../cursos/aplic/imgs/ico_calendario.gif\" alt=\"calendario\" onclick=\"displayCalendar(document.getElementById ('data'),'dd/mm/yyyy',this);\" />");
  echo("                            </td>\n");
  echo("                          </tr>\n");
  echo("                          <tr>\n");
  echo("                            <td style=\"border:none; text-align:right;\">\n");
  /* 35 - Sexo */
  echo("                              &nbsp;".RetornaFraseDaLista($lista_frases_configurar,35).":\n");
  echo("                            </td>\n");
  if ($sexo=="M")
    $chM="checked=\"checked\"";
  else
    $chM="";
  if ($sexo=="F")
    $chF="checked=\"checked\"";
  else
    $chF="";
  echo("                            <td style=\"border:none\">\n");
  /* 36 - Masculino */ 
  echo("                              <input type=\"radio\" ".$chM." name=\"sexo\" value=\"M\" />".RetornaFraseDaLista($lista_frases_configurar,36)."\n");
  /* 37 - Feminino */
  echo("                              <input type=\"radio\" ".$chF." name=\"sexo\" value=\"F\" />".RetornaFraseDaLista($lista_frases_configurar,37)."\n");
  echo("                            </td>\n");
  echo("                          </tr>\n");
  echo("                          <tr>\n");
  echo("                            <td style=\"border:none; text-align:right;\">\n");
  /* 38 - Email */
  echo("                              &nbsp;".RetornaFraseDaLista($lista_frases_configurar,38)." (*):\n");
  echo("                            </td>\n");
  echo("                            <td style=\"border:none\">\n");
  echo("                              <input class=\"input\" type=\"text\" size=\"30\" maxlength=\"48\" name=\"email\" value='".$email."' />\n");
  echo("                            </td>\n");
  echo("                          </tr>\n");
  echo("                          <tr>\n");
  echo("                            <td style=\"border:none; text-align:right;\">\n");
  /* 39 - Telefone */
  echo("                              &nbsp;".RetornaFraseDaLista($lista_frases_configurar,39).":\n");
  echo("                            </td>\n");
  echo("                            <td style=\"border:none\">\n");
  echo("                              <input class=\"input\" type=\"text\" size=\"16\" maxlength=\"25\" name=\"telefone\" value='".$telefone."' />\n");
  echo("                            </td>\n");
  echo("                          </tr>\n");
  echo("                          <tr>\n");
  echo("                            <td style=\"border:none; text-align:right;\">\n");
  /* 40 - Endere�o */
  echo("                              &nbsp;".RetornaFraseDaLista($lista_frases_configurar,40)." (*):\n");
  echo("                            </td>\n");
  echo("                            <td style=\"border:none\">\n");
  echo("                              <input class=\"input\" type=\"text\" size=\"30\" maxlength=\"48\" name=\"endereco\" value='".$endereco."' />\n");
  echo("                            </td>\n");
  echo("                          </tr>\n");
  echo("                          <tr>\n");
  echo("                            <td style=\"border:none; text-align:right;\">\n");
  /* 41 - Cidade */
  echo("                              &nbsp;".RetornaFraseDaLista($lista_frases_configurar,41)." (*):\n");
  echo("                            </td>\n");
  echo("                            <td style=\"border:none\">\n");
  echo("                              <input class=\"input\" type=\"text\" size=\"20\" maxlength=\"32\" name=\"cidade\" value='".$cidade."' />\n");
  echo("                            </td>\n");
  echo("                          </tr>\n");
  echo("                          <tr>\n");
  echo("                            <td style=\"border:none; text-align:right;\">\n");
  /* 42 - Estado */
  echo("                              &nbsp;".RetornaFraseDaLista($lista_frases_configurar,42)." (*):\n");  
  echo("                            </td>\n");
  echo("                            <td style=\"border:none\">\n");
  echo("                              <input class=\"input\" type=\"text\" size=\"2\" maxlength=\"2\" name=\"estado\" value='".$estado."' />\n");
  /* 43 - Pa�s */
  echo("                              &nbsp;&nbsp;&nbsp;".RetornaFraseDaLista($lista_frases_configurar,43)." (*):\n");
  echo("                              <input class=\"input\" type=\"text\" size=\"12\" maxlength=\"19\" name=\"pais\" value='".$pais."' />\n");
  echo("                            </td>\n");
  echo("                          </tr>\n");
  echo("                          <tr>\n");
  echo("                            <td style=\"border:none; text-align:right;\">\n");
  /* 44 - Profiss�o */
  echo("                                 &nbsp;".RetornaFraseDaLista($lista_frases_configurar,44).":\n");
  echo("                            </td>\n");
  echo("                            <td style=\"border:none\">\n");
  echo("                              <input class=\"input\" type=\"text\" size=\"20\" maxlength=\"32\" name=\"profissao\" value='".$profissao."' />\n");
  echo("                            </td>\n");
  echo("                          </tr>\n");
  echo("                          <tr>\n");
  echo("                            <td width=\"50%\" style=\"border:none; text-align:right;\">\n");
  /* 45 - Local de trabalho */
  echo("                              &nbsp;".RetornaFraseDaLista($lista_frases_configurar,45).":\n");
  echo("                            </td>\n");
  echo("                            <td style=\"border:none\">\n");
  echo("                              <input class=\"input\" type=\"text\" size=\"20\" maxlength=\"32\" name=\"local\" value='".$local_trab."' />\n");
  echo("                            </td>\n");
  echo("                          </tr>\n");
  echo("                          <tr>\n");
  echo("                            <td width=\"50%\" style=\"border:none; text-align:right;\">\n");
  /* 46 - Escolaridade */
  echo("                              &nbsp;".RetornaFraseDaLista($lista_frases_configurar,46).":\n");
  echo("                            </td>\n");
  
  echo("                            <td style=\"border:none\">\n");
  echo("                              <select class=\"input\" name=\"cod_escolaridade\" size=\"1\">\n");

  foreach ($lista_escolaridade as $cod => $linha)
  { 
    if($escolaridade == $linha['cod_escolaridade'])
      $selecionado="selected=\"selected\"";
    else
      $selecionado="";
  echo("                                <option value='".$linha['cod_escolaridade']."' ".$selecionado.">".RetornaFraseDaLista($lista_frases_geral,$linha['cod_texto_escolaridade'])."</option>\n");
  }
  echo("                              </select>\n");

  echo("                            </td>\n");
  echo("                          </tr>\n");
  echo("                          <tr>\n");
  echo("                            <td valign=\"top\" style=\"border:none; text-align:right;\">\n");
  /* 47 - Informa��es adicionais */
  echo("                              &nbsp;".RetornaFraseDaLista($lista_frases_configurar,47).":\n");
  echo("                            </td>\n");
  echo("                            <td style=\"border:none\">\n");
  echo("                              <textarea class=\"input\" rows=\"5\" cols=\"30\" name=\"informacoes\">".$informacoes."\n");
  echo("                              </textarea> <br /><br />\n");
  echo("                            </td>\n");
  echo("                          </tr>\n");
  echo("                          <tr>\n");
  echo("                            <td style=\"border:none\"></td>\n");
  echo("                            <td style=\"border:none\">\n");
  /* 66 - (*) Campos Obrigat�rios */
  echo("                              ".RetornaFraseDaLista($lista_frases_configurar,66)."\n");
  echo("                              <br /><br />\n");
  echo("                            </td>\n"); 
  echo("                          </tr>\n");  
  echo("                          <tr>\n"); 
  echo("                            <td style=\"border:none\"></td>\n");  
  echo("                            <td style=\"border:none\">\n");
  echo("                              <input type=\"submit\" class=\"input\" value=\"".RetornaFraseDaLista($lista_frases_configurar,27)."\" id=\"registar_altd\" />\n");
  echo("                            </td>\n");
  echo("                          </tr>\n");
  echo("                        </table>\n"); 
  echo("                      </form>\n");
  echo("                    </td>\n");
  echo("                  </tr>\n");
  echo("                </table>\n");
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
