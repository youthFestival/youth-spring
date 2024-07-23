create table FestivalImage
(
    id         int auto_increment
        primary key,
    imageId    int null,
    festivalId int null,
    constraint FestivalImage_ibfk_1
        foreign key (imageId) references Image (id)
            on delete cascade,
    constraint FestivalImage_ibfk_2
        foreign key (festivalId) references Festival (id)
            on delete cascade
);

create index festivalId
    on FestivalImage (festivalId);

create index imageId
    on FestivalImage (imageId);

