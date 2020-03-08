INSERT INTO users (username, password)
VALUES 
    ('member1', '123'),
    ('member2', 'abc');

INSERT INTO artifacts (title, media_type)
VALUES 
    ('the lord of the rings', 'book'),
    ('the shawshank redemption', 'film'),
    ('1984', 'book'),
    ('harry potter', 'book'),
    ('carrying the fire', 'book'),
    ('star wars', 'film'),
    ('intro to cooking', 'book'),
    ('the c programming language', 'book'),
    ('bunreacht na heireann', 'book'),
    ('airplane!', 'film');

INSERT INTO loans (artifact_id, return_date, user_id)
VALUES 
    (1, 20200310, 1);
