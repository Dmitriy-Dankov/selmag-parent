create schema if not exists user_management;

create table user_management.t_user (
    id serial primary key,
    c_username varchar(50) not null unique check ( length(trim(c_username)) > 4 ),
    c_password varchar(50) not null
);

create table user_management.t_authority (
    id serial primary key,
    c_authority varchar(50) not null unique check ( length(trim(c_authority)) > 3 )
);

create table user_management.user_authority (
    id serial primary key,
    id_user int not null references user_management.t_user(id),
    id_authority int not null references user_management.t_authority(id),
    constraint uk_user_authority unique (id_user, id_authority)
);