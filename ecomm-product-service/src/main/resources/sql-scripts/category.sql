-- use ecommerce_db2;

drop table if exists category;

create table category (
    category_id tinyint primary key auto_increment,
    category_name varchar(45) not null,
    is_active char(1) not null,
    created_datetime timestamp not null,
    created_by varchar(45) not null,
    last_updated_datetime timestamp not null,
    last_updated_by varchar(45) not null
) auto_increment = 11;

insert into category 
(category_name, is_active, created_datetime, created_by, last_updated_datetime, last_updated_by)
values
('Electronics', 'Y', '2025-02-10 10:15:00', 'ADMIN', '2025-02-10 10:15:00', 'ADMIN'),
('Fashion', 'Y', '2025-03-05 09:30:00', 'ADMIN', '2025-03-05 09:30:00', 'ADMIN'),
('Health and Beauty', 'Y', '2025-01-22 14:45:00', 'ADMIN', '2025-01-22 14:45:00', 'ADMIN'),
('Home and Garden', 'Y', '2025-04-14 11:20:00', 'ADMIN', '2025-04-14 11:20:00', 'ADMIN'),
('Sports and Outdoors', 'Y', '2025-05-02 08:10:00', 'ADMIN', '2025-05-02 08:10:00', 'ADMIN'),
('Books and Media', 'Y', '2025-02-28 16:05:00', 'ADMIN', '2025-02-28 16:05:00', 'ADMIN'),
('Toys and Games', 'Y', '2025-03-18 13:55:00', 'ADMIN', '2025-03-18 13:55:00', 'ADMIN'),
('Groceries', 'Y', '2025-01-30 17:25:00', 'ADMIN', '2025-01-30 17:25:00', 'ADMIN');

select * from category;

-- desc category;
