INSERT INTO community_enterant
(
	cid,
	sno,
	mid,
	joinstatus,
	requireddate,
	requiredmsg,
	entdate,
	upddate,
	updid
)
VALUES
(
    /*cid*/,
	(select coalesce(max(sno),0)+1 from community_enterant where cid = /*cid*/),
    /*mid*/,
    /*joinstatus*/,
    /*requireddate*/,
    /*requiredmsg*/,
    /*entdate*/,
    /*upddate*/,
	/*updid*/
);