create table User
(
    id           int auto_increment
        primary key,
    userId       varchar(50)                                      null,
    password     varchar(255)                                     not null,
    email        varchar(50)                                      null,
    createdAt    timestamp              default CURRENT_TIMESTAMP null,
    gender       enum ('남성', '여성')                                null,
    username     varchar(50)                                      not null,
    tel          varchar(50)                                      null,
    address      varchar(255)                                     not null,
    isAdmin      enum ('admin', 'user') default 'user'            not null,
    isAllowEmail tinyint(1)             default 0                 not null,
    constraint userId_UNIQUE
        unique (userId)
);

