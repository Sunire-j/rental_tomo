create table categories
(
    category_id   bigint auto_increment
        primary key,
    category_name varchar(255) not null,
    parent_id     bigint       null,
    constraint category_name
        unique (category_name),
    constraint fk_parent
        foreign key (parent_id) references categories (category_id)
);

create table user
(
    id            bigint auto_increment
        primary key,
    userid        varchar(45)          not null,
    nickname      varchar(255)         not null,
    password      varchar(255)         not null,
    phonenum      varchar(255)         not null,
    sex           enum ('M', 'F')      not null,
    birth         date                 not null,
    SNS           varchar(45)          null,
    profilesrc    varchar(255)         null,
    email         varchar(100)         not null,
    suspend_until date                 null,
    is_seller     tinyint(1) default 0 not null,
    introduce     text                 null,
    constraint idx_userid
        unique (userid)
);

create table follow
(
    id       bigint auto_increment
        primary key,
    follower bigint not null,
    followed bigint not null,
    constraint unique_follow
        unique (follower, followed),
    constraint fk_followed
        foreign key (followed) references user (id)
            on delete cascade,
    constraint fk_follower
        foreign key (follower) references user (id)
            on delete cascade
);

create table refresh_token
(
    token_id        bigint auto_increment
        primary key,
    user_id         bigint                              null,
    token           varchar(255)                        not null,
    expiration_date datetime                            not null,
    created_at      timestamp default CURRENT_TIMESTAMP null,
    constraint refresh_token_ibfk_1
        foreign key (user_id) references user (id)
            on delete cascade
);

create table seller_item
(
    content_id  bigint auto_increment
        primary key,
    user_id     bigint               not null,
    category_id bigint               not null,
    summary     text                 null,
    status      tinyint(1) default 0 not null,
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

