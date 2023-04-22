UPDATE working_plans
	SET 
	monday = REPLACE(monday, 'Peroid', 'Period'),
	tuesday = REPLACE(tuesday, 'Peroid', 'Period'),
	wednesday = REPLACE(wednesday, 'Peroid', 'Period'),
	thursday = REPLACE(thursday, 'Peroid', 'Period'),
	friday = REPLACE(friday, 'Peroid', 'Period'),
	saturday = REPLACE(saturday, 'Peroid', 'Period'),
	sunday = REPLACE(sunday, 'Peroid', 'Period');