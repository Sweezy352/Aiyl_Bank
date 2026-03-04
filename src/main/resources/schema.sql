SELECT accound_id, COUNT(*) AS total_transactions
FROM (
    SELECT sender_account_id AS accound_id FROM transactions
    WHERE created_at > now() - INTERVAL '1 month' AND sender_account_id IS NOT NULL UNION ALL
    SELECT receiver_account_id AS account_id FROM transactions
    WHERE created_at > now() - INTERVAL '1 month' AND receiver_account_id IS NOT NULL
     ) AS all_activity
GROUP BY accound_id
ORDER BY total_transactions DESC
LIMIT 5;


SELECT sum(amount) FROM transactions
                   WHERE operation_type = 'TRANSFER'
                     AND status = 'SUCCESS'
                     AND created_at BETWEEN '2026-02-04' AND '2026-03-05';


SELECT account_number, balance FROM accounts WHERE balance < 0;

--Данная индексация позволяет ускорить выполнение 2-го select запроса в десятки раз, так как база данных использует B-Tree поиск вместо полного обхода по таблице
CREATE INDEX index_transactions_type_status_created_at ON transactions (operation_type, status, created_at);