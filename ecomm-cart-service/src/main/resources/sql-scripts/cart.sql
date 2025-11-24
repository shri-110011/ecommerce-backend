use ecommerce_db2;

drop table if exists cart;

create table cart (
    cart_id mediumint primary key auto_increment,
    user_id mediumint not null,
    order_id mediumint default null,
    created_datetime timestamp default current_timestamp,
    expiration_datetime timestamp default (current_timestamp 
    + interval 2880 minute),
    last_updated_datetime timestamp not null,
    status enum('ACTIVE','EXPIRED','CLOSED') not null
) auto_increment = 1000001;

select * from cart;

-- desc cart;
