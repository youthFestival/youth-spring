create table CommentLike
(
    id        int auto_increment
        primary key,
    userId    int null,
    commentId int null,
    constraint CommentLike_ibfk_1
        foreign key (userId) references User (id)
            on delete cascade,
    constraint CommentLike_ibfk_2
        foreign key (commentId) references Comment (id)
            on delete cascade
);

create index commentId
    on CommentLike (commentId);

create index userId
    on CommentLike (userId);

