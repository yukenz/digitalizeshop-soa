CREATE USER 'yukenz'@'%' IDENTIFIED WITH caching_sha2_password BY 'awan123';

select *
from user;

GRANT ALL PRIVILEGES ON *.* TO 'yukenz'@'%' WITH GRANT OPTION;
FLUSH PRIVILEGES;

SHOW GRANTS FOR 'yukenz'@'%';

show tables;

create database digitalized_shop collate utf8mb3_general_mysql500_ci;
create database digitalized_shop2 collate utf8mb3_general_mysql500_ci;


CREATE TABLE `backend`
(
    `id`     varchar(100) primary key ,
    `secret` varchar(100)
);

select *
from backend;
insert into backend values (UUID(),UUID_SHORT());

select UUID(),UUID_SHORT()
from dual;

CREATE TABLE `seller`
(
    `id`       bigint PRIMARY KEY,
    `name`     varchar(100),
    `username` varchar(100),
    `password` varchar(100)
);

CREATE TABLE `product`
(
    `id`          bigint PRIMARY KEY,
    `seller_id`   bigint,
    `name`        varchar(100),
    `price`       bigint,
    `description` varchar(100),
    `image_uri`   varchar(100),
    `discount`    float,
    `category`    varchar(100),
    `isAvailable` bool
);

CREATE TABLE `transaction`
(
    `id`            bigint PRIMARY KEY,
    `seller_id`     bigint,
    `status`        int,
    `customer_uuid` varchar(100),
    `customer_name` varchar(100)
);

CREATE TABLE `order_book`
(
    `id`          bigint PRIMARY KEY,
    `trx_id`      bigint,
    `product_id`  int,
    `product_qty` int
);

CREATE TABLE `transaction_history`
(
    `transaction_id` bigint PRIMARY KEY,
    `seller_id`      bigint,
    `seller_name`    varchar(100),
    `customer_uuid`  varchar(100),
    `customer_name`  varchar(100),
    `status`         int
);

CREATE TABLE `order_history`
(
    `order_id`       bigint PRIMARY KEY,
    `transaction_id` bigint,
    `product_id`     int,
    `product_name`   int,
    `product_qty`    int
);

ALTER TABLE `product`
    ADD FOREIGN KEY (`seller_id`) REFERENCES `seller` (`id`);

ALTER TABLE `transaction`
    ADD FOREIGN KEY (`seller_id`) REFERENCES `seller` (`id`);

ALTER TABLE `order_book`
    ADD FOREIGN KEY (`trx_id`) REFERENCES `transaction` (`id`);

ALTER TABLE `order_book`
    ADD FOREIGN KEY (`product_id`) REFERENCES `product` (`id`);

ALTER TABLE `order_history`
    ADD FOREIGN KEY (`transaction_id`) REFERENCES `transaction_history` (`transaction_id`);

show create table seller;

DELIMITER $$
DROP PROCEDURE IF EXISTS authSeller;
CREATE PROCEDURE authSeller(
    IN usernameIn VARCHAR(100),
    IN passwordIn VARCHAR(100),
    OUT isValid BOOL
)
BEGIN

select IF(password = SHA2(passwordIn, 256), true, false)
into isValid
from seller
where username = usernameIn;

select ifnull(isValid, false) into isValid from dual;

END$$
DELIMITER ;

select ifnull(null, 'ada value')
from dual;


insert into seller (id, name, username, password)
values (0, 'CV AWAN INDO2', 'yukenz', '123');

set @usernameIn = 'yukenz';
set @passwordIn = 'awan123';
call authSeller(
        @usernameIn,
        @passwordIn,
        @isValid
     );
select @isValid;

select IF(password = '123', true, false)
from seller
where username = 'yukenz';

select *
from seller;

update seller set password=SHA2('awan123', 256) where username = 'yukenz';


DELIMITER $$
DROP PROCEDURE IF EXISTS allSeller;
CREATE PROCEDURE allSeller(
)
BEGIN

select * from seller;

END$$
DELIMITER ;

call allSeller();