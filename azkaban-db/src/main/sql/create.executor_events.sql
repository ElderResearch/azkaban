CREATE TABLE executor_events (
  execution_id  INT      NOT NULL,
  id			INT;
  event_type    TINYINT  NOT NULL,
  event_time    DATETIME NOT NULL,
  username      VARCHAR(64),
  message       VARCHAR(512),
  event_value   NUMERIC(10,2)
);

CREATE INDEX executor_log
  ON executor_events (executor_id, event_time);
