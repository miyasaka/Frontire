select 
    distinct
    a.statusid,
    a.comment,
    a.twituserid,
    a.screenname,
    a.twitname,
    a.pic,
    a.replytwitstatusid,
    a.replytwituserid,
    case b.favoriteflg
        when '1' then 'true'
        else 'false'
    end as favorite,
    substring(to_char(a.contributetime,'Day'),1,3) || ' ' ||
    substring(to_char(a.contributetime,'Month'),1,3) || ' ' ||
    to_char(a.contributetime,'dd hh24:mi:ss +0000 yyyy') as createtime,
    coalesce(b.favoriteflg,'0') as favoriteflg
from
    twitter_db as a,
    twitter_post_management as b
where
    a.statusid = b.statusid and
    a.delflg = '0' 
    /*IF searchword != ""*/
    and a.comment @@ /*searchword*/ 
    /*END*/
    /*IF searchUserId != ""*/
    and a.screenname in /*searchUserId*/(1,2)
    /*END*/
order by a.statusid desc