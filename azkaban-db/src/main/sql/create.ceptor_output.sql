CREATE TABLE ceptor_output (
  source_id        INT,
  event_source     INT,
  event_id         INT,
  event_timestamp TIMESTAMP,
  trigger_code     VARCHAR(128),
  trigger_version  INT NOT NULL,
  actor_id         INT NOT NULL,
  output_context   VARCHAR(1024),
  outout_offset    INT
);