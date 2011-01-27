-- グループ、フォローしている・されているメンバーの
-- ＩＤ、ニックネーム、画像パスリストを返す(最終ログイン日順)
-- type
--   0: グループ＋自分がフォローしているメンバー
--   1: グループのメンバー
--   2: 自分がフォローしているメンバー
--   3: 自分をフォローしているメンバー
--   4: 所属グループリスト(ドメイン、ID、グループ名、画像パス、参加メンバー数)
/*IF type == 0*/
	-- グループ＋自分がフォローしているメンバー
	select
		m.mid,
		m.nickname,
		case mp.mainpicno
			when '1' then mp.pic1
			when '2' then mp.pic2
			when '3' then mp.pic3
			else ''
		end as pic
	from
		(
			select followermid as mid
			from follow
			where followmid = /*mid*/ and delflg = '0'
			
			union
			
			select mid
			from frontier_group_join
			where
				(frontierdomain,gid) in (
						select fg.frontierdomain,fg.gid
						from
							frontier_group fg,
							frontier_group_join fgj
						where
							    fg.frontierdomain = fgj.frontierdomain
							and fg.gid            = fgj.gid
							and fgj.mid           = /*mid*/
							and fg.delflg         = '0'
					)
			and mid != /*mid*/
		) a,
		members m,
		memberphoto_info mp
	where
		    a.mid = m.mid
		and m.status = '1'
		and m.mid = mp.mid
	order by m.lastaccessdate desc
/*END*/
/*IF type == 1*/
	-- 1: グループのメンバー
	select
		a.frontierdomain,
		a.gid,
		m.mid,
		m.nickname,
		case mp.mainpicno
			when '1' then mp.pic1
			when '2' then mp.pic2
			when '3' then mp.pic3
			else ''
		end as pic
	from
		(
			select frontierdomain,gid,mid
			from frontier_group_join
			where
				(frontierdomain,gid) in (
						select fg.frontierdomain,fg.gid
						from
							frontier_group fg,
							frontier_group_join fgj
						where
							    fg.frontierdomain = fgj.frontierdomain
							and fg.gid            = fgj.gid
							and fgj.mid           = /*mid*/
							and fg.delflg         = '0'
					)
			and mid != /*mid*/
		) a,
		members m,
		memberphoto_info mp
	where
		    a.mid = m.mid
		and m.status = '1'
		and m.mid = mp.mid
	order by a.frontierdomain,a.gid,m.lastaccessdate desc
/*END*/
/*IF type == 2*/
-- 自分がフォローしているメンバー
	select
		m.mid,
		m.nickname,
		case mp.mainpicno
			when '1' then mp.pic1
			when '2' then mp.pic2
			when '3' then mp.pic3
			else ''
		end as pic,
		case
			when to_timestamp(to_char(current_timestamp,'yyyymmdd hh24:mi'),'yyyymmdd hh24:mi') - to_timestamp(to_char(lastaccessdate,'yyyymmdd hh24:mi'),'yyyymmdd hh24:mi') not like '%day%' and substr(to_timestamp(to_char(current_timestamp,'yyyymmdd hh24:mi'),'yyyymmdd hh24:mi') - to_timestamp(to_char(lastaccessdate,'yyyymmdd hh24:mi'),'yyyymmdd hh24:mi'),1,2) = '00' then '03'
			when to_timestamp(to_char(current_timestamp,'yyyymmdd hh24:mi'),'yyyymmdd hh24:mi') - to_timestamp(to_char(lastaccessdate,'yyyymmdd hh24:mi'),'yyyymmdd hh24:mi') not like '%day%' and substr(to_timestamp(to_char(current_timestamp,'yyyymmdd hh24:mi'),'yyyymmdd hh24:mi') - to_timestamp(to_char(lastaccessdate,'yyyymmdd hh24:mi'),'yyyymmdd hh24:mi'),1,2) <= 23 and substr(to_timestamp(to_char(current_timestamp,'yyyymmdd hh24:mi'),'yyyymmdd hh24:mi') - to_timestamp(to_char(lastaccessdate,'yyyymmdd hh24:mi'),'yyyymmdd hh24:mi'),1,2) > 0 then '02'
		end as lastlogin,
		m.membertype,
		fum.frontierdomain,
		fum.fid,
		fum.mid as fmid,
		fum.nickname as fnickname,
		case 
			when m.membertype = 0 then fum.nickname
			when m.membertype = 1 then fum.nickname || ' [F]'
			else ''
		end as sfnickname,
		case 
			when m.membertype = 0 then fum.nickname
			when m.membertype = 1 then fum.nickname || ' [' || fum.frontierdomain || ']'
			else ''
		end as lfnickname,
		coalesce(fum.pic,'') as fpic
	from
		follow a,
		members m,
		memberphoto_info mp,
		frontier_user_management fum
	where
		    a.followmid = /*mid*/
		and a.delflg = '0'
		and a.followermid = m.mid
		and a.delflg = '0'
		and m.status = '1'
		and m.mid = mp.mid
		and m.mid = fum.mid
	order by m.lastaccessdate desc
