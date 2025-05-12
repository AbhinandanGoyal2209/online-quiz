CREATE DATABASE quiz_system;
USE quiz_system;

CREATE TABLE quiz_results (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    score INT NOT NULL,
    quiz_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


Select * from quiz_results;

