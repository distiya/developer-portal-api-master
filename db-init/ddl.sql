create type user_role as enum ('ADMIN', 'USER');
create type notam_api_access_item_type as enum ('Sdk', 'Api');

create table "user"
(
    id                        bigserial    not null
        constraint user_pk
            primary key,
    role                      user_role    not null,
    full_name                 varchar(255) not null,
    email                     varchar(255) not null,
    password_hash             varchar(255) not null,
    company                   varchar(255) not null,
    address                   varchar(255) not null,
    city                      varchar(255) not null,
    state                     varchar(255) not null,
    country                   varchar(255) not null,
    zip_code                  varchar(20)  not null,
    primary_phone             varchar(20)  not null,
    alternate_phone           varchar(20),
    notam_data_intended_usage text,
    email_confirmation_code   varchar(255),
    password_reset_token      varchar(255),
    is_email_confirmed        boolean      not null,
    is_approved               boolean,
    is_enabled                boolean      not null,
    is_deleted                boolean      not null,
    created_by                bigint
        constraint user_user_id_fk
            references "user",
    created_at                timestamp    not null,
    updated_by                bigint
        constraint user_user_id_fk_2
            references "user",
    updated_at                timestamp
);
create unique index user_email_uindex
    on "user" (email);

create table hourly_api_usage
(
    id                   bigserial not null
        constraint hourly_api_usage_pk
            primary key,
    date_time_hour       timestamp not null,
    request_count        integer   not null,
    returned_notam_count integer   not null
);
create unique index hourly_api_usage_date_time_hour_uindex
    on hourly_api_usage (date_time_hour);

create table user_count_per_country
(
    id         bigserial    not null
        constraint user_count_per_country_pk
            primary key,
    country    varchar(255) not null,
    user_count integer      not null
);
create unique index user_count_per_country_country_uindex
    on user_count_per_country (country);

create table notam_api_token
(
    id                  bigserial not null
        constraint notam_api_token_pk
            primary key,
    user_id             bigint    not null,
    name                varchar(255),
    key                 varchar(255),
    is_enabled_by_user  boolean   not null,
    is_enabled_by_admin boolean   not null,
    is_deleted          boolean   not null,
    created_by          bigint    not null
        constraint notam_api_token_user_id_fk
            references "user",
    created_at          timestamp not null,
    updated_by          bigint
        constraint notam_api_token_user_id_fk_2
            references "user",
    updated_at          timestamp
);
create unique index notam_api_token_key_uindex
    on notam_api_token (key);

create table notam_api_access_item
(
    id             bigserial                  not null,
    item_type      notam_api_access_item_type not null,
    version        varchar(20)                not null,
    description    text                       not null,
    change_log     text                       not null,
    content_type   varchar(255)               not null,
    content_length integer                    not null,
    content        pg_largeobject             not null,
    file_name      varchar(255)               not null,
    is_deleted     boolean                    not null,
    created_by     bigint                     not null
        constraint notam_api_access_item_user_id_fk
            references "user",
    created_at     timestamp                  not null,
    updated_by     bigint
        constraint notam_api_access_item_user_id_fk_2
            references "user",
    updated_at     timestamp
);

INSERT INTO public."user" (role, full_name, email, password_hash, company, address, city, state, country, zip_code,
                           primary_phone, alternate_phone, notam_data_intended_usage, email_confirmation_code,
                           password_reset_token, is_email_confirmed, is_approved, is_enabled, is_deleted, created_by,
                           created_at, updated_by, updated_at)
VALUES ('ADMIN', 'Bill Dox', 'admin@developer-portal.notam.faa.gov',
        'b362b09de252379b28bbf1e05de413f68445684bb2f2f5724e18f52a9d69d20f76491995b5240c08', 'Topcoder', '11 Harbour St',
        'Sydney', 'NSW', 'Australia', '2000', '123456789', '012345678', 'Dev', null, null, true, true, true, false,
        null, '2020-12-12 16:15:52.743000', null, '2020-12-12 16:15:52.743000');