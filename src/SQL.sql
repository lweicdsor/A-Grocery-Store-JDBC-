 create table customer(
 customer_id int primary key,
 firstname varchar(30),
 middlename varchar(30),
 lastname varchar(30),
 balance int check (balance >= 0),
 password varchar(10)
 );

 create table staff(
 staff_id int primary key,
 firstname varchar(30),
 middlename varchar(30),
 lastname varchar(30),
 salary numeric(6,0) check (salary >= 0),
 password varchar(10),
 job_title varchar(20) check (job_title in ('manager', 'staff'))
 );
 
 create table address(
 street_number int,
 street_name varchar(256),
 apt_number int,
 zipcode number(10),
 city varchar(20),
 state char(2),
 primary key(street_number, street_name, apt_number, zipcode)
 );
  
 create table creditcard(
 card_number number(16) primary key,
 bank varchar(20),
 expiration_date date,
 customer_id int references customer,
 street_number int,
 street_name varchar(256),
 apt_number int,
 zipcode number(10),
 foreign key (street_number, street_name, apt_number, zipcode) references address
 );
   
 create table supplier(
 supplier_id int primary key,
 name varchar(256),
 street_number int,
 street_name varchar(256),
 apt_number int,
 zipcode number(10),
 foreign key (street_number, street_name, apt_number, zipcode) references address
 );

 create table warehouse(
 warehouse_id int primary key,
 storage_capacity int check ( storage_capacity >= 0),
 street_number int,
 street_name varchar(256),
 apt_number int,
 zipcode number(10),
 foreign key (street_number, street_name, apt_number, zipcode) references address
 );
 
 create table orders(
 order_number int primary key,
 customer_id int references customer,
 card_number number(20) references creditcard,
 subtotal int,
 time timestamp,
 status varchar(8),
 check (status in ('issued', 'send', 'received'))
 );
 
 create table product(
 product_name varchar(40),
 product_category varchar(20),
 sizes int check (sizes > 0),
 primary key(product_name, product_category)
 );
 
 create table shoppingcart(
 customer_id int references customer, 
 product_name varchar(40), 
 product_category varchar(20),
 quantity number(3) check (quantity >= 0),
 primary key(customer_id, product_name, product_category),
 foreign key (product_name, product_category) references product
 );
  
 create table price(
 product_name varchar(40), 
 product_category varchar(20),
 state char(2),
 price numeric(5,0) check (price >= 0),
 primary key(product_name, product_category, state),
 foreign key (product_name, product_category) references product
 );
 
 create table alcoholic_drinks(
 product_name varchar(40), 
 product_category varchar(20),
 alcohol_content numeric(3,0) check (alcohol_content > 0),
 primary key(product_name, product_category),
 foreign key (product_name, product_category) references product
 );
 
 create table food(
 product_name varchar(40), 
 product_category varchar(20),
 calory int check (calory >= 0),
 carbohydrate int check (carbohydrate >= 0),
 fat int check (fat >= 0),
 protein int check (protein >= 0),
 sodium int check (sodium >= 0),
 primary key (product_name, product_category),
 foreign key (product_name, product_category) references product
 );
  
 create table supplier_product(
 supplier_id int references supplier,
 product_name varchar(40), 
 product_category varchar(20),
 supplier_price numeric(4,0) check (supplier_price >= 0),
 primary key (supplier_id, product_name, product_category),
 foreign key (product_name, product_category) references product
 );
 
 create table order_details(
 order_number int references orders,
 product_name varchar(40), 
 product_category varchar(20),
 quantity int check (quantity > 0),
 primary key (order_number, product_name, product_category)
 );
 
 create table stock(
 product_name varchar(40), 
 product_category varchar(20),
 warehouse_id int references warehouse,
 available_amount int check (available_amount >= 0),
 primary key(product_name, product_category, warehouse_id),
 foreign key (product_name, product_category) references product
 );
 
 create table delivery_address(
 customer_id int references customer,
 street_number int,
 street_name varchar(256),
 apt_number int,
 zipcode number(10),
 foreign key (street_number, street_name, apt_number, zipcode) references address,
 primary key (customer_id, street_number, street_name, apt_number, zipcode)
 );

 create table staff_address(
 staff_id int references staff,
 street_number int,
 street_name varchar(256),
 apt_number int,
 zipcode number(10),
 foreign key (street_number, street_name, apt_number, zipcode) references address,
 primary key (staff_id, street_number, street_name, apt_number, zipcode)
 );
 
 create index product_index on product (product_name);
 create index orders_index on orders (status, time);
 create index customer_index on customer (firstname, lastname);
 create index staff_index on staff (firstname, lastname);