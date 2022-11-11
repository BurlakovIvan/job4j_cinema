CREATE TABLE IF NOT EXISTS countries (
   id SERIAL PRIMARY KEY,
   name VARCHAR NOT NULL UNIQUE
);

COMMENT ON TABLE countries IS 'Страны';
COMMENT ON COLUMN countries.id IS 'Идентификатор страны';
COMMENT ON COLUMN countries.name IS 'Название страны';