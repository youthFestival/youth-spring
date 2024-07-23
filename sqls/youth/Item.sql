create table Item
(
    id      int auto_increment
        primary key,
    name    varchar(255) not null,
    price   int          null,
    keyword varchar(255) null,
    boothId int          null,
    constraint boothId
        foreign key (boothId) references Booth (id)
);

