CREATE TABLE IF NOT EXISTS tag
(
    id      INT          NOT NULL AUTO_INCREMENT,
    name    VARCHAR(50)  NULL,
    PRIMARY KEY (id),
    CONSTRAINT USER_CONSTR UNIQUE (name)
);
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