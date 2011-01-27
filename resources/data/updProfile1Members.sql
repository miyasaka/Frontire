update
	members
set
	email = /*email*/,
	mobileemail = /*mobileemail*/,
	password = /*password*/,
	upddate = current_timestamp,
	updid = /*mid*/,
	target = /*fshoutFrom*/,
	updateFrequency = /*updateFrequency*/
where
	mid = /*mid*/