-- up
ALTER TABLE posts ADD content LONGTEXT;
ALTER TABLE posts ADD mail VARCHAR(50);
-- down
ALTER TABLE posts DROP COLUMN content;
ALTER TABLE posts DROP COLUMN mail;