INSERT INTO user (id,password, role, username)
VALUES
    (1, '$2y$10$pQk.BfBXK6Hnir3iFozCCeiMWCBCsfOaW7DKpJwJqY9R3hj2rmrlm' ,'ADMIN', 'admin'),
    (2, '$2y$10$PIQA1dGgk7STIErfIwSj/eWP8je.DWoKyMGYYQGQ9zZgNkwAs4Mui' ,'CLIENT', 'client'),
    (3, '$2y$10$yPdQsx1ZV4eymOLYbqxCbu7DWHgOlEj8JUef.It7gYDMFIqn/MvbK' ,'CLIENT', 'client'),
    (4, '$2y$10$InvztSJvZ.rfYpkTEfUfjuJgfyfJFoT3xI/U9jCS1R.UIQMW4mp/K' ,'TELLER', 'teller');



INSERT INTO account (id, iban, account_type, balance, currency, interest, client_id)
VALUES
    (1, 'AL12345678912345678912345678', 'CURRENT', 100.0, 'EUR', 0.0, 1),
    (2, 'AL12345678912345678912345676', 'TECHNICAL', 200.0, 'USD', 0.01, 1),
    (3, 'AL12345678912345678912345674', 'CURRENT', 200.0, 'USD', 0.0, 1),
    (4, 'AL12345678912345678912345671', 'TECHNICAL', 200.0, 'ALL', 0.13, 1);

INSERT INTO card (id, card_type, account_id)
VALUES
    (1, 'DEBIT', 1),
    (2, 'CREDIT', 2);

