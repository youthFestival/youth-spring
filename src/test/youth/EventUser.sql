create table EventUser
(
    id      int auto_increment
        primary key,
    userId  int not null,
    eventId int not null,
    constraint EventUser_Event_id_fk
        foreign key (eventId) references Event (id)
            on update cascade on delete cascade,
    constraint EventUser_User_id_fk
        foreign key (userId) references User (id)
            on update cascade on delete cascade
);

