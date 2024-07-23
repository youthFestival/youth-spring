create table Booth
(
    id       int auto_increment
        primary key,
    category enum ('먹거리', '체험', '플리마켓', '티켓', '굿즈', '기타') not null,
    name     varchar(255)                                 not null
);

