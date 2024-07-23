create table ArtistUser
(
    id       int auto_increment
        primary key,
    artistId int not null,
    userId   int not null,
    constraint artistId
        foreign key (artistId) references Artist (id),
    constraint userId
        foreign key (userId) references User (id)
);

