-- use ecommerce_db2;

drop table if exists reservation;

create table reservation (
    reservation_id mediumint primary key auto_increment,
    user_id mediumint not null,
    status enum('ACTIVE', 'CONSUMED', 'CANCELLED') not null,
    expiration_datetime timestamp not null,
    created_datetime timestamp not null,
    created_by varchar(45) not null,
    last_updated_datetime timestamp not null,
    last_updated_by varchar(45) not null,
    foreign key(user_id) references user(user_id)
) auto_increment = 1000001;

select * from reservations;

-- truncate reservations;
-- alter table reservations auto_increment = 1000001;

-- desc reservations;
