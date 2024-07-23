create table Comment
(
    id         int auto_increment
        primary key,
    updatedAt  timestamp default CURRENT_TIMESTAMP null,
    content    text                                not null,
    userId     int                                 not null,
    festivalId int                                 not null,
    constraint Comment_ibfk_1
        foreign key (userId) references User (id)
            on delete cascade,
    constraint festivalId
        foreign key (festivalId) references Festival (id)
);

create index userId
    on Comment (userId);

