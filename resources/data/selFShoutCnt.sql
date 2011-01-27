-- F Shout数カウント用SQL
-- 自分のメンバーID、グループメンバーID、フォローメンバーIDよりF Shout数を返す
-- ※※※※※※※※※ 「cnt」と言う名前で数だけ返す ※※※※※※※※※
-- type (取得内容 | 必要なパラメタ)
--   0: 自分＋グループメンバー＋フォローメンバーのF Shout数  |  mid/gmidlist/fmidlist
--   1: 0の内容＋自分宛のF Shout数                           |  mid/gmidlist/fmidlist/kwd
--   2: １人のメンバーのF Shout数                            |  mid/plevel
--   3: 自分が確認すべきF Shout数                            |  mid/gmidlist/fmidlist/kwd
/*IF type == 0 || type == 1*/
	--   0:一覧 or 1:自分宛 の場合
	--      必要なパラメタ : gmidlist/fmidlist
	select count(fs.comment) as cnt
	from
		frontiershout fs,
		members m
	where
		    fs.delflg = '0'
		and fs.mid    = m.mid
		and m.status  = '1'
		and (
				   -- グループメンバーのF Shout取得条件
				   (m.mid in /*gmidlist*/(1,2))
				   -- フォローメンバーのF Shout取得条件
				or (m.mid in /*fmidlist*/(1,2) and fs.pub_level in ('0','1','2'))
			)
/*IF type == 1*/
	--   1: 自分宛の場合、キーワード追加
	--      必要なパラメタ : mid/gmidlist/fmidlist/kwd
		and fs.comment like '%' || /*kwd*/ || '%'
/*END*/
/*END*/
/*IF type == 2*/
	--   2: １人のメンバーのF Shout数
	--      必要なパラメタ : mid/plevel
	select count(fs.comment) as cnt
	from
		frontiershout fs
	where
		    fs.delflg = '0'
		and fs.mid    = /*mid*/
		and fs.pub_level in /*plevel*/(1,2)
/*END*/
/*IF type == 3*/
	--   3: 自分が確認すべきF Shout数
	--      必要なパラメタ : mid/gmidlist/fmidlist/kwd
	select count(fs.comment) as cnt
	from
		frontiershout fs,
		members m
	where
		    fs.delflg     = '0'
		and m.mid        != /*mid*/
		and fs.mid        = m.mid
		and m.status      = '1'
		and fs.demandflg  = '1'
		and fs.confirmflg = '0'
		and (
				   -- グループメンバーのF Shout取得条件
				   (fs.mid in /*gmidlist*/(1,2))
				   -- フォローメンバーのF Shout取得条件
				or (fs.mid in /*fmidlist*/(1,2) and fs.pub_level in ('0','1','2'))
			)
		and fs.comment like /*kwd*/ || '%'
/*END*/