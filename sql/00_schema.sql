DROP TABLE IF EXISTS Customer;
CREATE TABLE Customer (
    id int,
    first_name varchar(255),
    last_name varchar(255),
    address varchar(255),
    city varchar(255)
);

DROP TABLE IF EXISTS Product;
CREATE TABLE Product (
    id int,
    description varchar(255),
    weight float8
);

DROP TABLE IF EXISTS Shipping;
CREATE TABLE Shipping (
    id int,
    customer_id int,
    state varchar(255),
    send_date DATE,
    arrive_date DATE,
    priority int
);

DROP TABLE IF EXISTS Shipping_item;
CREATE TABLE Shipping_item (
    id int,
    shipping_id int,
    product_id int,
    product_count int
);

DROP TABLE IF EXISTS Task_shipping;
CREATE TABLE Task_shipping (
    id serial not null,
    shipping_id int,
    start_date timestamp,
    end_date timestamp,
    status varchar(255),
    message text,
   primary key (id)
);