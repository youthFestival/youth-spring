create table UniversityFestival
(
    id         int auto_increment
        primary key,
    univId     int null,
    festivalId int null,
    constraint UniversityFestival_ibfk_1
        foreign key (univId) references University (id)
            on delete cascade,
    constraint UniversityFestival_ibfk_2
        foreign key (festivalId) references Festival (id)
            on delete cascade
);

create index festivalId
    on UniversityFestival (festivalId);

create index univId
    on UniversityFestival (univId);

