insert into Customer (id, first_name, last_name, address, city)
            values (1, 'Marcos', 'Gutierrez', 'la buena direccion 123', 'CABA');
insert into Customer (id, first_name, last_name, address, city)
            values (2, 'Hernan', 'Toledo', 'falsa 456', 'CABA');
insert into Customer (id, first_name, last_name, address, city)
            values (3, 'Silvina', 'Hernandez', 'Marcos Paz 997', 'Buenos Aires');

insert into Product (id, description, weight) values (1, 'Maiz Pisingallo', 1.1);
insert into Product (id, description, weight) values (2, 'Tornillos', 2.2);
insert into Product (id, description, weight) values (3, 'Caja', 1.1);
insert into Product (id, description, weight) values (4, 'Modem', 0.9);
insert into Product (id, description, weight) values (5, 'Celular', 0.3);
insert into Product (id, description, weight) values (6, 'Tablet', 0.5);
insert into Product (id, description, weight) values (7, 'Petroleo', 1.5);
insert into Product (id, description, weight) values (8, 'Tizas', 0.6);
insert into Product (id, description, weight) values (9, 'Marcadores', 0.8);
insert into Product (id, description, weight) values (10, 'Mesada', 8.4);
insert into Product (id, description, weight) values (11, 'Marmol', 13.3);
insert into Product (id, description, weight) values (12, 'Puerta', 3.5);
insert into Product (id, description, weight) values (13, 'Cortina', 1.3);
insert into Product (id, description, weight) values (14, 'Televisor', 8.0);
insert into Product (id, description, weight) values (15, 'Colchon', 10.5);

insert into Shipping (id, customer_id, state, send_date, arrive_date, priority)
            values (1, 1, 'Entregado', CURRENT_DATE - 5, CURRENT_DATE, 0);
insert into Shipping_item (id, shipping_id, product_id, product_count) values (1, 1, 1, 5);
insert into Shipping_item (id, shipping_id, product_id, product_count) values (2, 1, 2, 2);
insert into Shipping_item (id, shipping_id, product_id, product_count) values (3, 1, 3, 1);

insert into Shipping (id, customer_id, state, send_date, arrive_date, priority)
            values (2, 2, 'En camino', CURRENT_DATE - 5, null, 2);
insert into Shipping_item (id, shipping_id, product_id, product_count) values (4, 2, 4, 6);
insert into Shipping_item (id, shipping_id, product_id, product_count) values (5, 2, 5, 2);
insert into Shipping_item (id, shipping_id, product_id, product_count) values (6, 2, 6, 2);

insert into Shipping (id, customer_id, state, send_date, arrive_date, priority)
            values (3, 1, 'Inicial', CURRENT_DATE, null, 2);
insert into Shipping_item (id, shipping_id, product_id, product_count) values (7, 3, 4, 1);
insert into Shipping_item (id, shipping_id, product_id, product_count) values (8, 3, 5, 2);

insert into Shipping (id, customer_id, state, send_date, arrive_date, priority)
            values (4, 3, 'Cancelado', CURRENT_DATE - 4, null, 2);
insert into Shipping_item (id, shipping_id, product_id, product_count) values (9, 4, 3, 1);