/*END*/
/*IF type == 3*/
-- 自分をフォローしているメンバー
	select
		m.mid,
		m.nickname,
		case mp.mainpicno
			when '1' then mp.pic1
			when '2' then mp.pic2
			when '3' then mp.pic3
			else ''
		end as pic,
		case
			when to_timestamp(to_char(current_timestamp,'yyyymmdd hh24:mi'),'yyyymmdd hh24:mi') - to_timestamp(to_char(lastaccessdate,'yyyymmdd hh24:mi'),'yyyymmdd hh24:mi') not like '%day%' and substr(to_timestamp(to_char(current_timestamp,'yyyymmdd hh24:mi'),'yyyymmdd hh24:mi') - to_timestamp(to_char(lastaccessdate,'yyyymmdd hh24:mi'),'yyyymmdd hh24:mi'),1,2) = '00' then '03'
			when to_timestamp(to_char(current_timestamp,'yyyymmdd hh24:mi'),'yyyymmdd hh24:mi') - to_timestamp(to_char(lastaccessdate,'yyyymmdd hh24:mi'),'yyyymmdd hh24:mi') not like '%day%' and substr(to_timestamp(to_char(current_timestamp,'yyyymmdd hh24:mi'),'yyyymmdd hh24:mi') - to_timestamp(to_char(lastaccessdate,'yyyymmdd hh24:mi'),'yyyymmdd hh24:mi'),1,2) <= 23 and substr(to_timestamp(to_char(current_timestamp,'yyyymmdd hh24:mi'),'yyyymmdd hh24:mi') - to_timestamp(to_char(lastaccessdate,'yyyymmdd hh24:mi'),'yyyymmdd hh24:mi'),1,2) > 0 then '02'
		end as lastlogin,
		m.membertype,
		fum.frontierdomain,
		fum.fid,
		fum.mid as fmid,
		fum.nickname as fnickname,
		case 
			when m.membertype = 0 then fum.nickname
			when m.membertype = 1 then fum.nickname || ' [F]'
			else ''
		end as sfnickname,
		case 
			when m.membertype = 0 then fum.nickname
			when m.membertype = 1 then fum.nickname || ' [' || fum.frontierdomain || ']'
			else ''
		end as lfnickname,
		coalesce(fum.pic,'') as fpic
	from
		follow a,
		members m,
		memberphoto_info mp,
		frontier_user_management fum
	where
		    a.followermid = /*mid*/
		and a.followmid = m.mid
		and a.delflg = '0'
		and m.status = '1'
		and m.mid = mp.mid
		and m.mid = fum.mid
	order by m.lastaccessdate desc
/*END*/
/*IF type == 4*/
	-- 4: 所属グループリスト(ドメイン、ID、グループ名、画像パス、参加メンバー数)
	select
		fg.frontierdomain,
		fg.gid,
		fg.gname,
		coalesce(fg.pic,'') as pic,
		fg.joinnumber
	from
		frontier_group fg,
		frontier_group_join fgj
	where
		    fg.frontierdomain = fgj.frontierdomain
		and fg.gid            = fgj.gid
		and fgj.mid           = /*mid*/
		and fg.delflg         = '0'
	order by fgj.entdate
/*END*/