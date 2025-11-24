-- use ecommerce_db2;

drop table if exists product;

create table product (
    product_id mediumint primary key auto_increment,
    product_name varchar(45) not null,
    price decimal(8, 2) not null,
    category_id tinyint not null,
    available_quantity int not null,
    price_version smallint default 1 not null,
    foreign key(category_id) references categories(category_id)
) auto_increment = 1000001;

insert into product(product_name, category_id, price, available_quantity) 
values 
('Laptop', 11, 53499, 72),
('Smartphone', 11, 12999, 105),
('Smartwatch', 11, 2999, 56),
('Wireless Buds', 11, 2599, 100),
('Men\'s T-shirt', 12, 799, 50),
('Jeans', 12, 1299, 24),
('Perfume', 13, 300, 40),
('Moisturizer', 13, 120, 17),
('Cookware Set', 14, 6999, 20),
('Dining Table', 14, 4000, 35),
('Bicycle', 15, 7900, 78),
('Football', 15, 1000, 14),
('Sony PlayStation-5', 17, 53666, 19),
('Mother Dairy Paneer, 200g', 18, 93, 135),
('Mother Dairy Curd, 400ml', 18, 50, 122),
('Amul Kesar Milk, 100g', 18, 20, 80);

select * from product;

-- desc product;
