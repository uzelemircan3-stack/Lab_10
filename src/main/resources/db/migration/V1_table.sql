CREATE TABLE users (
                       id INTEGER PRIMARY KEY AUTOINCREMENT,
                       username VARCHAR(20) NOT NULL,
                       email VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(100) NOT NULL
);