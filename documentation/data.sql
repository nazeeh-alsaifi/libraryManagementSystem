-- INSERT INTO MY_USER(
--     ID,
--     USERNAME,
--     PASSWORD,
--     USER_ROLE
-- ) VALUES (
--     1,
--     'nazeeh alsaifi',
--     'test',
--     'ADMIN'
-- );

INSERT INTO BOOK (
    ID,
    TITLE,
    AUTHOR,
    PUBLICATION_YEAR,
    ISBN
) VALUES (
    1,
    'Clean Code: A Handbook of Agile Software Craftsmanship',
    'Robert C. Martin',
    '2008',
    '9780132350884'
),
(
    2,
    'Patterns of Enterprise Application Architecture',
    'Martin Fowler',
    '2012',
    '9780321127426'
);

INSERT INTO PATRON (
    ID,
    NAME,
    CONTACT_INFORMATION
) VALUES (
    1,
    'Peter Smith',
    '9780132350884'
),
(
    2,
    'Noura Hashem',
    '9780321127426'
);

-- INSERT INTO BORROWING_RECORD (
--     ID,
--     BORROWING_DATE,
--     RETURN_DATE,
--     BOOK_ID,
--     PATRON_ID
-- ) VALUES (
--     1,
--     CURRENT_DATE(),
--     CURRENT_DATE()+4,
--     1,
--     1
-- ),
-- (
--     2,
--     CURRENT_DATE()+5,
--     CURRENT_DATE()+7,
--     1,
--     2
-- ),
-- (
--     3,
--     CURRENT_DATE()+8,
--     CURRENT_DATE()+10,
--     1,
--     1
-- )
-- ;