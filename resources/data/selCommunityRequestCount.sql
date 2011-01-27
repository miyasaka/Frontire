-- コミュニティ参加リクエスト情報取得
select
 c.cid
,ce.sno
,ce.mid
,ce.joinstatus
,ce.requireddate
,ce.requiredmsg
,ce.entdate
,ce.upddate
,ce.updid
from
 communities c,
 community_enterant ce
where
    ce.joinstatus = '0'
and c.cid = ce.cid
and c.admmid = /*mid*/