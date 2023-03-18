CREATE TABLE IF NOT EXISTS public.roles (
	id SERIAL NOT NULL primary key,
	name text NOT NULL
);

CREATE TABLE IF NOT EXISTS users (
	id serial primary key,
	username text NOT NULL,
	password text NOT NULL,
	first_name text,
	last_name text,
	email text,
	mobile text,
	street text,
	city text,
	postcode text
);


CREATE TABLE IF NOT EXISTS users_roles (
	user_id integer NOT NULL,
	role_id integer NOT NULL,
	PRIMARY KEY (user_id,role_id)
);

CREATE TABLE IF NOT EXISTS works (
  id serial primary key,
  name text,
  duration integer,
  price DECIMAL(10, 2),
	editable BOOLEAN,
	target text,
  description TEXT
);

CREATE TABLE IF NOT EXISTS invoices (
	id serial primary key,
	number text,
	status text,
	total_amount DECIMAL(10, 2),
	issued timestamp
);


CREATE TABLE IF NOT EXISTS appointments (
  id serial primary key,
  startdate timestamp,
  enddate timestamp,
	canceled_at timestamp,
	status text,
	id_canceler integer,
  id_provider integer,
  id_customer integer,
  id_work integer,
	id_invoice integer
);

CREATE TABLE IF NOT EXISTS works_providers (
  id_user integer NOT NULL,
  id_work integer NOT NULL,
  PRIMARY KEY (id_user, id_work)
);

CREATE TABLE IF NOT EXISTS working_plans (
	id_provider serial primary key,
  monday TEXT,
	tuesday TEXT,
	wednesday TEXT,
	thursday TEXT,
	friday TEXT,
	saturday TEXT,
	sunday TEXT

);

CREATE TABLE IF NOT EXISTS messages (
	id serial primary key,
	created_at timestamp,
	message TEXT,
	id_author integer,
  id_appointment integer
);

CREATE TABLE IF NOT EXISTS corporate_customers (
	id_customer serial primary key,
	vat_number text,
	company_name text
);

CREATE TABLE IF NOT EXISTS providers (
	id_provider serial primary key
);

CREATE TABLE IF NOT EXISTS  retail_customers (
	id_customer serial primary key
);

CREATE TABLE IF NOT EXISTS customers (
	id_customer serial primary key
);

CREATE TABLE IF NOT EXISTS notifications (
	id serial primary key,
	title text,
	message TEXT,
  created_at timestamp,
  url text,
	is_read BOOLEAN,
  id_user integer
);

CREATE TABLE IF NOT EXISTS exchanges (
	id serial primary key,
	exchange_status text,
  id_appointment_requestor integer,
  id_appointment_requested integer
);

-- INSERT available roles
INSERT INTO roles (id,name) VALUES
  (1,'ROLE_ADMIN'),
  (2,'ROLE_PROVIDER'),
  (3,'ROLE_CUSTOMER'),
  (4,'ROLE_CUSTOMER_CORPORATE'),
  (5,'ROLE_CUSTOMER_RETAIL');

-- INSERT admin account with username: 'admin' and password 'qwerty123'
INSERT INTO users (id, username, password)
VALUES (1, 'admin', '$2a$10$EqKcp1WFKVQISheBxkQJoOqFbsWDzGJXRz/tjkGq85IZKJJ1IipYi');
INSERT INTO users_roles (user_id, role_id)
VALUES (1, 1);

-- INSERT provider account with username: 'provider' and password 'qwerty123'
INSERT INTO users (id, username, password)
VALUES (2, 'provider', '$2a$10$EqKcp1WFKVQISheBxkQJoOqFbsWDzGJXRz/tjkGq85IZKJJ1IipYi');
INSERT INTO providers (id_provider)
VALUES (2);
INSERT INTO users_roles (user_id, role_id)
VALUES (2, 2);


-- INSERT retail customer account with username: 'customer_r' and password 'qwerty123'
INSERT INTO users (id, username, password)
VALUES (3, 'customer_r', '$2a$10$EqKcp1WFKVQISheBxkQJoOqFbsWDzGJXRz/tjkGq85IZKJJ1IipYi');
INSERT INTO customers (id_customer)
VALUES (3);
INSERT INTO retail_customers (id_customer)
VALUES (3);
INSERT INTO users_roles (user_id, role_id)
VALUES (3, 3);
INSERT INTO users_roles (user_id, role_id)
VALUES (3, 5);

-- INSERT corporate customer account with username: 'customer_c' and password 'qwerty123'
INSERT INTO users (id, username, password)
VALUES (4, 'customer_c', '$2a$10$EqKcp1WFKVQISheBxkQJoOqFbsWDzGJXRz/tjkGq85IZKJJ1IipYi');
INSERT INTO customers (id_customer)
VALUES (4);
INSERT INTO corporate_customers (id_customer, vat_number, company_name)
VALUES (4, '123456789', 'Company name');
INSERT INTO users_roles (user_id, role_id)
VALUES (4, 3);
INSERT INTO users_roles (user_id, role_id)
VALUES (4, 4);

INSERT INTO works (id, name, duration, price, editable, target, description)
VALUES (1, 'English lesson', 60, 100.00, true, 'retail',
        'This is english lesson with duration 60 minutes and price 100 pln');

INSERT INTO works_providers
VALUES (2, 1);
INSERT INTO working_plans
VALUES (2,
        '{"workingHours":{"start":[6,0],"end":[18,0]},"breaks":[],"timePeroidsWithBreaksExcluded":[{"start":[6,0],"end":[18,0]}]}',
        '{"workingHours":{"start":[6,0],"end":[18,0]},"breaks":[],"timePeroidsWithBreaksExcluded":[{"start":[6,0],"end":[18,0]}]}',
        '{"workingHours":{"start":[6,0],"end":[18,0]},"breaks":[],"timePeroidsWithBreaksExcluded":[{"start":[6,0],"end":[18,0]}]}',
        '{"workingHours":{"start":[6,0],"end":[18,0]},"breaks":[],"timePeroidsWithBreaksExcluded":[{"start":[6,0],"end":[18,0]}]}',
        '{"workingHours":{"start":[6,0],"end":[18,0]},"breaks":[],"timePeroidsWithBreaksExcluded":[{"start":[6,0],"end":[18,0]}]}',
        '{"workingHours":{"start":[6,0],"end":[18,0]},"breaks":[],"timePeroidsWithBreaksExcluded":[{"start":[6,0],"end":[18,0]}]}',
        '{"workingHours":{"start":[6,0],"end":[18,0]},"breaks":[],"timePeroidsWithBreaksExcluded":[{"start":[6,0],"end":[18,0]}]}');

