create table FestivalArtist
(
    id         int auto_increment
        primary key,
    festivalId int null,
    artistId   int null,
    constraint FestivalArtist_ibfk_1
        foreign key (festivalId) references Festival (id)
            on delete cascade,
    constraint FestivalArtist_ibfk_2
        foreign key (artistId) references Artist (id)
            on delete cascade
);

create index artistId
    on FestivalArtist (artistId);

create index festivalId
    on FestivalArtist (festivalId);

