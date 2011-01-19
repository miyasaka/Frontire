-- グループ、メンバー一覧画面でメンバーを検索する
-- type
--   1: ドメインをキーにしてのメンバー検索
--   2: グループに所属しているメンバー検索（キー：グループID）
/*IF type == 1*/
	--ドメインをキーにしてのメンバー検索
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
		end as lastlogin
	from
        frontier_user_management a,
		members m,
		memberphoto_info mp
	where
        a.frontierdomain = /*frontierdomain*/ and
        a.mid = m.mid and
        m.status = '1' and
		m.mid = mp.mid
	order by m.lastaccessdate desc
/*END*/
/*IF type == 2*/
	--グループに所属しているメンバー検索
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
        a.gname
	from
        frontier_group a,
        frontier_group_join b,
		members m,
		memberphoto_info mp
	where
        a.frontierdomain = /*frontierdomain*/ and
        a.gid = /*gid*/ and
        a.frontierdomain = b.frontierdomain and
        a.gid = b.gid and
        b.mid = m.mid and
        m.status = '1' and
		m.mid = mp.mid
	order by m.lastaccessdate desc
/*END*/
