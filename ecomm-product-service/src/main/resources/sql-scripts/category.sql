-- use ecommerce_db2;

drop table if exists category;

create table category (
    category_id tinyint primary key auto_increment,
    category_name varchar(45) not null
) auto_increment = 11;

insert into category(category_name) values ('Electronics'), ('Fashion'), 
('Health and Beauty'), ('Home and Garden'), ('Sports and Outdoors'), 
('Books and Media'), ('Toys and Games'), ('Groceries');

select * from category;

-- desc category;
