update members set
    follownumber = (select count(*) from follow where followmid = /*mid*/ and delflg = '0'),
    followernumber = (select count(*) from follow where followermid = /*mid*/ and delflg = '0'),
    updid = /*entid*/,
    upddate = now()
where
    mid = /*mid*/