create table FAQ
(
    id        int auto_increment
        primary key,
    title     varchar(255)                        not null,
    content   text                                not null,
    updatedAt timestamp default CURRENT_TIMESTAMP null
);

