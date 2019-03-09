INSERT INTO `lingua_textos` (`cod_texto`, `cod_lingua`, `cod_ferramenta`, `texto`)
VALUES (533, 1, -7, 'Google Agenda Autorizado');

INSERT INTO `lingua_textos` (`cod_texto`, `cod_lingua`, `cod_ferramenta`, `texto`)
VALUES (533, 2, -7, 'Google Calendar Autorizado');

INSERT INTO `lingua_textos` (`cod_texto`, `cod_lingua`, `cod_ferramenta`, `texto`)
VALUES (533, 3, -7, 'Google Calendar Authorized');

INSERT INTO `lingua_textos` (`cod_texto`, `cod_lingua`, `cod_ferramenta`, `texto`)
VALUES (533, 4, -7, 'Google Calendar Autorizado');

INSERT INTO `lingua_textos` (`cod_texto`, `cod_lingua`, `cod_ferramenta`, `texto`)
VALUES (534, 1, -7, 'Autorizar Google Agenda');

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

INSERT INTO `lingua_textos` (`cod_texto`, `cod_lingua`, `cod_ferramenta`, `texto`)
VALUES (536, 1, 22, 'Integrar ao Google Agenda');

INSERT INTO `lingua_textos` (`cod_texto`, `cod_lingua`, `cod_ferramenta`, `texto`)
VALUES (536, 2, 22, 'Integrar al Google Agenda');

INSERT INTO `lingua_textos` (`cod_texto`, `cod_lingua`, `cod_ferramenta`, `texto`)
VALUES (536, 3, 22, 'Integrate to Google Calendar');

INSERT INTO `lingua_textos` (`cod_texto`, `cod_lingua`, `cod_ferramenta`, `texto`)
VALUES (536, 4, 22, 'Integrar ao Google Agenda');

INSERT INTO `lingua_textos` (`cod_texto`, `cod_lingua`, `cod_ferramenta`, `texto`)
VALUES (537, 1, 22, 'Essa avaliação e seus dados salvos serão integrados ao Google Agenda. Confirma?');

INSERT INTO `lingua_textos` (`cod_texto`, `cod_lingua`, `cod_ferramenta`, `texto`)
VALUES (537, 2, 22, 'Esta evaluación y sus datos guardados se integrarán a Google Calendar. Confirmar?');

INSERT INTO `lingua_textos` (`cod_texto`, `cod_lingua`, `cod_ferramenta`, `texto`)
VALUES (537, 3, 22, 'This evaluation and it`s saved data will be integrated into Google Calendar. Do you confirm?');

INSERT INTO `lingua_textos` (`cod_texto`, `cod_lingua`, `cod_ferramenta`, `texto`)
VALUES (537, 4, 22, 'Essa avaliação e seus dados salvos serão integrados ao Google Agenda. Confirma?');

CREATE TABLE usuario_google_calendar
(
  cod_usuario INT(11),
  google_calendar_ativado BOOLEAN,
  access_token VARCHAR(200),
  expiracao_access_token INT (11),
  refresh_token VARCHAR(100)
);
