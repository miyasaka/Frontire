SELECT
    m.nickname,
    m.mid
FROM
    members m,
    follow f
WHERE
    f.followmid = /*followmid*/ AND
    f.followermid = /*followermid*/  AND
    m.mid = f.followermid and
    m.status = '1'
