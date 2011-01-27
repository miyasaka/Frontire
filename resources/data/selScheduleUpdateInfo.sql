SELECT
    joinmid,
    case when joinmid in /*listmid*/(1,2) then '0' else '1' end as delflg
FROM
    schedule_join_info
WHERE
    mid = /*mid*/
and
    sno = /*sno*/