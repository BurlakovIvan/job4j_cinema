CREATE TABLE IF NOT EXISTS sessions (
  id SERIAL PRIMARY KEY,
  name VARCHAR,
  movie_id INT NOT NULL REFERENCES movies(id)
);

COMMENT ON TABLE sessions IS 'Сеансы';
COMMENT ON COLUMN sessions.id IS 'Идентификатор сеанса';
COMMENT ON COLUMN sessions.name IS 'Название сеанса';
COMMENT ON COLUMN sessions.movie_id IS 'Идентификатор фильма, в этом сеансе';