INSERT INTO `lingua_textos` (`cod_texto`, `cod_lingua`, `cod_ferramenta`, `texto`)
VALUES (533, 1, -7, 'Google Calendar Autorizado');

INSERT INTO `lingua_textos` (`cod_texto`, `cod_lingua`, `cod_ferramenta`, `texto`)
VALUES (533, 2, -7, 'Google Calendar Autorizado');

INSERT INTO `lingua_textos` (`cod_texto`, `cod_lingua`, `cod_ferramenta`, `texto`)
VALUES (533, 3, -7, 'Google Calendar Authorized');

INSERT INTO `lingua_textos` (`cod_texto`, `cod_lingua`, `cod_ferramenta`, `texto`)
VALUES (533, 4, -7, 'Google Calendar Autorizado');

INSERT INTO `lingua_textos` (`cod_texto`, `cod_lingua`, `cod_ferramenta`, `texto`)
VALUES (534, 1, -7, 'Autorizar Google Calendar');

INSERT INTO `lingua_textos` (`cod_texto`, `cod_lingua`, `cod_ferramenta`, `texto`)
VALUES (534, 2, -7, 'Autorizar Google Calendar');

INSERT INTO `lingua_textos` (`cod_texto`, `cod_lingua`, `cod_ferramenta`, `texto`)
VALUES (534, 3, -7, 'Authorize Google Calendar');

INSERT INTO `lingua_textos` (`cod_texto`, `cod_lingua`, `cod_ferramenta`, `texto`)
VALUES (534, 4, -7, 'Autorizar Google Calendar');

INSERT INTO `lingua_textos` (`cod_texto`, `cod_lingua`, `cod_ferramenta`, `texto`)
VALUES (535, 1, -7, 'Confirmar');

INSERT INTO `lingua_textos` (`cod_texto`, `cod_lingua`, `cod_ferramenta`, `texto`)
VALUES (535, 2, -7, 'Confirmar');

INSERT INTO `lingua_textos` (`cod_texto`, `cod_lingua`, `cod_ferramenta`, `texto`)
VALUES (535, 3, -7, 'Confirm');

INSERT INTO `lingua_textos` (`cod_texto`, `cod_lingua`, `cod_ferramenta`, `texto`)
VALUES (535, 4, -7, 'Confirmar');

CREATE TABLE usuario_google_calendar
(
  cod_usuario INT(11),
  google_calendar_ativado BOOLEAN,
  access_token VARCHAR(200),
  expiracao_access_token INT (11),
  refresh_token VARCHAR(100)
);