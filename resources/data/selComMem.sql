select
 m.mid,
 c.cid,
 m.nickname,
 ce.joinstatus,
 ce.requiredmsg,
 ce.sno
from
 members m,
 communities c,
 community_enterant ce
where
 m.mid = /*mid*/ and
 m.mid = ce.mid and
 c.cid = /*cid*/ and
 c.cid = ce.cid