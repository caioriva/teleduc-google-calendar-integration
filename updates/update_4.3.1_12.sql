/* Base Geral*/

INSERT IGNORE INTO `Lingua_textos` (`cod_texto`, `cod_lingua`, `cod_ferramenta`, `texto`) VALUES
(106, 1, 14, 'Seu coment�rio est� vazio. Para n�o envi�-lo, pressione o bot�o Cancelar.'),
(106, 2, 14, 'Su comentario est� vac�o. Para no enviarlo, presione el bot�n Cancelar.'),
(106, 3, 14, 'Your comment is empty. To not send it, press the Cancel button.'),
(106, 4, 14, 'O teu coment�rio est� vazio. Para n�o o enviar, pressiones o bot�o Cancelar.');

UPDATE `Lingua_textos`  set  `texto` = 
'Seu coment�rio est� vazio. Para n�o envi�-lo, pressione o bot�o Cancelar.'
where `cod_texto`      = 106 and
      `cod_lingua`     = 1   and
      `cod_ferramenta` = 15;

UPDATE `Lingua_textos`  set  `texto` = 
'O teu coment�rio est� vazio. Para n�o o enviar, pressiones o bot�o Cancelar.'
where `cod_texto`      = 106 and
      `cod_lingua`     = 4   and
      `cod_ferramenta` = 15;
