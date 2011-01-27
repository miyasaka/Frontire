--コミュニティ参加者状況取得
select
 cid
,sno
,mid
,joinstatus
,requireddate
,requiredmsg
,entdate
,upddate
,updid
from
 community_enterant
where
 cid = /*cid*/
and mid = /*mid*/