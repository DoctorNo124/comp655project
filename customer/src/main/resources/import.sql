
DROP TABLE IF EXISTS customers CASCADE;

CREATE TABLE customers (
                           balance FLOAT(53) NOT NULL,
                           id BIGSERIAL NOT NULL,
                           email VARCHAR(255),
                           name VARCHAR(255),
                           PRIMARY KEY (id)
);

/*insert 10 customers into the customers table*/

INSERT INTO customers (balance, email, name) VALUES (100.0, 'nogina@gmail.com', 'Irina Nogina');
INSERT INTO customers (balance, email, name) VALUES (200.0, 'smith@gmail.com', 'Michael Smith');
INSERT INTO customers (balance, email, name) VALUES (150.5, 'jones@gmail.com', 'Kathy Jones');
INSERT INTO customers (balance, email, name) VALUES (180.75, 'parsons@gmail.com', 'Joe Parsons');
INSERT INTO customers (balance, email, name) VALUES (500.0, 'curl@gmail.com', 'Adrianna Curl');
INSERT INTO customers (balance, email, name) VALUES (300.0, 'spilberg@gmail.com', 'Steven Spilberg');
INSERT INTO customers (balance, email, name) VALUES (220.0, 'jjones@gmail.com', 'Joe Jones');
INSERT INTO customers (balance, email, name) VALUES (400.0, 'arov8@gmail.com', 'Michael Arov');
INSERT INTO customers (balance, email, name) VALUES (350.0, 'jsmith@gmail.com', 'Julia Smith');
INSERT INTO customers (balance, email, name) VALUES (280.0, 'joker@gmail.com', 'Steve Joker');
