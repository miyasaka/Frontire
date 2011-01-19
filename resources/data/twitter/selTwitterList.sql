select 
    a.statusid,
    a.comment,
    a.twituserid,
    a.screenname,
    a.twitname,
    a.pic,
    a.replytwitstatusid,
    a.replytwituserid,
    to_char(a.contributetime,'yyyy/mm/dd hh24:mi') as contributetime,
    coalesce(b.favoriteflg,'0') as favoriteflg
from
    twitter_db as a
    left join twitter_post_management as b 
            on a.statusid = b.statusid and
               a.twituserid = b.twituserid
where
/*IF statusid != null*/
	a.statusid < cast(trim(/*statusid*/) as int8) and
/*END*/
/*IF statusidfrom != null*/
	a.statusid > cast(trim(/*statusidfrom*/) as int8) and
/*END*/
    a.twituserid = /*twituserid*/ and
    a.delflg = '0'
order by a.statusid desc