CREATE TABLE IF NOT EXISTS tag
(
    id      INT          NOT NULL AUTO_INCREMENT,
    name    VARCHAR(50)  NULL,
    PRIMARY KEY (id),
    CONSTRAINT USER_CONSTR UNIQUE (name)
);
INSERT INTO tag (id, name) VALUES (DEFAULT, 'belita');
INSERT INTO tag (id, name) VALUES (DEFAULT, 'mark formel');

CREATE TABLE IF NOT EXISTS giftCertificate
(
    id       INT          NOT NULL AUTO_INCREMENT,
    name    VARCHAR(50)  NULL,
    description  VARCHAR(150)  NULL,
    price   DOUBLE NULL,
    duration INT    NULL,
    creationDate    TIMESTAMP(6) NULL,
    lastUpdateDate  TIMESTAMP(6) NULL,
    PRIMARY KEY (id),
    CONSTRAINT GIFT_CERTIFICATE_CONSTR UNIQUE (name)
);

INSERT INTO giftCertificate(id, name, description, price, duration, creationDate, lastUpdateDate) VALUES (DEFAULT, 'comfort', 'comfort 30 day', 100, 30, '2020-02-01 10:11:02', '2020-02-30 10:11:02');
INSERT INTO giftCertificate(id, name, description, price, duration, creationDate, lastUpdateDate) VALUES (DEFAULT, 'comfort+', 'comfort 60 day', 200, 60, '2020-02-01 10:11:02', '2020-03-30 10:11:02');
INSERT INTO giftCertificate(id, name, description, price, duration, creationDate, lastUpdateDate) VALUES (DEFAULT, 'super comfort', 'comfort 90 day', 300, 90, '2020-02-01 10:11:02', '2020-04-30 10:11:02');

CREATE TABLE IF NOT EXISTS certificate_tag
(
    id        INT          NOT NULL AUTO_INCREMENT,
    certificateId INT          NOT NULL,
    tagId     INT          NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_certificate_tag_giftCertificate1
        FOREIGN KEY (certificateId)
            REFERENCES giftCertificate (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,
    CONSTRAINT fk_certificate_tag_tag2
        FOREIGN KEY (tagId)
            REFERENCES tag (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
);

INSERT INTO certificate_tag (id, certificateId, tagId) VALUES (DEFAULT, 2, 1);
INSERT INTO certificate_tag (id, certificateId, tagId) VALUES (DEFAULT, 3, 2);