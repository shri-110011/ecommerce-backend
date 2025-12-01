-- use ecommerce_db;

create table product_price_history (
    history_id mediumint primary key auto_increment,
    product_id mediumint not null,
    old_price decimal(8,2),
    new_price decimal(8,2) not null,
    current_price_version smallint not null,
    created_datetime timestamp default current_timestamp not null,
    created_by varchar(45) not null,
    foreign key(product_id) references product(product_id),
    constraint uq_prod_id_cur_price_ver unique(product_id, current_price_version)
)auto_increment = 1000001;

insert into product_price_history(product_id, old_price, new_price, 
current_price_version, created_by)
select product_id, null, price, price_version, created_by from product;

select * from product_price_history;

-- select product_id, null, null, price, price_version from products;

-- desc product_price_history;
