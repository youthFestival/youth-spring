create table Festival
(
    id            int auto_increment
        primary key,
    name          varchar(255)          not null,
    startDate     timestamp             null,
    endDate       timestamp             null,
    geoLocationId int                   null,
    description   text                  null,
    categories    enum ('대학축제', '페스티벌') not null,
    organizer     varchar(255)          null,
    viewCount     int default 0         not null,
    locality      varchar(255)          null,
    tel           varchar(50)           null,
    minAge        int                   null,
    ticketPrice   int                   null,
    ticketUrl     varchar(255)          null,
    ticketOpen    timestamp             null,
    ticketClose   timestamp             null,
    stageOpen     time                  null,
    stageClose    time                  null,
    organizerUrl  varchar(255)          null,
    univercityId  int                   null,
    constraint Festival_ibfk_1
        foreign key (geoLocationId) references Geolocation (id)
            on delete cascade,
    constraint univercityId
        foreign key (univercityId) references University (id)
);

create index geoLocationId
    on Festival (geoLocationId);

