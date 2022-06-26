DROP TABLE IF EXISTS users CASCADE;
DROP SEQUENCE IF EXISTS hibernate_sequence;
CREATE SEQUENCE hibernate_sequence start with 10001 increment by 1;

CREATE TABLE users
(
    id              bigint auto_increment primary key,
    nickname        varchar(10)  not null,
    email           varchar(300) not null unique,
    password        varchar(60)  not null,
    created_at      timestamp    not null default now(),
    updated_at      timestamp    not null default now(),
    following_count int          not null default 0,
    follower_count  int          not null default 0,
    city            varchar,
    street          varchar,
    detail          varchar,
    zipcode         varchar
);