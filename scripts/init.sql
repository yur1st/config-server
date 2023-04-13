-- Table: ncs_cards.properties

-- DROP TABLE IF EXISTS ncs_cards.properties;

CREATE TABLE IF NOT EXISTS ncs_cards.properties
(
	"APPLICATION" character varying(20),
	"PROFILE"     character varying(20),
	"LABEL"       character varying(20),
	"KEY"         character varying(50),
	"VALUE"       character varying(200),
	id            integer NOT NULL,
	CONSTRAINT properties_pkey PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS ncs_cards.properties
	OWNER TO dba;

GRANT ALL ON TABLE ncs_cards.properties TO dba;

-- Trigger: properties_change

-- DROP TRIGGER IF EXISTS properties_change ON ncs_cards.properties;

CREATE TRIGGER properties_change
	AFTER INSERT OR DELETE OR UPDATE
	ON ncs_cards.properties
	FOR EACH ROW
EXECUTE FUNCTION ncs_cards.props_notify();

CREATE OR REPLACE FUNCTION ncs_cards.props_notify()
	RETURNS TRIGGER AS
$$
DECLARE
BEGIN
	PERFORM pg_notify('properties', TO_CHAR(NOW(), 'HH24:MI:SS'));
	RETURN new;
END;
$$ LANGUAGE plpgsql;

INSERT INTO ncs_cards.properties ("APPLICATION", "PROFILE", "LABEL", "KEY", "VALUE", id) VALUES ('app', 'test', 'master', 'app.external-for-test', 'external from config server for Test1', 1);
INSERT INTO ncs_cards.properties ("APPLICATION", "PROFILE", "LABEL", "KEY", "VALUE", id) VALUES ('app', 'dev', 'master', 'app.external-for-dev', 'external from config server for DEV2', 2);


