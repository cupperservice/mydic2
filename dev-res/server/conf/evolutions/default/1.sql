# --- !Ups
CREATE TABLE word (
  id            bigint(20) NOT NULL AUTO_INCREMENT,
  word          varchar(255) NOT NULL,
  ref_count     int,
  last_ref_time varchar(32) NOT NULL,
  PRIMARY KEY (id)
);

CREATE INDEX index_word ON word (word(10));

CREATE TABLE example (
  id        bigint(20) NOT NULL AUTO_INCREMENT,
  content   varchar(255) NOT NULL,
  word_id   bigint(20) NOT NULL,
  PRIMARY KEY (id)
);

CREATE INDEX index_word_ref ON example (word_id, id)

# --- !Downs
DROP INDEX index_word ON word;
DROP TABLE word;
DROP INDEX index_word_ref ON example;
DROP TABLE example;
