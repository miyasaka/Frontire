-- 共通項目取得SQL
select
	i.itemid,
	i.itemdesc,
	ic.itemcd,
	ic.itemname
from
	mst_comitems as i,
	mst_comitemcode as ic
where
	    i.itemid = ic.itemid
	and i.itemid = /*itemid*/
order by ic.itemcd;