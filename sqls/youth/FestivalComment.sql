create table FestivalComment
(
    id         int auto_increment
        primary key,
    category   enum ('QnA', '기대평') not null,
    commentId  int                 null,
    festivalId int                 null,
    constraint FestivalComment_ibfk_1
        foreign key (festivalId) references Festival (id),
    constraint FestivalComment_ibfk_2
        foreign key (commentId) references Comment (id)
);

create index commentId
    on FestivalComment (commentId);

create index festivalId
    on FestivalComment (festivalId);

