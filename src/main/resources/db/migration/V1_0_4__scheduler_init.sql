CREATE TABLE IF NOT EXISTS public.roles (
	id uuid NOT NULL primary key,
	name text NOT NULL
);

CREATE TABLE IF NOT EXISTS users (
	id uuid primary key,
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
	user_id uuid NOT NULL,
	role_id uuid NOT NULL,
	PRIMARY KEY (user_id,role_id)
);

CREATE TABLE IF NOT EXISTS works (
  id uuid primary key,
  name text,
  duration integer,
  price DECIMAL(10, 2),
	editable BOOLEAN,
	target text,
  description TEXT
);

CREATE TABLE IF NOT EXISTS invoices (
	id uuid primary key,
	number text,
	status text,
	total_amount DECIMAL(10, 2),
	issued timestamp
);


CREATE TABLE IF NOT EXISTS appointments (
  id uuid primary key,
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
  id_user uuid NOT NULL,
  id_work uuid NOT NULL,
  PRIMARY KEY (id_user, id_work)
);

CREATE TABLE IF NOT EXISTS working_plans (
	id_provider uuid primary key,
  monday TEXT,
	tuesday TEXT,
	wednesday TEXT,
	thursday TEXT,
	friday TEXT,
	saturday TEXT,
	sunday TEXT

);

CREATE TABLE IF NOT EXISTS messages (
	id uuid primary key,
	created_at timestamp,
	message TEXT,
	id_author uuid,
  id_appointment uuid
);

CREATE TABLE IF NOT EXISTS corporate_customers (
	id_customer uuid primary key,
	vat_number text,
	company_name text
);

CREATE TABLE IF NOT EXISTS providers (
	id_provider uuid primary key
);

CREATE TABLE IF NOT EXISTS  retail_customers (
	id_customer uuid primary key
);

CREATE TABLE IF NOT EXISTS customers (
	id_customer uuid primary key
);

CREATE TABLE IF NOT EXISTS notifications (
	id uuid primary key,
	title text,
	message TEXT,
  created_at timestamp,
  url text,
	is_read BOOLEAN,
  id_user uuid
);

CREATE TABLE IF NOT EXISTS exchanges (
	id uuid primary key,
	exchange_status text,
  id_appointment_requestor uuid,
  id_appointment_requested uuid
);

-- INSERT available roles
INSERT INTO roles (id,name) VALUES
  ('4863203f-fcf8-48d6-b3c1-939e959f873c','ROLE_ADMIN'),
  ('973c824f-2d1f-4c45-b169-532a2b7d7100','ROLE_PROVIDER'),
  ('93fafa5e-6903-4952-9c94-b41eac240c2d','ROLE_CUSTOMER'),
  ('1bcef0fb-9914-4ed3-87e4-534f54719423','ROLE_CUSTOMER_CORPORATE'),
  ('2bfa4fcb-3bfb-46e3-8ec6-7a7e431e0a0b','ROLE_CUSTOMER_RETAIL');

-- INSERT admin account with username: 'admin' and password 'qwerty123'
INSERT INTO users (id, username, password)
VALUES ('3797fbbe-c7e7-48a6-9846-7d41ecf4d338', 'admin', '$2a$10$EqKcp1WFKVQISheBxkQJoOqFbsWDzGJXRz/tjkGq85IZKJJ1IipYi');
INSERT INTO users_roles (user_id, role_id)
VALUES ('3797fbbe-c7e7-48a6-9846-7d41ecf4d338', '4863203f-fcf8-48d6-b3c1-939e959f873c');

-- INSERT provider account with username: 'provider' and password 'qwerty123'
INSERT INTO users (id, username, password)
VALUES ('629f43d1-e89f-471e-ba0f-61bdbdb359d0', 'provider', '$2a$10$EqKcp1WFKVQISheBxkQJoOqFbsWDzGJXRz/tjkGq85IZKJJ1IipYi');
INSERT INTO providers (id_provider)
VALUES ('629f43d1-e89f-471e-ba0f-61bdbdb359d0');
INSERT INTO users_roles (user_id, role_id)
VALUES ('629f43d1-e89f-471e-ba0f-61bdbdb359d0', '973c824f-2d1f-4c45-b169-532a2b7d7100');


