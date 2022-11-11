CREATE TABLE IF NOT EXISTS tickets (
    id SERIAL PRIMARY KEY,
    session_id INT NOT NULL REFERENCES sessions(id),
    pos_row INT NOT NULL,
    cell INT NOT NULL,
    user_id INT REFERENCES users(id),
    unique (session_id, pos_row, cell)
);

COMMENT ON TABLE tickets IS 'Билеты';
COMMENT ON COLUMN tickets.id IS 'Идентификатор билета';
COMMENT ON COLUMN tickets.session_id IS 'Идентификатор сеанса';
COMMENT ON COLUMN tickets.pos_row IS 'Ряд';
COMMENT ON COLUMN tickets.cell IS 'Место';
COMMENT ON COLUMN tickets.user_id IS 'Идентификатор пользователя, купивший билет';