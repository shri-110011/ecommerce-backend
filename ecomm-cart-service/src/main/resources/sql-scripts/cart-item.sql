use ecommerce_db2;

drop table if exists cart_item;

create table cart_item (
    cart_item_id mediumint primary key auto_increment,
    cart_id mediumint not null,
    product_id mediumint not null,
    quantity tinyint not null,
    foreign key(product_id) references products(product_id)
) auto_increment = 1000001;

select * from cart_item;

-- desc cart_item;
