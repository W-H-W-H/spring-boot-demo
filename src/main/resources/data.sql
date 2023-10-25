INSERT INTO 
book (id, title, isbn) 
VALUES ('B00001', 'A Military History Of The Western World, Vol. I: From The Earliest Times To The Battle Of Lepanto', '0306803046');

INSERT INTO 
book (id, title, isbn) 
VALUES ('B00002', 'History of the Second World War', '0304935646');


INSERT INTO
app_role (id, role_name)
VALUES ('R0001', 'ADMIN');

INSERT INTO
app_role (id, role_name)
VALUES ('R0002', 'USER');

INSERT INTO
app_role (id, role_name)
VALUES ('R0003', 'MANAGER');

INSERT INTO
app_user (id, email, display_name, password, is_enabled)
VALUES (1, 'specterfbells@gmail.com', 'Super Admin', '$2a$10$xO9r5vq.HwwHWugVxWj22uq30GwhcIUC.f32zgEEzSmETSlCTUfii', true);

INSERT INTO
app_user (id, email, display_name, password, is_enabled)
VALUES (2, 'waiting.13@gmail.com', 'Wai Ting 13', '$2a$10$xO9r5vq.HwwHWugVxWj22uq30GwhcIUC.f32zgEEzSmETSlCTUfii', true);

INSERT INTO app_user_role (app_user_id, app_role_id)
VALUES (1, 'R0001');

INSERT INTO app_user_role (app_user_id, app_role_id)
VALUES (1, 'R0002');

INSERT INTO app_user_role (app_user_id, app_role_id)
VALUES (1, 'R0003');

INSERT INTO app_user_role (app_user_id, app_role_id)
VALUES (2, 'R0002');






