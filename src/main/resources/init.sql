
DROP TABLE IF EXISTS country;
DROP TABLE IF EXISTS continent;


CREATE TABLE continent (
continent_id   INT   NOT NULL,
continent_name  VARCHAR(20)  NOT NULL,
CONSTRAINT continent_pk PRIMARY KEY (continent_id),
CONSTRAINT continent_uk UNIQUE (continent_name)
);

INSERT INTO continent (continent_id, continent_name) VALUES (1, 'Africa');
INSERT INTO continent (continent_id, continent_name) VALUES (2, 'Asia');
INSERT INTO continent (continent_id, continent_name) VALUES (3, 'Europe');
INSERT INTO continent (continent_id, continent_name) VALUES (4, 'North America');
INSERT INTO continent (continent_id, continent_name) VALUES (5, 'South America');
INSERT INTO continent (continent_id, continent_name) VALUES (6, 'Oceania');
INSERT INTO continent (continent_id, continent_name) VALUES (7, 'Antarctica');
COMMIT;

CREATE TABLE country (
country_id         INT        NOT NULL,
continent_id         INT        NOT NULL,
country_name       VARCHAR(50)  NOT NULL,
area            INT        NOT NULL,
pop             INT        NOT NULL,
pop_upd_on      datetime,
currency        VARCHAR(50)  NOT NULL,
CONSTRAINT country_pk PRIMARY KEY (country_id),
CONSTRAINT country_uk UNIQUE (country_name),
CONSTRAINT country_fk FOREIGN KEY (continent_id) REFERENCES continent(continent_id)
);

INSERT INTO country (country_id, continent_id, country_name, area, pop, pop_upd_on, currency)
VALUES(14, 3, 'Germany', 137847, 82046000, '2008-11-30', 'Euro');

INSERT INTO country (country_id, continent_id, country_name, area, pop, pop_upd_on, currency)
VALUES(48, 1, 'Ghana', 92098, 23837000, null, 'Cedi');

INSERT INTO country (country_id, continent_id, country_name, area, pop, pop_upd_on, currency)
VALUES(53, 6, 'Australia', 2966200, 21884000, '2009-09-04', 'Australian Dollar');

INSERT INTO country (country_id, continent_id, country_name, area, pop, pop_upd_on, currency)
VALUES(73, 3, 'Greece', 50949, 11257285, '2009-01-09', 'Euro');

INSERT INTO country (country_id, continent_id, country_name, area, pop, pop_upd_on, currency)
VALUES(122, 3, 'Georgia', 26900, 4382100, '2008-11-30', 'Lari');

INSERT INTO country (country_id, continent_id, country_name, area, pop, pop_upd_on, currency)
VALUES(123, 6, 'New Zealand', 104454, 4320300, '2009-09-04', 'New Zealand Dollar');

INSERT INTO country (country_id, continent_id, country_name, area, pop, pop_upd_on, currency)
VALUES(147, 1, 'Gambia', 4361, 1705000, null, 'Dalasi');

INSERT INTO country (country_id, continent_id, country_name, area, pop, pop_upd_on, currency)
VALUES(149, 1, 'Gabon', 103347, 1475000, null, 'CFA franc');
COMMIT;