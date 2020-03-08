INSERT INTO users (username, password)
VALUES 
    ('member1', '123'),
    ('member2', 'abc');

INSERT INTO librarians (username, password)
VALUES
    ('lib1', '123'),
    ('lib2', 'abc');

INSERT INTO artifacts (title, media_type, in_library)
VALUES 
    ('the lord of the rings', 'book', 0),
    ('the shawshank redemption', 'film', 1),
    ('1984', 'book', 1),
    ('harry potter', 'book', 1),
    ('carrying the fire', 'book', 1),
    ('star wars', 'film', 1),
    ('intro to cooking', 'book', 1),
    ('the c programming language', 'book', 1),
    ('bunreacht na heireann', 'book', 1),
    ('airplane!', 'film', 1);

INSERT INTO loans (artifact_id, return_date, user_id)
VALUES 
    (1, 20200310, 1),
    (5, 20190101, 1);
