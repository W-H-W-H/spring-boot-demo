INSERT INTO  book (id, title, isbn)  VALUES ('B00001', 'A Military History Of The Western World, Vol. I: From The Earliest Times To The Battle Of Lepanto', '978-0306803048');
INSERT INTO  book (id, title, isbn)  VALUES ('B00002', 'A Military History Of The Western World, Vol. II: From The Defeat Of The Spanish Armada To The Battle Of Waterloo', '978-0306803055');
INSERT INTO  book (id, title, isbn)  VALUES ('B00003', 'A Military History Of The Western World, Vol. III: From the American Civil War to the End of World War II', '978-0306803062');
INSERT INTO  book (id, title, isbn)  VALUES ('B00004', 'Generalship of Alexander the Great', '978-0306813306');
INSERT INTO  book (id, title, isbn)  VALUES ('B00005', 'Rust for Rustaceans', '978-1718501850');
INSERT INTO  book (id, title, isbn)  VALUES ('B00006', 'Strategy: Second Revised Edition', '978-0452010710');
INSERT INTO  book (id, title, isbn)  VALUES ('B00007', 'Panzer Leader', '978-0306811012');
INSERT INTO  book (id, title, isbn)  VALUES ('B00008', 'Scipio Africanus: Greater Than Napoleon', '978-0306813634');
INSERT INTO  book (id, title, isbn)  VALUES ('B00009', 'Spring Boot in Action. First Edition Edition', '978-1617292545');
INSERT INTO  book (id, title, isbn)  VALUES ('B00010', 'Elixir in Action. First Edition', '978-1617292019');

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






