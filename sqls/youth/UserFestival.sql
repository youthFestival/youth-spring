create table UserFestival
(
    id         int auto_increment
        primary key,
    userId     int null,
    festivalId int null,
    constraint UserFestival_ibfk_1
        foreign key (userId) references User (id)
            on delete cascade,
    constraint UserFestival_ibfk_2
        foreign key (festivalId) references Festival (id)
            on delete cascade
);

create index festivalId
    on UserFestival (festivalId);

create index userId
    on UserFestival (userId);

