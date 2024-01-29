CREATE TABLE IF NOT EXISTS doctors (

    id              BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    uuid            UUID NOT NULL,
    first_name      VARCHAR(255) NOT NULL,
    second_name     VARCHAR(255),
    last_name       VARCHAR(255) NOT NULL,

    CONSTRAINT      pk_doctor PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS patients (

    id              BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    uuid            UUID NOT NULL,
    first_name      VARCHAR(255) NOT NULL,
    second_name     VARCHAR(255),
    last_name       VARCHAR(255) NOT NULL,
    birth_date      DATE NOT NULL,

    CONSTRAINT      pk_patient PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS timeslots (

    id                  BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    doctor_id           BIGINT NOT NULL, -- fk
    patient_id          BIGINT, -- fk
    date                DATE NOT NULL,
    start_time          TIME NOT NULL,
    duration            INT NOT NULL,

    CONSTRAINT  pk_timeslot PRIMARY KEY (id),

    CONSTRAINT  fk_timeslot_doctor_id
        FOREIGN KEY (doctor_id)
            REFERENCES doctors (id)
            ON DELETE CASCADE,

    CONSTRAINT  fk_timeslot_patient_id
        FOREIGN KEY (patient_id)
            REFERENCES patients (id)
            ON DELETE CASCADE
);