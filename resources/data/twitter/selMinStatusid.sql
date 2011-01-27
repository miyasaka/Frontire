--twitter_post_managementから指定したユーザの最小のステータスIDを取得する
select
    coalesce(min(statusid),-1) as statudid
from
    twitter_post_management
where
    twituserid = /*twituserid*/