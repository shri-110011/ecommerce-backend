-- use ecommerce_db2;

drop table if exists product_event_outbox;

create table product_event_outbox (
    event_id mediumint primary key auto_increment,
    event_type enum('PRODUCT_ADDED') not null,
    product_id mediumint not null,
    is_processed char(1) not null,
    retry_count smallint not null,
    next_retry_at timestamp not null,
    created_datetime timestamp not null,
    created_by varchar(45) not null,
    last_updated_datetime timestamp not null,
    last_updated_by varchar(45) not null,
    foreign key(product_id) references product(product_id)
)auto_increment = 1000001;

select * from product_event_outbox;

-- desc product_event_outbox;
