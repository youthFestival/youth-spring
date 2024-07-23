create table QnA
(
    id         int auto_increment
        primary key,
    questionId int null,
    answerId   int null,
    constraint QnA_ibfk_1
        foreign key (questionId) references Inquiry (id)
            on delete cascade,
    constraint QnA_ibfk_2
        foreign key (answerId) references Inquiry (id)
            on delete cascade
);

create index answerId
    on QnA (answerId);

create index questionId
    on QnA (questionId);

