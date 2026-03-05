TRUNCATE TABLE transactions, accounts RESTART IDENTITY CASCADE;

-- 1. Добавление тестовых аккаунтов
INSERT INTO accounts (account_number, balance, status, created_at) VALUES
                                                                       ('10203810400000000001', 5000.00, 'ACTIVE', now() - INTERVAL '2 months'),
                                                                       ('10203810400000000002', 1500.50, 'ACTIVE', now() - INTERVAL '45 days'),
                                                                       ('10203810400000000003', 0.00, 'ACTIVE', now() - INTERVAL '10 days'),
                                                                       ('10203810400000000004', 100.00, 'BLOCKED', now() - INTERVAL '5 days'),
                                                                       ('10203810400000000005', 10000.00, 'ACTIVE', now() - INTERVAL '1 month'),
                                                                       ('10203810400000000006', -150.75, 'ACTIVE', now() - INTERVAL '3 days'),
                                                                       ('10203810400000000007', -5000.00, 'ACTIVE', now() - INTERVAL '1 day');

--2 . Пополнение
INSERT INTO transactions (receiver_account_id, amount, operation_type, status, balance_after_op, created_at)
VALUES
(1, 5000.00, 'DEPOSIT', 'SUCCESS', 5000.00, now() - INTERVAL '25 days'),
(2, 2000.00, 'DEPOSIT', 'SUCCESS', 2000.00, now() - INTERVAL '20 days'),
(3, 100.00,  'DEPOSIT', 'SUCCESS', 100.00,  now() - INTERVAL '5 days');

-- 3. Переводы - TRANSFER SUCCESS
INSERT INTO transactions (sender_account_id, receiver_account_id, amount, operation_type, status, balance_after_op, created_at)
VALUES (1, 2, 500.00, 'TRANSFER', 'SUCCESS', 4500.00, now() - INTERVAL '10 days');

INSERT INTO transactions (sender_account_id, receiver_account_id, amount, operation_type, status, balance_after_op, created_at)
VALUES (2, 3, 300.00, 'TRANSFER', 'SUCCESS', 1700.00, now() - INTERVAL '5 days');

INSERT INTO transactions (sender_account_id, receiver_account_id, amount, operation_type, status, balance_after_op, created_at)
VALUES
    (1, 3, 100.00, 'TRANSFER', 'SUCCESS', 4400.00, now() - INTERVAL '4 days'),
    (1, 5, 200.00, 'TRANSFER', 'SUCCESS', 4200.00, now() - INTERVAL '3 days'),
    (5, 1, 1000.00, 'TRANSFER', 'SUCCESS', 9000.00, now() - INTERVAL '2 days');

INSERT INTO transactions (sender_account_id, receiver_account_id, amount, operation_type, status, balance_after_op, created_at) VALUES
                                                                                                                                    (2, 1, 1000.00, 'TRANSFER', 'SUCCESS', 11000.00, '2026-03-01 10:00:00'),
                                                                                                                                    (1, 2, 200.00,  'TRANSFER', 'SUCCESS', 10800.00, '2026-03-01 12:30:00'),
                                                                                                                                    (1, 2, 150.00,  'TRANSFER', 'SUCCESS', 10650.00, '2026-03-01 15:45:00');
INSERT INTO transactions (receiver_account_id, amount, operation_type, status, balance_after_op, created_at) VALUES
    (1, 5000.00, 'DEPOSIT', 'SUCCESS', 15650.00, '2026-03-02 09:00:00');

INSERT INTO transactions (sender_account_id, receiver_account_id, amount, operation_type, status, balance_after_op, created_at) VALUES
                                                                                                                                    (1, 2, 1000.00, 'TRANSFER', 'SUCCESS', 14650.00, '2026-03-02 11:00:00'),
                                                                                                                                    (1, 2, 1000.00, 'TRANSFER', 'SUCCESS', 13650.00, '2026-03-02 13:00:00'),
                                                                                                                                    (1, 2, 1000.00, 'TRANSFER', 'SUCCESS', 12650.00, '2026-03-02 15:00:00');

INSERT INTO transactions (sender_account_id, receiver_account_id, amount, operation_type, status, balance_after_op, created_at) VALUES
                                                                                                                                    (1, 2, 500.00, 'TRANSFER', 'SUCCESS', 12150.00, '2026-03-03 10:00:00'),
                                                                                                                                    (1, 2, 500.00, 'TRANSFER', 'SUCCESS', 11650.00, '2026-03-03 14:00:00'),
                                                                                                                                    (1, 2, 500.00, 'TRANSFER', 'SUCCESS', 11150.00, '2026-03-03 18:00:00');

INSERT INTO transactions (sender_account_id, receiver_account_id, amount, operation_type, status, balance_after_op, created_at) VALUES
    (1, 6, 100.00, 'TRANSFER', 'SUCCESS', 11050.00, '2026-03-04 08:00:00');


-- 4. Неудачные переводы - TRANSFER FAILED
-- Недостаточно средств на счете ...0003
INSERT INTO transactions (sender_account_id, receiver_account_id, amount, operation_type, status, failure_reason, balance_after_op, created_at)
VALUES (3, 1, 10000.00, 'TRANSFER', 'FAILED', 'Insufficient funds', 300.00, now() - INTERVAL '1 day');