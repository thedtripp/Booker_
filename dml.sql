INSERT INTO USERS (username)
VALUES  ('thedtripp'),
        ('ronald'),
        ('branty'),
        ('ronald'),
        ('joseph'),
        ('anel'),
        ('rick')
; 
SELECT * FROM USERS;
INSERT INTO BOOKS (ISBN, book_title, book_author, chapters, pages)
VALUES 
    (   
        '978-0136042594', 
        'Artificial Intelligence: A Modern Approach 3rd Edition', 
        'Stuart Russell', 
        27, 
        1151
    ),    
    (   
        '978-1119056447', 
        'Big Java: Early Objects, 6th Edition', 
        'Stuart Russell', 
        26, 
        1444
    ),
    (   
        '978-0262033848', 
        'Introduction to Algorithms, 3rd Edition (The MIT Press)', 
        'Stuart Russell', 
        35, 
        1313
    );

--SELECT * FROM BOOKS;
SELECT book_id, '', substr(book_title, 0, 32), chapters, pages FROM BOOKS;

INSERT INTO UBASSOCIATIONS (book_id, user_id, progress, current_chapter)
VALUES 
    --1 ('thedtripp')
    (1, 1, 1, 1),
    (2, 1, 2, 24),
    (3, 1, 3, 34),
    --2 ('ronald')
    (2, 2, 4, 17),
    --3 ('branty')
    (1, 3, 5, 16),
    (2, 3, 6, 15),
    (3, 3, 7, 14),
    --4 ('ronald')
    (2, 4, 8, 13),
    (3, 4, 9, 12),
    --5 ('joseph')
    (1, 5, 10, 11),
    (2, 5, 11, 10),
    (3, 5, 12, 9),
    --6 ('anel')
    (1, 6, 13, 8),
    (3, 6, 14, 7),
    --7 ('rick')
    (1, 7, 15, 6),
    (2, 7, 16, 5);


select substr(username || '          ', 1, 10), substr(book_title, 1, 32), progress, current_chapter from UBASSOCIATIONS natural join books natural join users;
--select * from UBASSOCIATION natural join books natural join users;
