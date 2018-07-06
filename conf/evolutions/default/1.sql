-- Up
CREATE TABLE posts(
  id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY ,
  title VARCHAR(100) NOT NULL
);
INSERT INTO  posts(title) VALUES ("Title 1");
INSERT INTO  posts(title) VALUES ("Title 2");
INSERT INTO  posts(title) VALUES ("Title 3");

-- Down
DROP TABLE IF EXISTS posts;