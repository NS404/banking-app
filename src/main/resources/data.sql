INSERT INTO user (id,password, role, username)
VALUES
    (1, '$2a$10$uMV4bkEay6iOO1ykeMuatuAgH21yS3rCN3.HjB9XdQDScgZ/ydXd2' ,'ADMIN', 'admin'),
    (2, '$2a$10$GLqi.m6QGXq8k8Wj1ea36.LPxg9.f3PA3xnYL9F.s57myXS0PzjJO' ,'CLIENT', 'client'),
    (3, '$2a$10$OTvkMyVyJlMlhQg45NuPluUPOvs6Gz2v/Pcj1/eTeIaS6iidpd2Au' ,'CLIENT', 'client1'),
    (4, '$2a$10$XaRpmV5Kgi.PL3qJceHha.eaWZOIEheJLHVXLIqf9Qx9Kv1QTR4.S' ,'TELLER', 'teller');



INSERT INTO account (id, iban, account_type, balance, currency, interest, client_id)
VALUES
    (1, 'AL12345678912345678912345678', 'CURRENT', 100.0, 'EUR', 0.0, 2),
    (2, 'AL12345678912345678912345676', 'TECHNICAL', 200.0, 'USD', 0.01, 2),
    (3, 'AL12345678912345678912345674', 'CURRENT', 200.0, 'USD', 0.0, 3),
    (4, 'AL12345678912345678912345671', 'TECHNICAL', 200.0, 'EUR', 0.13, 3),
    (5, 'AL12345678912345678912345611', 'CURRENT', 200.0, 'EUR', 0.0, 3);


INSERT INTO card (id, card_type, account_id)
VALUES
    (1, 'DEBIT', 1),
    (2, 'CREDIT', 2),
    (3, 'DEBIT', 3),
    (4, 'CREDIT', 4);

