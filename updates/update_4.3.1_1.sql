/* Base Geral*/

UPDATE `Lingua_textos`  set  `texto` = 
'Editar Quest�o Objetiva'
where `cod_texto`      = 99 and
      `cod_lingua`     = 1  and
      `cod_ferramenta` = 23;

UPDATE `Lingua_textos`  set  `texto` = 
'Salvar Todas'
where `cod_texto`      = 181 and
      `cod_lingua`     = 1   and
      `cod_ferramenta` = 23;

UPDATE `Lingua_textos`  set  `texto` = 
'Salvar Resposta'
where `cod_texto`      = 182 and
      `cod_lingua`     = 1   and
      `cod_ferramenta` = 23;

DELETE FROM `Lingua_textos` 
where `cod_texto`      > 502 and
      `cod_lingua`     = 1   and
      `cod_ferramenta` = 23;

INSERT IGNORE INTO `Lingua_textos` (`cod_texto`, `cod_lingua`, `cod_ferramenta`, `texto`) VALUES
(24,  1, 23, 'Editar resposta'),
(232, 1, 23, 'Cria��o de Alternativa'),
(233, 1, 23, 'N�o Corrigida');


UPDATE `Lingua_textos`  set  `texto` = 
'Edit Answer'
where `cod_texto`      = 24 and
      `cod_lingua`     = 3  and
      `cod_ferramenta` = 23;

UPDATE `Lingua_textos`  set  `texto` = 
'Save All'
where `cod_texto`      = 181 and
      `cod_lingua`     = 3   and
      `cod_ferramenta` = 23;

UPDATE `Lingua_textos`  set  `texto` = 
'Save Answer'
where `cod_texto`      = 182 and
      `cod_lingua`     = 3   and
      `cod_ferramenta` = 23;

DELETE FROM `Lingua_textos` 
where `cod_texto`      > 231 and
      `cod_lingua`     = 3   and
      `cod_ferramenta` = 23;

INSERT IGNORE INTO `Lingua_textos` (`cod_texto`, `cod_lingua`, `cod_ferramenta`, `texto`) VALUES
(232, 3, 23, 'Creation of Alternative'),
(233, 3, 23, 'Not corrected');

DELETE FROM `Lingua_textos` 
where `cod_texto`      > 1 and
      `cod_lingua`     = 4 and
      `cod_ferramenta` = 23;

