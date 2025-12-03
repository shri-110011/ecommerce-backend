-- use ecommerce_db2;

drop table if exists product;

create table product (
    product_id mediumint primary key auto_increment,
    product_name varchar(45) not null,
    price decimal(8, 2) not null,
    category_id tinyint not null,
    price_version smallint default 1 not null,
    is_active char(1) not null,
    created_datetime timestamp not null,
    created_by varchar(45) not null,
    last_updated_datetime timestamp not null,
    last_updated_by varchar(45) not null,
    foreign key(category_id) references category(category_id)
) auto_increment = 1000001;

insert into product 
(product_name, category_id, price, is_active, 
created_datetime, created_by, last_updated_datetime, 
last_updated_by)
values
('Laptop', 11, 53499, 'Y', '2025-02-10 10:00:00', 'ADMIN', '2025-02-10 10:00:00', 'ADMIN'),
('Smartphone', 11, 12999, 'Y', '2025-02-12 09:45:00', 'ADMIN', '2025-02-12 09:45:00', 'ADMIN'),
('Smartwatch', 11, 2999, 'Y', '2025-03-01 13:20:00', 'ADMIN', '2025-03-01 13:20:00', 'ADMIN'),
('Wireless Buds', 11, 2599, 'Y', '2025-03-05 11:30:00', 'ADMIN', '2025-03-05 11:30:00', 'ADMIN'),
('Men''s T-shirt', 12, 799, 'Y', '2025-01-22 16:00:00', 'ADMIN', '2025-01-22 16:00:00', 'ADMIN'),
('Jeans', 12, 1299, 'Y', '2025-02-14 14:50:00', 'ADMIN', '2025-02-14 14:50:00', 'ADMIN'),
('Perfume', 13, 300, 'Y', '2025-01-29 10:10:00', 'ADMIN', '2025-01-29 10:10:00', 'ADMIN'),
('Moisturizer', 13, 120, 'Y', '2025-02-03 12:45:00', 'ADMIN', '2025-02-03 12:45:00', 'ADMIN'),
('Cookware Set', 14, 6999, 'Y', '2025-04-01 09:25:00', 'ADMIN', '2025-04-01 09:25:00', 'ADMIN'),
('Dining Table', 14, 4000, 'Y', '2025-04-10 15:40:00', 'ADMIN', '2025-04-10 15:40:00', 'ADMIN'),
('Bicycle', 15, 7900, 'Y', '2025-05-02 08:10:00', 'ADMIN', '2025-05-02 08:10:00', 'ADMIN'),
('Football', 15, 1000, 'Y', '2025-03-18 11:15:00', 'ADMIN', '2025-03-18 11:15:00', 'ADMIN'),
('Sony PlayStation-5', 17, 53666, 'Y', '2025-01-27 17:55:00', 'ADMIN', '2025-01-27 17:55:00', 'ADMIN'),
('Mother Dairy Paneer, 200g', 18, 93, 'Y', '2025-02-20 14:00:00', 'ADMIN', '2025-02-20 14:00:00', 'ADMIN'),
('Mother Dairy Curd, 400ml', 18, 50, 'Y', '2025-02-22 09:35:00', 'ADMIN', '2025-02-22 09:35:00', 'ADMIN'),
('Amul Kesar Milk, 100g', 18, 20, 'Y', '2025-03-02 16:10:00', 'ADMIN', '2025-03-02 16:10:00', 'ADMIN');

select * from product;

-- desc product;
