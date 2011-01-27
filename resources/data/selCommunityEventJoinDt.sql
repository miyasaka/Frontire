--コミュニティ参加者情報取得
select
 cid
,bbsid
,mid
,entid
,entdate
,updid
,upddate
from
 community_evententry_info
where
 cid = /*cid*/
and mid = /*mid*/
and bbsid = /*bbsid*/
