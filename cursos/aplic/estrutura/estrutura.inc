<?php
/*
<!--
-------------------------------------------------------------------------------

    Arquivo : cursos/aplic/estrutura/estrutura.inc

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


/* *********************************************************************
   RetornaListaFerramentas - Retorna a lista de ferramentas
   Entrada: $sock - BASE EXTERNA
   Saida: array [$cod_ferramenta]['cod_texto_nome']
                [$cod_ferramenta]['cod_texto_descricao']
                [$cod_ferramenta]['diretorio']
*/
// function RetornaListaFerramentas($sock)
// {
//   $query ="select * from Ferramentas where cod_ferramenta!=43 order by cod_ferramenta";
//   $res=Enviar($sock,$query);
//   $lista=RetornaArrayLinhas($res);
//   unset($lista1);
//   foreach($lista as $cod=>$linha)
//   {
//     $lista1[$linha['cod_ferramenta']]['cod_texto_nome']=$linha['cod_texto_nome'];
//     $lista1[$linha['cod_ferramenta']]['cod_texto_descricao']=$linha['cod_texto_descricao'];
//     $lista1[$linha['cod_ferramenta']]['diretorio']=$linha['diretorio'];
//   }
//   return ($lista1);
// }

/* *********************************************************************
   RetornaOrdemFerramentas - Retorna a ordem das ferramentas no menu
   Entrada: $sock - BASE EXTERNA
   Saida: array []['cod_ferramenta']= codigo da ferramenta
                []['posicao'] = posicao da ferramenta. -1 se barra
*/
// function RetornaOrdemFerramentas($sock)
// {
//   $query ="select * from Menu where cod_ferramenta!=43 order by posicao";
//   $res=Enviar($sock,$query);
//   $lista=RetornaArrayLinhas($res);
//   return ($lista);
// }


?>
