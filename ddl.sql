-- CREATE TABLE BOOKS (
--     book_id int NOT NULL,
--     ISBN varchar(20) NOT NULL,
--     book_title varchar(255) NOT NULL,
--     book_author varchar(255) NOT NULL,
--     chapters int NOT NULL,
--     pages int NOT NULL,
--     PRIMARY KEY (book_id)
-- ); 

-- CREATE TABLE USERS (
--     user_id int NOT NULL,
--     username varchar(100) NOT NULL,
--     PRIMARY KEY (user_id)
-- ); 

-- CREATE TABLE UBASSOCIATION (
--     book_id int NOT NULL,
--     user_id int NOT NULL,
--     progress int NOT NULL,
--     current_chapter int NOT NULL,
--     PRIMARY KEY (book_id, user_id),
--     FOREIGN KEY (book_id) REFERENCES BOOKS(book_id),
--     FOREIGN KEY (user_id) REFERENCES USERS(user_id)
-- ); 

--DROP TABLE UBASSOCIATION;
--DROP TABLE BOOKS;
--DROP TABLE USERS;
