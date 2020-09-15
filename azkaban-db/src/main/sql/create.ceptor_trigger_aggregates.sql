CREATE VIEW ceptor_trigger_aggregates AS (
	SELECT ceptor_triggers.code
		, ceptor_triggers.version
		, description
		, COUNT 
	FROM ceptor_triggers
	LEFT JOIN
	(
		SELECT COUNT(event_id) COUNT
		, trigger_code
		, trigger_version 
		FROM ceptor_output WHERE ceptor_output.output_offset is null
		GROUP BY trigger_code, trigger_version
	) b 
	ON ceptor_triggers.code = b.trigger_code 
	AND ceptor_triggers.version = b.trigger_version
);