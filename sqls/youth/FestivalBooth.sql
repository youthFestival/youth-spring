create table FestivalBooth
(
    id         int auto_increment
        primary key,
    festivalId int null,
    boothId    int null,
    constraint FestivalBooth_ibfk_1
        foreign key (boothId) references Booth (id)
            on delete cascade,
    constraint FestivalBooth_ibfk_2
        foreign key (festivalId) references Festival (id)
            on delete cascade
);

create index boothId
    on FestivalBooth (boothId);

create index festivalId
    on FestivalBooth (festivalId);

