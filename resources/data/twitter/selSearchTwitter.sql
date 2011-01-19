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
    to_char(a.contributetime,'yyyy/mm/dd hh24:mi') as contributetime,
    --0固定(アプリが落ちるため)
    '0' as favoriteflg
from
    twitter_db as a,
    twitter_post_management as b
where
    a.statusid = b.statusid and
    a.delflg = '0' 
    /*IF searchword != ""*/
    and a.comment @@ /*searchword*/ 
    /*END*/
    /*IF searchFronYmd != ""*/
    and to_char(a.contributetime,'yyyymmdd') >= /*searchFronYmd*/ 
    /*END*/
    /*IF searchToYmd != ""*/
    and to_char(a.contributetime,'yyyymmdd') <= /*searchToYmd*/ 
    /*END*/
    /*IF searchUserId != ""*/
    and b.twituserid in /*searchUserId*/(1,2)
    /*END*/
    /*IF searchUserId == ""*/
    and exists(select c.twituserid from oauthTokensStore as c where c.twituserid = b.twituserid and c.mid = /*mid*/ and delflg = '0')
    /*END*/
order by a.statusid desc