create table Event
(
    id          int auto_increment
        primary key,
    category    enum ('디데이', '문의답변', '구독한 행사 이벤트', '좋아하는 아티스트 행사 알림', '기대평 대댓글') not null,
    content     varchar(255)                                                     not null,
    redirectUrl varchar(255)                                                     not null,
    isChecked   tinyint(1) default 0                                             null,
    createdAt   timestamp  default CURRENT_TIMESTAMP                             null
);

