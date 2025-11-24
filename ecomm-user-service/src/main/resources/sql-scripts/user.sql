-- drop database if exists ecommerce_db;

-- drop database ecommerce_db2;

create database if not exists ecommerce_db2;

use ecommerce_db2;

drop table if exists user;

create table user (
    user_id mediumint primary key auto_increment,
    user_name varchar(45) not null,
    email_id varchar(30) not null,
    is_active char(1) not null
) auto_increment = 1000001;

show index from user;
-- drop index email_id on user;

create unique index idx_uq_email_id
on user ((case when is_active = 'Y' then email_id else null end));

insert into user 
(user_name, email_id, is_active)
values
('John', 'john@gmail.com', 'Y'),
('Peter', 'peter@gmail.com', 'Y'),
('Billy', 'billy@gmail.com', 'N');

select * from user;

-- desc user;
