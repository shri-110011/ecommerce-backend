-- use ecommerce_db2;

drop table if exists inventory_event_log;

create table inventory_event_log (
    event_id mediumint primary key auto_increment,
    product_id mediumint not null,
    event_type enum('RESTOCK', 'SALE', 'ADJUSTMENT') not null,
    quantity int not null,
    created_datetime timestamp default current_timestamp not null,
    created_by varchar(45) not null,
    reason varchar(45) default null,
    foreign key(product_id) references product(product_id)
) auto_increment = 1000001;

select * from inventory_event_log;

-- truncate inventory_events_log;
-- alter table inventory_events_log auto_increment = 1000001;

-- SET FOREIGN_KEY_CHECKS = 1;

-- desc inventory_events_log;

-- insert into inventory_events_log(product_id, change_type, quantity, status) values (1000005, 'RESERVATION', 1, 'ACTIVE');