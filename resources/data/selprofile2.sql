select
 m.mid
,coalesce(m.familyname,'') as familyname
,coalesce(m.name,'') as name
,coalesce(m.nickname,'') as nickname
,coalesce(m.residence_region,'') as residence_region 
,coalesce(m.residence_city,'') as residence_city
,coalesce(m.gender,'') as gender
,coalesce(m.yearofbirth,'') as yearofbirth
,coalesce(m.dateofbirth,'') as dateofbirth
,coalesce(substr(m.dateofbirth,1,2),'') as dateofmonth
,coalesce(substr(m.dateofbirth,3,4),'') as dateofday
,coalesce(m.bloodtype,'') as bloodtype
,coalesce(m.hometown_region,'') as hometown_region
,coalesce(m.hometown_city,'') as hometown_city
,coalesce(m.interest1,'') as interest1
,coalesce(m.occupation,'') as occupation
,coalesce(m.company,'') as company
,coalesce(m.aboutme,'') as aboutme
,coalesce(mi.favgenre1,'') as favgenre1
,coalesce(mi.favgenre2,'') as favgenre2
,coalesce(mi.favgenre3,'') as favgenre3
,coalesce(mi.favcontents1,'') as favcontents1
,coalesce(mi.favcontents2,'') as favcontents2
,coalesce(mi.favcontents3,'') as favcontents3
,coalesce(mii.pub_yearofbirth,'') as pub_yearofbirth
,coalesce(mii.pub_dateofbirth,'') as pub_dateofbirth
,coalesce(mii.pub_residence,'') as pub_residence
,coalesce(mii.pub_hometown,'') as pub_hometown
,coalesce(mii.pub_occupation,'') as pub_occupation
,coalesce(mii.pub_company,'') as pub_company
,coalesce(mii.pub_diary,'') as pub_diary
,coalesce(mii.pub_name,'') as pub_name
,coalesce(mii.pub_gender,'') as pub_gender
,coalesce(mii.pub_fshout,'') as pub_fshout
from
 members m
,memberadd_info mi
,memberitem_info mii
where
    m.mid = mi.mid
and m.mid = mii.mid
and m.mid = /*mid*/