-- INSERT retail customer account with username: 'customer_r' and password 'qwerty123'
INSERT INTO users (id, username, password)
VALUES ('4d003570-cd94-4fdb-a5cf-667ca8b98fa2', 'customer_r', '$2a$10$EqKcp1WFKVQISheBxkQJoOqFbsWDzGJXRz/tjkGq85IZKJJ1IipYi');
INSERT INTO customers (id_customer)
VALUES ('4d003570-cd94-4fdb-a5cf-667ca8b98fa2');
INSERT INTO retail_customers (id_customer)
VALUES ('4d003570-cd94-4fdb-a5cf-667ca8b98fa2');
INSERT INTO users_roles (user_id, role_id)
VALUES ('4d003570-cd94-4fdb-a5cf-667ca8b98fa2', '93fafa5e-6903-4952-9c94-b41eac240c2d');
INSERT INTO users_roles (user_id, role_id)
VALUES ('4d003570-cd94-4fdb-a5cf-667ca8b98fa2', '2bfa4fcb-3bfb-46e3-8ec6-7a7e431e0a0b');

-- INSERT corporate customer account with username: 'customer_c' and password 'qwerty123'
INSERT INTO users (id, username, password)
VALUES ('bb4caaf3-b16f-4ef0-8a65-f826c9c853cd', 'customer_c', '$2a$10$EqKcp1WFKVQISheBxkQJoOqFbsWDzGJXRz/tjkGq85IZKJJ1IipYi');
INSERT INTO customers (id_customer)
VALUES ('bb4caaf3-b16f-4ef0-8a65-f826c9c853cd');
INSERT INTO corporate_customers (id_customer, vat_number, company_name)
VALUES ('bb4caaf3-b16f-4ef0-8a65-f826c9c853cd', '123456789', 'Company name');
INSERT INTO users_roles (user_id, role_id)
VALUES ('bb4caaf3-b16f-4ef0-8a65-f826c9c853cd', '93fafa5e-6903-4952-9c94-b41eac240c2d');
INSERT INTO users_roles (user_id, role_id)
VALUES ('bb4caaf3-b16f-4ef0-8a65-f826c9c853cd', '1bcef0fb-9914-4ed3-87e4-534f54719423');

INSERT INTO works (id, name, duration, price, editable, target, description)
VALUES ('0114860f-d6a0-4876-a88d-0b8e63512f78', 'English lesson', 60, 100.00, true, 'retail',
        'This is english lesson with duration 60 minutes and price 100 pln');

INSERT INTO works_providers(id_user, id_work)
VALUES ('629f43d1-e89f-471e-ba0f-61bdbdb359d0', '0114860f-d6a0-4876-a88d-0b8e63512f78');
INSERT INTO working_plans(id_provider, 
	monday,
	tuesday,
	wednesday,
	thursday,
	friday,
	saturday,
	sunday)
VALUES ('629f43d1-e89f-471e-ba0f-61bdbdb359d0',
        '{"workingHours":{"start":[6,0],"end":[18,0]},"breaks":[],"timePeroidsWithBreaksExcluded":[{"start":[6,0],"end":[18,0]}]}',
        '{"workingHours":{"start":[6,0],"end":[18,0]},"breaks":[],"timePeroidsWithBreaksExcluded":[{"start":[6,0],"end":[18,0]}]}',
        '{"workingHours":{"start":[6,0],"end":[18,0]},"breaks":[],"timePeroidsWithBreaksExcluded":[{"start":[6,0],"end":[18,0]}]}',
        '{"workingHours":{"start":[6,0],"end":[18,0]},"breaks":[],"timePeroidsWithBreaksExcluded":[{"start":[6,0],"end":[18,0]}]}',
        '{"workingHours":{"start":[6,0],"end":[18,0]},"breaks":[],"timePeroidsWithBreaksExcluded":[{"start":[6,0],"end":[18,0]}]}',
        '{"workingHours":{"start":[6,0],"end":[18,0]},"breaks":[],"timePeroidsWithBreaksExcluded":[{"start":[6,0],"end":[18,0]}]}',
        '{"workingHours":{"start":[6,0],"end":[18,0]},"breaks":[],"timePeroidsWithBreaksExcluded":[{"start":[6,0],"end":[18,0]}]}');

