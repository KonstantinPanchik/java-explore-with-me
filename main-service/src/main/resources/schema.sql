DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS locations CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS compilations CASCADE;
DROP TABLE IF EXISTS compilations_to_event CASCADE;
DROP TABLE If EXISTS requests CASCADE;


CREATE TABLE IF NOT EXISTS users
(
    user_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY UNIQUE,
    name    VARCHAR(250)        NOT NULL,
    email   VARCHAR(254) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS user_subscriptions
(
    user_id    BIGINT NOT NULL,
    blogger_id BIGINT NOT NULL,
    CONSTRAINT pk_user_subscriptions PRIMARY KEY (user_id, blogger_id),
    CONSTRAINT fk_user_subscriptions_to_user FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE,
    CONSTRAINT fk_user_subscriptions_to_blogger FOREIGN KEY (blogger_id) REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS categories
(
    category_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY UNIQUE,
    name        VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS locations
(
    id  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY UNIQUE,
    lat FLOAT,
    lon FLOAT,
    UNIQUE (lat,lon)
);

CREATE TABLE IF NOT EXISTS events
(
    event_id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY UNIQUE,
    annotation         VARCHAR(2000) NOT NULL,
    category_id        BIGINT        NOT NULL,
    confirmed_Requests BIGINT,
    create_date          TIMESTAMP WITHOUT TIME ZONE,
    description        VARCHAR(7000),
    event_date         TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    initiator_user_id       BIGINT        NOT NULL,
    location_id        BIGINT,
    paid               BOOLEAN,
    participant_limit  INTEGER DEFAULT 0,
    published_on       TIMESTAMP WITHOUT TIME ZONE,
    request_moderation BOOLEAN DEFAULT true,
    state              VARCHAR(200) NOT NULL,
    title              VARCHAR(120)  NOT NULL,
    CONSTRAINT fk_event_to_user FOREIGN KEY (initiator_user_id) REFERENCES users (user_id),
    CONSTRAINT fk_event_to_category FOREIGN KEY (category_id) REFERENCES categories (category_id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_location FOREIGN KEY (location_id) REFERENCES locations (id)
);

CREATE TABLE IF NOT EXISTS requests
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    event_id     BIGINT NOT NULL,
    requester_id BIGINT NOT NULL,
    create_date  TIMESTAMP WITHOUT TIME ZONE,
    status       VARCHAR(20),
    UNIQUE (event_id,requester_id),
    CONSTRAINT fk_requests_to_event FOREIGN KEY (event_id) REFERENCES events (event_id),
    CONSTRAINT fk_requests_to_user FOREIGN KEY (requester_id) REFERENCES users (user_id)
);

CREATE TABLE IF NOT EXISTS compilation
(
    compilation_id     BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY UNIQUE,
    pinned BOOLEAN     NOT NULL,
    title  VARCHAR(50) NOT NULL,
    UNIQUE(title)
);

CREATE TABLE IF NOT EXISTS compilation_event
(
    id             BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    event_id       BIGINT NOT NULL,
    compilation_id BIGINT NOT NULL,
    CONSTRAINT fk_event_compilation_to_event FOREIGN KEY (event_id) REFERENCES events (event_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_event_compilation_to_compilation FOREIGN KEY (compilation_id) REFERENCES compilation (compilation_id) ON DELETE CASCADE ON UPDATE CASCADE
);

