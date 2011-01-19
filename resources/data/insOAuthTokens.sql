insert into oauthtokensstore(
  tokenid
 ,mid
 ,consumerid
 ,delflg
 ,twituserid
 ,screenname
 ,twitname
 ,pic
 ,useflg)
select
 case count(*) when 0 then 1
 else max(to_number(coalesce(tokenid,'0'),'9999999999')+1)
 end
,/*mid*/'xxxxxxxxxx'
,/*consumerid*/'hoge'
,'0'
,/*twituserid*/
,/*screenname*/
,/*twitname*/
,/*pic*/
,(select case count(*) when 0 then '1' else '0' end from oauthtokensstore
 where mid = /*mid*/ and delflg = '0' and useflg = '1')
from oauthtokensstore