# 하루 마지막 커밋에 테이블 수정 반영

create table categories
(
    category_id   bigint auto_increment
        primary key,
    category_name varchar(255) not null,
    constraint category_name
        unique (category_name)
);

create table user
(
    id            bigint auto_increment
        primary key,
    userid        varchar(45)     not null,
    nickname      varchar(255)    not null,
    password      varchar(255)    not null,
    phonenum      varchar(255)    not null,
    sex           enum ('M', 'F') not null,
    birth         date            not null,
    SNS           varchar(45)     null,
    profilesrc    varchar(255)    null,
    email         varchar(100)    not null,
    suspend_until date            null,
    constraint idx_userid
        unique (userid)
);

create table refresh_token
(
    user_id         varchar(45)                         not null
        primary key,
    token           varchar(255)                        not null,
    expiration_date datetime                            not null,
    created_at      timestamp default CURRENT_TIMESTAMP null,
    constraint refresh_token_ibfk_1
        foreign key (user_id) references user (userid)
            on delete cascade
);

create table seller_item
(
    content_id  bigint auto_increment
        primary key,
    user_id     bigint not null,
    category_id bigint not null,
    summary     text   not null,
    constraint seller_item_ibfk_1
        foreign key (user_id) references user (id)
            on delete cascade,
    constraint seller_item_ibfk_2
        foreign key (category_id) references categories (category_id)
            on delete cascade
);

create index category_id
    on seller_item (category_id);

create index user_id
    on seller_item (user_id);

