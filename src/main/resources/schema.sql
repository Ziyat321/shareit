drop table if exists comments cascade;
drop table if exists bookings cascade;
drop table if exists items cascade;
drop table if exists users cascade;
drop table if exists requests cascade;

create table if not exists users
(
    id    bigserial primary key,
    name  varchar not null,
    email varchar not null unique
);

create table if not exists requests
(
    id          bigserial primary key,
    description varchar                  not null,
    created     timestamp without time zone not null,
    owner_id    int8                     not null,
    foreign key (owner_id) references users (id)
);

create table if not exists items
(
    id           bigserial primary key,
    name         varchar not null,
    description  varchar not null,
    is_available boolean not null,
    owner_id     int8    not null,
    request_id   int8,
    foreign key (owner_id) references users (id),
    foreign key (request_id) references requests (id)
);

create table if not exists bookings
(
    id         bigserial primary key,
    start_date timestamp without time zone not null,
    end_date   timestamp without time zone not null,
    item_id    int8                        not null,
    booker_id  int8                        not null,
    status     int                         not null,
    foreign key (item_id) references items (id),
    foreign key (booker_id) references users (id)
);

create table if not exists comments
(
    id        bigserial primary key,
    text      varchar                     not null,
    created   timestamp without time zone not null,
    item_id   int8                        not null,
    author_id int8                        not null,
    foreign key (item_id) references items (id),
    foreign key (author_id) references users (id)
);

