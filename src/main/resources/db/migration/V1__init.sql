-- user_account
create table IF not exists user_account
(
    id
    BIGINT
    generated
    by
    default as
    identity
    primary
    key,
    discord_id
    varchar
(
    32
) not null unique, username varchar
(
    200
), created_at TIMESTAMPTZ null, last_login_at TIMESTAMPTZ null );

create table IF not exists user_roles
(
    user_id
    BIGINT
    not
    null
    references
    user_account
(
    id
) on delete cascade, role varchar
(
    64
) not null );

-- role_mapping
create table IF not exists role_mapping
(
    id
    BIGINT
    generated
    by
    default as
    identity
    primary
    key,
    discord_role_id
    varchar
(
    32
) not null unique, app_role varchar
(
    64
) not null );

-- wiki_page
create table IF not exists wiki_page
(
    id
    BIGINT
    generated
    by
    default as
    identity
    primary
    key,
    slug
    varchar
(
    160
) not null unique, title varchar
(
    200
) not null, content_markdown TEXT, content_html TEXT, published BOOLEAN not null default false, created_at TIMESTAMPTZ null, updated_at TIMESTAMPTZ null, last_editor_discord_id varchar
(
    32
) );
create index IF not exists ix_wiki_updated on wiki_page(updated_at);

-- wiki_revision
create table IF not exists wiki_revision
(
    id
    BIGINT
    generated
    by
    default as
    identity
    primary
    key,
    page_id
    BIGINT
    not
    null
    references
    wiki_page
(
    id
) on delete cascade, content_markdown TEXT, editor_discord_id varchar
(
    32
), created_at TIMESTAMPTZ null );

-- announcement
create table IF not exists announcement
(
    id
    BIGINT
    generated
    by
    default as
    identity
    primary
    key,
    type
    varchar
(
    16
) not null, title varchar
(
    200
) not null, content_markdown TEXT, content_html TEXT, author_discord_id varchar
(
    32
), created_at TIMESTAMPTZ null, published_at TIMESTAMPTZ null, status varchar
(
    16
) not null );
create index IF not exists ix_ann_type_pub on announcement(type, published_at);
