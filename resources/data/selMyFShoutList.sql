-- ■マイFShout一覧取得用SQL
select
	-- ユニークになるソートキー(登録日 + メンバーID + FShout No)
	to_char(fs.entdate,'yyyymmddhh24miss') || m.mid || fs.no as ukey,
	fum.frontierdomain,
	fum.fid,
	m.mid,
	cast(substr(m.mid,2) as int4) as substrmid,
	cast(substr(fum.fid,2) as int4) as substrfid,
	-- メンバータイプ毎のニックネーム設定(外部Frontier対応)
	case m.membertype
		when '0' then m.nickname
		when '1' then fum.nickname
		else ''
	end as nickname,
	fs.comment,
	fs.no,
	fs.twitter,
	fs.entid,
	to_char(fs.entdate,'yyyy/mm/dd hh24:mi') as entdate,
	-- メンバータイプ毎の画像パス設定(外部Frontier対応)
	case m.membertype
		when '0' then
			case mpi.mainpicno
				when '1' then mpi.pic1
				when '2' then mpi.pic2
				when '3' then mpi.pic3
			end
		when '1' then fum.pic
		else ''
	end as photo,
	fs.updid,
	fs.upddate,
	fs.demandflg,
	fs.confirmflg,
	fs.pub_level,
	fs.delflg,
	m.membertype,
	coalesce(fum.nickname,'') as fnickname,
	coalesce(fum.pic,'') as fpic
from
	members m,
	frontiershout fs,
	memberphoto_info mpi,
	frontier_user_management fum
where
	    m.mid     = fs.mid
	and m.mid     = mpi.mid
	and m.mid     = fum.mid
	and m.status  = '1'
	and fs.delflg = '0'
	and (
			   -- グループメンバーのF Shout取得条件
			   (m.mid in /*gmidlist*/(1,2))
			   -- フォローメンバーのF Shout取得条件
			or (m.mid in /*fmidlist*/(1,2) and fs.pub_level in ('0','1','2'))
		)
/*IF kwdflg == 1*/
	-- キーワード追加
	and fs.comment like '%' || /*kwd*/ || '%'
/*END*/
/*IF periodflg == 1*/
	-- ソートキー追加
	and to_char(fs.entdate,'yyyymmddhh24miss') || m.mid || fs.no < /*period*/
/*END*/
order by to_char(fs.entdate,'yyyymmddhh24miss') desc, m.mid desc, fs.no desc