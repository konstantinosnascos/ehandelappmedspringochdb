drop table if exists payments cascade;
drop table if exists order_items cascade;
drop table if exists orders cascade;
drop table if exists cart_items cascade;
drop table if exists carts cascade;
drop table if exists inventory cascade;
drop table if exists product_category cascade;
drop table if exists products cascade;
drop table if exists categories cascade;
drop table if exists customers cascade;

create table categories (
    id serial primary key,
    name varchar(100) not null unique
);

create table products (
    id serial primary key,
    sku varchar(50) not null unique,
    name varchar(100) not null,
    description text,
    price numeric(10, 2) not null check (price >= 0),
    active boolean not null default true,
    created_at timestamp not null default current_timestamp
);

create table product_category (
    product_id integer not null references products(id) on delete cascade,
    category_id integer not null references categories(id) on delete cascade,
    primary key (product_id, category_id)
);

create table inventory (
    product_id integer primary key references products(id) on delete cascade,
    in_stock integer not null default 0 check (in_stock >= 0)
);

create table customers (
    id serial primary key,
    email varchar(100) not null unique,
    name varchar(100) not null,
    created_at timestamp not null default current_timestamp
);

create table carts (
    id serial primary key,
    customer_id integer not null references customers(id) on delete cascade,
    created_at timestamp not null default current_timestamp
);

create table cart_items (
    id serial primary key,
    cart_id integer not null references carts(id) on delete cascade,
    product_id integer not null references products(id) on delete cascade,
    qty integer not null check (qty > 0),
    unique(cart_id, product_id)
);

create table orders (
    id serial primary key,
    customer_id integer not null references customers(id),
    status varchar(20) not null default 'NEW' check (status in ('NEW', 'PAID', 'CANCELLED')),
    total numeric(10, 2) not null default 0 check (total >= 0),
    created_at timestamp not null default current_timestamp
);

create table order_items (
    id serial primary key,
    order_id integer not null references orders(id) on delete cascade,
    product_id integer not null references products(id),
    qty integer not null check (qty > 0),
    unit_price numeric(10, 2) not null check (unit_price >= 0),
    line_total numeric(10, 2) not null check (line_total >= 0)
);

create table payments (
    id serial primary key,
    order_id integer not null references orders(id) on delete cascade,
    method varchar(20) not null check (method in ('CARD', 'INVOICE')),
    status varchar(20) not null default 'PENDING' check (status in ('PENDING', 'APPROVED', 'DECLINED')),
    timestamp timestamp not null default current_timestamp
);