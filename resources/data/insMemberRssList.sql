INSERT INTO memberrsslist
(
 mid
,no
,sno
,rssurl
)
VALUES
(
 /*mid*/
,(select coalesce(max(no),0)+1 as no from memberrsslist where mid = /*mid*/)
,(select coalesce(max(sno),0)+1 as sno from memberrsslist where mid = /*mid*/)
,/*rssurl*/

)