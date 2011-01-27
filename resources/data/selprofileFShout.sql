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
,fum.fid
,COALESCE(fum.nickname,'') as fnickname
,fum.frontierdomain
,m.membertype
from
 members m
,memberitem_info mi
,frontier_user_management fum
where
    m.mid = mi.mid
and m.mid = fum.mid
and to_number(substr(fum.fid, 2, 10),'000000000') = /*mid*/
and fum.frontierdomain = /*frontierdomain*/
and m.status = '1'
group by 
 m.mid
,m.nickname
,m.entdate
,m.email
,m.mobileemail
,mEmailFront
,mEmailDomain
,mi.pub_diary
,m.password
,m.twitteraccount
,m.twitterpassword
,m.twitterflg
,m.follownumber
,m.followernumber
,fum.fid
,fum.nickname
,fum.frontierdomain
,m.membertype
