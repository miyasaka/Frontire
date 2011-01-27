-- イベント参加者情報削除（複数）
delete from community_evententry_info
where
 cid = /*cid*/
and mid in /*chkmem*/(1,2)
and bbsid = /*bbsid*/
