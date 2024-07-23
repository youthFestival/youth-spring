create table Artist
(
    id      int auto_increment
        primary key,
    name    varchar(255) not null,
    imageId int          null,
    constraint Artist_ibfk_1
        foreign key (imageId) references Image (id)
            on delete cascade
);

create index imageId
    on Artist (imageId);

