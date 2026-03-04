CREATE TABLE IF NOT EXISTS transactions(
    id BIGSERIAL PRIMARY KEY,
    sender_account_id BIGINT REFERENCES accounts(id),
    receiver_account_id BIGINT REFERENCES accounts(id),
    amount NUMERIC(15, 2) NOT NULL,
    operation_type VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    failure_reason VARCHAR(255),
    balance_after_op NUMERIC(15, 2),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);