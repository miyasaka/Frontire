select
 m.mid
,m.nickname
,m.entdate
,m.email
,m.mobileemail
,case when m.mobileemail !='' then substr(m.mobileemail,0,strpos(m.mobileemail,'@')) else '' end as mEmailFront
,case when m.mobileemail !='' then substr(m.mobileemail,strpos(m.mobileemail,'@')+1,length(m.mobileemail) - strpos(m.mobileemail,'@')) else '' end as mEmailDomain
,mi.pub_diary
,m.password
,COALESCE(m.twitteraccount,'') as twitteraccount
,COALESCE(m.twitterpassword,'') as twitterpassword
,COALESCE(m.twitterflg,'') as twitterflg
,m.follownumber
,m.followernumber
,mi.pub_fshout
,m.target
,m.updatefrequency
from
 members m
,memberitem_info mi
where
    m.mid = mi.mid
and m.mid = /*mid*/
and m.status = '1'

