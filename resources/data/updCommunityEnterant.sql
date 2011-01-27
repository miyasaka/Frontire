-- コミュニティ参加者情報更新
update community_enterant
set
 joinstatus  = /*joinstatus*/
,requiredmsg = /*requiredmsg*/
,upddate     = /*upddate*/
,updid       = /*updid*/
where
    cid = /*cid*/
and mid = /*mid*/