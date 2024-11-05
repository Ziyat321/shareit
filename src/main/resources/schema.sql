create table users
(
    id    serial8 primary key,
    name  varchar not null,
    email varchar not null unique
);

create table items
(
    id           serial8 primary key,
    name         varchar not null,
    description  varchar not null,
    is_available boolean not null,
    owner_id     int8    not null,
    foreign key (owner_id) references users (id)
);

create table bookings
(
    id         serial8 primary key,
    start_date timestamp without time zone not null,
    end_date timestamp without time zone not null,
    item_id int8 not null ,
    booker_id int8 not null ,
    status int not null,
    foreign key (item_id) references items(id),
    foreign key (booker_id) references users(id)
);

create table comments(
  id serial8 primary key,
  text varchar not null,
  created timestamp without time zone not null,
  item_id int8 not null,
  author_id int8 not null,
  foreign key (item_id) references items(id),
  foreign key (author_id) references users(id)
);