create table EmailVerificationToken
(
    id        int auto_increment
        primary key,
    userId    int                                 not null,
    token     varchar(255)                        not null,
    createdAt timestamp default CURRENT_TIMESTAMP null,
    expiresAt timestamp                           not null,
    constraint token
        unique (token),
    constraint EmailVerificationToken_ibfk_1
        foreign key (userId) references User (id)
            on delete cascade
);

create index userId
    on EmailVerificationToken (userId);

