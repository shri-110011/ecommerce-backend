-- use ecommerce_db2;

drop table if exists order_item;

create table order_item (
    order_id mediumint,
    product_id mediumint,
    quantity tinyint not null,
    price_at_purchase decimal(8, 2) not null,
    price_version smallint not null,
    primary key (order_id, product_id),
    foreign key(order_id) references `order`(order_id),
    foreign key(product_id) references product(product_id)
);

select * from order_item;
