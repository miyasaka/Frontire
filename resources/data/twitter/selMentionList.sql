--mentionを取得する
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
    coalesce(b.favoriteflg,'0') as favoriteflg,
    b.twituserid as mytwituserid
from
    twitter_db as a,
    twitter_post_management as b 
where
    a.statusid = b.statusid and
    b.twituserid = /*twituserid*/ and
/*IF realmentionflg != null*/
	b.realmentionflg = '1' and
/*END*/
/*IF statusidFrom != null*/
	a.statusid < cast(trim(/*statusidFrom*/) as int8) and
/*END*/
/*IF statusidTo != null*/
	a.statusid > cast(trim(/*statusidTo*/) as int8) and
/*END*/
/*IF favoriteflg != null*/
	b.favoriteflg = '1' and
/*END*/
    a.delflg = '0'
order by a.statusid desc