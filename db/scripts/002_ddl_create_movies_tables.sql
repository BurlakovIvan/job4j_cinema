CREATE TABLE IF NOT EXISTS movies (
   id SERIAL PRIMARY KEY,
   name VARCHAR NOT NULL,
   description VARCHAR,
   created TIMESTAMP DEFAULT now(),
   photo bytea,
   country_id INT NOT NULL REFERENCES countries(id)
);

COMMENT ON TABLE movies IS 'Фильмы';
COMMENT ON COLUMN movies.id IS 'Идентификатор фильма';
COMMENT ON COLUMN movies.name IS 'Название фильма';
COMMENT ON COLUMN movies.description IS 'Описание фильма';
COMMENT ON COLUMN movies.created IS 'Дата премьеры фильма';
COMMENT ON COLUMN movies.photo IS 'Постер фильма';
COMMENT ON COLUMN movies.country_id IS 'Идентификатор страны производства фильма';