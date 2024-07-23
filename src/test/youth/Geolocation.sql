create table Geolocation
(
    id        int auto_increment
        primary key,
    name      varchar(50)  not null,
    detail    varchar(255) null,
    latitude  float        null,
    longitude float        null
);

