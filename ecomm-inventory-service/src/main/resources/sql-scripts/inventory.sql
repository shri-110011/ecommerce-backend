-- use ecommerce_db2;
-- use ecommerce_db2;

drop table if exists inventory;

create table inventory (
    inventory_id mediumint primary key auto_increment,
    product_id mediumint not null,
    actual_stock int not null,
    created_datetime timestamp default current_timestamp not null,
    created_by varchar(45) not null,
    last_updated_datetime timestamp default current_timestamp not null,
    last_updated_by varchar(45) not null,
    foreign key(product_id) references product(product_id)
)auto_increment = 1000001;

insert into inventory (product_id, actual_stock, created_by, last_updated_by)
values 
(1000001, 72, 'ADMIN', 'ADMIN'),
(1000002, 105, 'ADMIN', 'ADMIN'),
(1000003, 56, 'ADMIN', 'ADMIN'),
(1000004, 100, 'ADMIN', 'ADMIN'),
(1000005, 50, 'ADMIN', 'ADMIN'),
(1000006, 24, 'ADMIN', 'ADMIN'),
(1000007, 40, 'ADMIN', 'ADMIN'),
(1000008, 17, 'ADMIN', 'ADMIN'),
(1000009, 20, 'ADMIN', 'ADMIN'),
(1000010, 35, 'ADMIN', 'ADMIN'),
(1000011, 78, 'ADMIN', 'ADMIN'),
(1000012, 14, 'ADMIN', 'ADMIN'),
(1000013, 19, 'ADMIN', 'ADMIN'),
(1000014, 135, 'ADMIN', 'ADMIN'),
(1000015, 122, 'ADMIN', 'ADMIN'),
(1000016, 80, 'ADMIN', 'ADMIN');

select * from inventory;
