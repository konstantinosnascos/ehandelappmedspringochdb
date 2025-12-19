insert into categories (name) values
    ('Elektronik'),
    ('Kläder'),
    ('Hem'),
    ('Sport'),
    ('Böcker');

insert into products (sku, name, description, price) values
    ('ELEC-001', 'Trådlösa Hörlurar', 'Bluetooth 5.0 med brusreducering', 899.00),
    ('ELEC-002', 'USB-C Laddare', 'Snabbladdare 65W', 349.00),
    ('ELEC-003', 'Powerbank', 'Portabel 20000mAh', 499.00),
    ('KLAD-001', 'T-shirt Svart', 'Ekologisk bomull', 199.00),
    ('KLAD-002', 'Jeans Slim', 'Stretch denim', 599.00),
    ('KLAD-003', 'Hoodie Grå', 'Mjuk fleece', 449.00),
    ('HEM-001', 'LED-lampa', 'Smart WiFi dimbar', 149.00),
    ('HEM-002', 'Kaffebryggare', 'Programmerbar 12 koppar', 799.00),
    ('SPRT-001', 'Yogamatta', 'Halkfri 6mm', 299.00),
    ('BOK-001', 'Clean Code', 'Robert C. Martin', 349.00);

insert into product_category (product_id, category_id) values
    (1, 1),
    (2, 1),
    (3, 1),
    (4, 2),
    (5, 2),
    (6, 2),
    (7, 1),
    (7, 3),
    (8, 3),
    (9, 4),
    (10, 5);

insert into inventory (product_id, in_stock) values
    (1, 50),
    (2, 100),
    (3, 30),
    (4, 200),
    (5, 75),
    (6, 60),
    (7, 150),
    (8, 25),
    (9, 40),
    (10, 15);

insert into customers (email, name) values
    ('anna.andersson@email.se', 'Anna Andersson'),
    ('erik.eriksson@email.se', 'Erik Eriksson'),
    ('maria.svensson@email.se', 'Maria Svensson'),
    ('johan.johansson@email.se', 'Johan Johansson'),
    ('lisa.nilsson@email.se', 'Lisa Nilsson');

insert into carts (customer_id) values
    (1),
    (2),
    (3),
    (4),
    (5);

insert into cart_items (cart_id, product_id, qty) values
    (1, 7, 3),
    (3, 1, 1),
    (3, 10, 2);

insert into orders (customer_id, status, total) values
    (1, 'PAID', 1248.00),
    (2, 'PAID', 1247.00),
    (3, 'NEW', 1098.00),
    (4, 'CANCELLED', 499.00),
    (5, 'PAID', 698.00);

insert into order_items (order_id, product_id, qty, unit_price, line_total) values
    (1, 1, 1, 899.00, 899.00),
    (1, 2, 1, 349.00, 349.00),
    (2, 4, 2, 199.00, 398.00),
    (2, 5, 1, 599.00, 599.00),
    (2, 6, 1, 449.00, 449.00),
    (3, 8, 1, 799.00, 799.00),
    (3, 9, 1, 299.00, 299.00),
    (4, 3, 1, 499.00, 499.00),
    (5, 10, 2, 349.00, 698.00);

insert into payments (order_id, method, status) values
    (1, 'CARD', 'APPROVED'),
    (2, 'CARD', 'APPROVED'),
    (4, 'CARD', 'DECLINED'),
    (5, 'INVOICE', 'APPROVED');