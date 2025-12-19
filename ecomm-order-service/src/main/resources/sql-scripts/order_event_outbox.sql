-- use ecommerce_db2;

drop table if exists order_event_outbox;

create table order_event_outbox (
    event_id mediumint primary key auto_increment,
    event_type enum('ORDER_CREATED', 'ORDER_CANCELLED') not null,
    order_id mediumint not null,
    is_processed char(1) not null,
    retry_count smallint not null,
    next_retry_at timestamp not null,
    created_datetime timestamp not null,
    created_by varchar(45) not null,
    last_updated_datetime timestamp not null,
    last_updated_by varchar(45) not null,
    foreign key(order_id) references `order`(order_id)
)auto_increment = 1000001;

select * from order_event_outbox;

-- desc order_event_outbox;
