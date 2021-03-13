DROP TABLE IF EXISTS score;

CREATE TABLE IF NOT EXISTS score(
id INT AUTO_INCREMENT,
player VARCHAR(50) NOT NULL,
score INT NOT NULL,
published_date DATETIME NOT NULL,
PRIMARY KEY(id)
);