INSERT IGNORE INTO `Lingua_textos` (`cod_texto`, `cod_lingua`, `cod_ferramenta`, `texto`) VALUES
(2, 4, 23, 'Existem questoes n�o corrigidas'),
(3, 4, 23, 'Coment�rio feito com sucesso!'),
(4, 4, 23, 'Corrigir Exerc�cio'),
(5, 4, 23, 'Voltar'),
(6, 4, 23, 'Compartilhado com Formadores'),
(7, 4, 23, 'Totalmente compartilhado'),
(8, 4, 23, 'N�o compartilhado'),
(9, 4, 23, 'Resposta Certa'),
(10, 4, 23, 'Resposta Errada'),
(11, 4, 23, 'Alternativa Certa'),
(12, 4, 23, 'Arquivos'),
(13, 4, 23, 'T�tulo'),
(14, 4, 23, 'Nota'),
(15, 4, 23, 'Valor'),
(16, 4, 23, 'Status'),
(17, 4, 23, 'Enunciado'),
(18, 4, 23, 'Alternativas'),
(19, 4, 23, 'Corrigida'),
(20, 4, 23, 'Resposta'),
(21, 4, 23, 'Ok'),
(22, 4, 23, 'Cancelar'),
(23, 4, 23, 'Coment�rio do Avaliador'),
(24, 4, 23, 'Editar resposta'),
(25, 4, 23, 'Editar Nota'),
(26, 4, 23, 'Editar Coment�rio'),
(27, 4, 23, 'Fechar'),
(28, 4, 23, 'Entregar Corre��o'),
(29, 4, 23, 'Aplica��o cancelada com sucesso!'),
(30, 4, 23, 'Exerc�cio aplicado com sucesso'),
(31, 4, 23, 'Exerc�cio reaplicado com sucesso'),
(32, 4, 23, 'Quest�es inclu�das com sucesso'),
(33, 4, 23, 'T�tulo alterado com sucesso.'),
(34, 4, 23, 'O t�tulo n�o pode ser vazio.'),
(35, 4, 23, 'N�o h� nenhuma quest�o'),
(36, 4, 23, 'Quest�es apagadas com sucesso.'),
(37, 4, 23, 'Valores atribuidos com sucesso'),
(38, 4, 23, 'O valor deve ser num�rico!'),
(39, 4, 23, 'Realmente deseja apagar o ficheiro?'),
(40, 4, 23, 'Tem a certeza que deseja descompactar este ficheiro?'),
(41, 4, 23, 'Realmente deseja ocultar o ficheiro? Ele n�o sera vis�vel para alunos.'),
(42, 4, 23, 'Ficheiro(s) ocultado(s) com sucesso.'),
(43, 4, 23, 'Ficheiro apagado com sucesso.'),
(44, 4, 23, 'Ficheiro anexado com sucesso'),
(45, 4, 23, 'Hora de disponibiliza��o inv�lida. Por favor volte e corrija.'),
(46, 4, 23, 'Hora de limite de entrega inv�lida. Por favor volte e corrija.'),
(47, 4, 23, 'A disponibiliza��o do exerc�cio deve ser posterior a data atual.'),
(48, 4, 23, 'O limite de entrega deve ser posterior a disponibiliza��o do exerc�cio.'),
(49, 4, 23, 'Editar Exerc�cio'),
(50, 4, 23, 'Renomear t�tulo'),
(51, 4, 23, 'Editar texto'),
(52, 4, 23, 'Limpar texto'),
(53, 4, 23, 'Aplicar'),
(54, 4, 23, 'Reaplicar'),
(55, 4, 23, 'Cancelar aplica��o'),
(56, 4, 23, 'Hist�rico'),
(57, 4, 23, 'Compartilhamento'),
(58, 4, 23, 'Texto'),
(59, 4, 23, 'Quest�es'),
(60, 4, 23, 'Tipo'),
(61, 4, 23, 'T�pico'),
(62, 4, 23, 'Dificuldade'),
(63, 4, 23, 'Total'),
(64, 4, 23, 'Apagar selecionadas'),
(65, 4, 23, 'Atribuir valor'),
(66, 4, 23, 'Adicionar quest�es'),
(67, 4, 23, 'Nome'),
(68, 4, 23, 'Tamanho'),
(69, 4, 23, 'Data'),
(70, 4, 23, 'Oculto'),
(71, 4, 23, 'Apagar'),
(72, 4, 23, 'Descompactar'),
(73, 4, 23, 'Ocultar'),
(74, 4, 23, '�rea restrita ao formador'),
(75, 4, 23, 'Associar a avalia��o'),
(76, 4, 23, 'Sim'),
(77, 4, 23, 'N�o'),
(78, 4, 23, 'Disponibilizar gabarito com a correc��o'),
(79, 4, 23, 'Tipo de aplica��o'),
(80, 4, 23, 'Individual'),
(81, 4, 23, 'Em Grupo'),
(82, 4, 23, 'Disponibiliza��o'),
(83, 4, 23, 'Imediata'),
(84, 4, 23, 'Agendar'),
(85, 4, 23, 'Hor�rio'),
(86, 4, 23, 'Limite de entrega'),
(87, 4, 23, 'Realmente deseja limpar o gabarito? O conte�do ser� perdido.'),
(88, 4, 23, 'O t�tulo n�o pode ser vazio.'),
(89, 4, 23, 'T�pico criado com sucesso'),
(90, 4, 23, 'Realmente deseja apagar o(s) item(s) selecionado(s)?'),
(91, 4, 23, 'Dificuldade atualizada com sucesso.'),
(92, 4, 23, 'T�pico atualizado com sucesso.'),
(93, 4, 23, 'Tem a certeza que deseja excluir definitivamente as quest�es?'),
(94, 4, 23, 'Editar enunciado'),
(95, 4, 23, 'Novo topico'),
(96, 4, 23, 'Limpar enunciado'),
(97, 4, 23, 'Editar gabarito'),
(98, 4, 23, 'Limpar gabarito'),
(99, 4, 23, 'Editar Quest�o Objetiva'),
(100, 4, 23, 'Dif�cil'),
(101, 4, 23, 'M�dio'),
(102, 4, 23, 'F�cil'),
(103, 4, 23, 'Gabarito'),
(104, 4, 23, 'Editar'),
(105, 4, 23, 'Adicionar Alternativa'),
(106, 4, 23, 'Nome do t�pico'),
(107, 4, 23, 'Exerc�cios Individuais Dispon�veis'),
(108, 4, 23, 'Exerc�cios em Grupo Dispon�veis'),
(109, 4, 23, 'Exerc�cios Individuais'),
(110, 4, 23, 'Exerc�cios em Grupo'),
(111, 4, 23, 'Biblioteca de Exerc�cios'),
(112, 4, 23, 'Biblioteca de Quest�es'),
(113, 4, 23, 'Agrupar'),
(114, 4, 23, 'de'),
(115, 4, 23, 'do grupo'),
(116, 4, 23, 'Exerc�cios n�o entregues'),
(117, 4, 23, 'Exerc�cios n�o corrigidos'),
(118, 4, 23, 'N�o h� nenhum exerc�cio'),
(119, 4, 23, 'Agrupar por:'),
(120, 4, 23, 'O titulo deve conter apenas n�meros, letras e espacos'),
(121, 4, 23, 'Aplica��o cancelada com sucesso.'),
(122, 4, 23, 'Tem a certeza que deseja excluir definitivamente o(s) exerc�cio(s) selecionado(s)?'),
(123, 4, 23, 'Tem a certeza que deseja recuperar os exerc�cios selecionados?'),
(124, 4, 23, 'Tem a certeza que deseja enviar para lixeira os exerc�cios selecionados?'),
(125, 4, 23, 'Exerc�cio(s) exclu�do(s) da lixeira.'),
(126, 4, 23, 'Exerc�cio(s) recuperado(s).'),
(127, 4, 23, 'Exerc�cio(s) enviado(s) para lixeira.'),
(128, 4, 23, 'Lixeira'),
(129, 4, 23, 'Novo exerc�cio'),
(130, 4, 23, 'Situa��o'),
(131, 4, 23, 'Apagar selecionados'),
(132, 4, 23, 'Cancelar aplica��o dos selecionados'),
(133, 4, 23, 'Recuperar selecionados'),
(134, 4, 23, 'A��o'),
(135, 4, 23, 'Usu�rio'),
(136, 4, 23, 'Cria��o'),
(137, 4, 23, 'Aplicada'),
(138, 4, 23, 'Cancelada aplica��o'),
(139, 4, 23, 'Desconhecida'),
(140, 4, 23, 'Edi��o Cancelada'),
(141, 4, 23, 'Em Edi��o'),
(142, 4, 23, 'Edi��o Finalizada'),
(143, 4, 23, 'Questoes ordenadas.'),
(144, 4, 23, 'Tem a certeza que deseja excluir definitivamente as quest�es selecionadas?'),
(145, 4, 23, 'Tem a certeza que deseja recuperar as quest�es selecionadas?'),
(146, 4, 23, 'Tem a certeza que deseja enviar para lixeira as quest�es selecionadas?'),
(147, 4, 23, 'Quest�es exclu�das da lixeira.'),
(148, 4, 23, 'Quest�es recuperadas.'),
(149, 4, 23, 'Quest�es enviadas para a lixeira.'),
(150, 4, 23, 'Voltar � edi��o do exerc�cio'),
(151, 4, 23, 'Nova quest�o'),
(152, 4, 23, 'Filtrar'),
(153, 4, 23, 'clique no cabe�alho para ordenar as quest�es'),
(154, 4, 23, 'Incluir selecionadas em um exerc�cio'),
(155, 4, 23, 'Incluir selecionadas no exerc�cio'),
(156, 4, 23, 'Recuperar selecionadas'),
(157, 4, 23, 'Escolha um exerc�cio:'),
(158, 4, 23, 'Tipo da quest�o');
INSERT INTO `Lingua_textos` (`cod_texto`, `cod_lingua`, `cod_ferramenta`, `texto`) VALUES
(159, 4, 23, 'Objetiva'),
(160, 4, 23, 'Dissertativa'),
(161, 4, 23, 'Todos'),
(162, 4, 23, 'Todas'),
(163, 4, 23, 'Realmente deseja entregar? Quest�es n�o salvas n�o ser�o enviadas.'),
(164, 4, 23, 'Todas as respostas foram salvas com sucesso.'),
(165, 4, 23, 'Resolver exerc�cio'),
(166, 4, 23, 'N�o respondida'),
(167, 4, 23, 'Respondida'),
(168, 4, 23, 'Coment�rio'),
(169, 4, 23, 'Ver resolu��o'),
(170, 4, 23, 'Avalia��o'),
(171, 4, 23, 'Resposta gravada'),
(172, 4, 23, 'Desejas excluir os ficheiro(s) selecionado(s) ?'),
(173, 4, 23, 'Escolha um t�pico'),
(174, 4, 23, 'Desejas entregar a correc��o?'),
(175, 4, 23, 'Texto editado com sucesso!'),
(176, 4, 23, 'Nota final'),
(177, 4, 23, 'quest�o'),
(178, 4, 23, 'T�tulo do Exerc�cio'),
(179, 4, 23, 'Grupo'),
(180, 4, 23, 'Aluno'),
(181, 4, 23, 'Editar gabarito'),
(182, 4, 23, 'Limpar gabarito'),
(183, 4, 23, 'Esconder'),
(184, 4, 23, 'Uma quest�o pode conter no m�ximo 10 alternativas.'),
(185, 4, 23, 'Ficheiro anexado com sucesso.'),
(186, 4, 23, ' (oculto)'),
(187, 4, 23, 'Diret�rio est� vazio.'),
(188, 4, 23, 'Existem quest�es com valores iguais a 0, Deseja continuar?'),
(189, 4, 23, 'Entregar'),
(190, 4, 23, 'Corrigir Exerc�cio'),
(191, 4, 23, 'Exerc�cio entregue com sucesso!'),
(192, 4, 23, 'Compartilhamento alterado com sucesso.'),
(193, 4, 23, 'N�o � poss�vel aplicar um exerc�cio vazio. Adicione ao menos uma quest�o.'),
(194, 4, 23, 'N�o h� coment�rios dispon�veis'),
(195, 4, 23, 'Pressione o bot�o abaixo para selecionar o ficheiro a ser anexado.(ficheiros .ZIP podem ser enviados e descompactados posteriormente)'),
(196, 4, 23, 'Tem a certeza que deseja enviar para a lixeira esse exerc�cio?'),
(197, 4, 23, 'Desejas limpar o texto? O conte�do ser� perdido.'),
(198, 4, 23, 'Texto exclu�do com sucesso!'),
(199, 4, 23, 'Realmente deseja cancelar aplica��o dos selecionados?'),
(200, 4, 23, 'Enunciado editado com sucesso'),
(201, 4, 23, 'Exerc�cio criado com sucesso!'),
(202, 4, 23, 'Alternativa adicionada com sucesso!'),
(203, 4, 23, 'Alternativa editada com sucesso!'),
(204, 4, 23, 'Quest�o criada com sucesso!'),
(205, 4, 23, 'Enunciado exclu�do com sucesso.'),
(206, 4, 23, 'Nome do anexo com acentos ou caracteres inv�lidos! Renomeie o ficheiro e tente novamente.'),
(207, 4, 23, 'ja existe(m) alternativa(s) correta(s). Deseja continuar?'),
(208, 4, 23, 'Pressione o bot�o abaixo para selecionar o ficheiro a ser anexado.(ficheiros .ZIP podem ser enviados e descompactados posteriormente)'),
(209, 4, 23, 'Erro ao apagar ficheiro.'),
(210, 4, 23, 'Ficheiro descompactado com sucesso.'),
(211, 4, 23, 'Erro ao descompactar ficheiro.'),
(212, 4, 23, 'Multipla escolha'),
(213, 4, 23, 'Digite um nome para o t�pico'),
(214, 4, 23, 'Editar Questao Multipla Escolha'),
(215, 4, 23, 'Erro ao anexar ficheiro'),
(216, 4, 23, 'Quest�es filtradas'),
(217, 4, 23, 'ja existe'),
(218, 4, 23, 'Deseja sobrescrev�-lo?'),
(219, 4, 23, 'Ficheiro'),
(220, 4, 23, 'Abrindo a pasta requisitada,aguarde...'),
(221, 4, 23, 'a'),
(222, 4, 23, 'de'),
(223, 4, 23, 'Coment�rio de Aluno'),
(224, 4, 23, 'Coment�rio de Formador'),
(225, 4, 23, 'Coment�rios postados por mim'),
(226, 4, 23, 'Corrigido'),
(227, 4, 23, 'Entregue'),
(228, 4, 23, 'A quest�o foi aplicada em um exerc�cio, portanto n�o pode ser editada.'),
(229, 4, 23, 'Disponibilizado'),
(230, 4, 23, 'Agendado'),
(231, 4, 23, 'Em cria��o'),
(232, 4, 23, 'Cria��o de Alternativa'),
(233, 4, 23, 'N�o Corrigida'),
(234, 4, 23, 'Salvar Todas'),
(235, 4, 23, 'Salvar Resposta');