create table Inquiry
(
    id        int auto_increment
        primary key,
    category  enum ('질문', '기타', '답변')                                not null,
    title     varchar(255)                                           not null,
    content   text                                                   not null,
    authorId  int                                                    not null,
    updatedAt timestamp                    default CURRENT_TIMESTAMP null,
    status    enum ('접수중', '접수완료', '답변완료') default '접수중'             null,
    constraint authorId
        foreign key (id) references User (id)
            on update cascade on delete cascade
);

