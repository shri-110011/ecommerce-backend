-- use ecommerce_db2;

drop table if exists inventory_event_reservation;

create table inventory_event_reservation (
    inventory_event_id mediumint primary key,
    reservation_id mediumint not null,
    product_id mediumint not null,
    quantity int not null,
    created_datetime timestamp not null,
    created_by varchar(45) not null,
    foreign key(inventory_event_id) references inventory_event_log(event_id),
    foreign key(reservation_id) references reservation(reservation_id),
    foreign key(product_id) references product(product_id)
);

select * from inventory_event_reservation;

-- desc inventory_event_reservation;


-- truncate inventory_events_log;
-- alter table inventory_events_log auto_increment = 1000001;

-- SET FOREIGN_KEY_CHECKS = 1;

-- desc inventory_events_log;

-- insert into inventory_events_log(product_id, change_type, quantity, status) values (1000005, 'RESERVATION', 1, 'ACTIVE');