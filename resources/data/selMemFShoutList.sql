-- ■メンバー単位のFShout一覧取得用SQL
-- publevelは取得する公開範囲：配列型(List<String>)
select
	m.mid,
	m.nickname,
	m.membertype,
	fs.comment,
	fs.no,
	fs.twitter,
	fs.entid,
	to_char(fs.entdate,'yyyy/mm/dd hh24:mi') as entdate,
	case mpi.mainpicno
		when '1' then mpi.pic1
		when '2' then mpi.pic2
		when '3' then mpi.pic3
	end as photo,
	fum.nickname as fnickname,
	fum.frontierdomain,
	fum.fid,
	fs.updid,
	fs.upddate,
	fs.demandflg,
	fs.confirmflg,
	fs.pub_level,
	fs.delflg
from
	members m,
	frontiershout fs,
	memberphoto_info mpi,
	frontier_user_management fum
where
	    m.mid         = /*mid*/
	and m.mid         = fs.mid
	and m.mid         = mpi.mid
	and m.mid         = fum.mid
	and fs.delflg     = '0'
	and fs.pub_level in /*publevel*/(1,2)
order by fs.entdate desc