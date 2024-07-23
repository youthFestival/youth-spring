create table Image
(
    id         int auto_increment
        primary key,
    detail     varchar(255)                                                                   not null,
    imgUrl     varchar(255)                                                                   null,
    categories enum ('행사_사진', '구조도', '포스터', '행사_정보', '대학로고', '부스', '기타', '아티스트') default '기타' null
);

