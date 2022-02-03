CREATE TABLE myEvent
(
    Id BIGSERIAL PRIMARY KEY,
    Name VARCHAR(64),
    Date VARCHAR(64),
    City VARCHAR(64),
    Building VARCHAR(128)
);

CREATE TABLE myAccount
(
    Id BIGSERIAL PRIMARY KEY,
    UserName VARCHAR(64),
    Password VARCHAR(64)
);