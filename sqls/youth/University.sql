create table University
(
    id            int auto_increment
        primary key,
    name          varchar(50) not null,
    campus        varchar(50) null,
    geoLocationId int         null,
    logoId        int         null,
    constraint University_ibfk_1
        foreign key (geoLocationId) references Geolocation (id)
            on delete cascade,
    constraint University_ibfk_2
        foreign key (logoId) references Image (id)
            on delete cascade
);

create index Universities_ibfk_1
    on University (geoLocationId);

create index logoId
    on University (logoId);

