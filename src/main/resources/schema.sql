CREATE TABLE IF NOT EXISTS Customers
(
    id      VARCHAR(20) PRIMARY KEY,
    name    VARCHAR(100) NOT NULL,
    address VARCHAR(300) NOT NULL
);

CREATE TABLE IF NOT EXISTS Teachers
(
    id      VARCHAR(20) PRIMARY KEY,
    name    VARCHAR(100) NOT NULL,
    address VARCHAR(300) NOT NULL
);
CREATE TABLE IF NOT EXISTS Students
(
    id      VARCHAR(20) PRIMARY KEY,
    name    VARCHAR(100) NOT NULL,
    address VARCHAR(300) NOT NULL
);
CREATE TABLE IF NOT EXISTS Employees
(
    id      VARCHAR(20) PRIMARY KEY,
    name    VARCHAR(100) NOT NULL,
    address VARCHAR(300) NOT NULL
);
