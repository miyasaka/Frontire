SELECT
    m.mid
FROM
    members m
WHERE
    mid in /*listmid*/(1,2) AND
    not exists (SELECT
                    si.mid
                FROM
                    schedule_join_info si
                WHERE
                    si.mid = /*mid*/ AND
                    si.sno = /*sno*/ AND
                    m.mid = si.joinmid
                )
