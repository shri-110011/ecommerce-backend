-- use ecommerce_db2;

drop table if exists `order`;

create table `order` (
    order_id mediumint primary key auto_increment,
    user_id mediumint not null,
    reservation_id mediumint not null,
    total_amount decimal(9, 2) not null,
    status enum('CONFIRMED', 'PROCESSING', 'PACKAGED', 'SHIPPED',
    'DELIVERED', 'CANCELLED') not null,
    created_datetime timestamp not null,
    last_updated_datetime timestamp not null,
    foreign key (user_id) references `user`(user_id)
) auto_increment = 1000001;

alter table `order` 
add constraint chk_total_amount check(total_amount > 0);

select * from `order`;
