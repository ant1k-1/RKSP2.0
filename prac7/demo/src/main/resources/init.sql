CREATE TABLE IF NOT EXISTS booksr (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255),
    author VARCHAR(255),
    isbn VARCHAR(255),
    publisher VARCHAR(255),
    year INTEGER,
);