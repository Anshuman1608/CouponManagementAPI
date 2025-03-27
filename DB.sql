CREATE DATABASE coupon_db
    WITH
    OWNER = admin
    ENCODING = 'UTF8'
    LOCALE_PROVIDER = 'libc'
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;

CREATE SCHEMA couponmanager
    AUTHORIZATION admin;

CREATE TABLE couponmanager.coupons
(
    id serial,
    type character varying(50)[],
    details jsonb NOT NULL,
    active boolean DEFAULT TRUE,
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS couponmanager.coupons
    OWNER to admin;
