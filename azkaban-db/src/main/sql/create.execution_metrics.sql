CREATE TABLE execution_metrics (
  id			INT		 NOT NULL,
  execution_id 	INT      NOT NULL,
  metric_type   TINYINT  NOT NULL,
  metric_time   LONG     NOT NULL,
  value			NUMERIC(20,2)  
);
