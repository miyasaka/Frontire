SELECT
	A.SNO,
	A.TITLECOLOR,
	A.CID,
	A.BBSID,
	A.TITLE,
	A.DETAIL,
	REPLACE(A.DETAIL,'\r\n','\\n') AS DETAILJS,
	A.STARTYEAR,
	A.STARTMONTH,
	A.STARTDAY,
	A.STARTYEAR||A.STARTMONTH||A.STARTDAY AS SCHEDULEDATE,
	A.TIME_S,
	A.MINUTE_S,
	A.TIME_E,
	A.MINUTE_E,
	A.STARTTIME,
	A.ENDTIME,
	A.FRIENDSTATUS,
	A.NICKNAME,
	A.ENTRY
FROM
	(
		-- 他のメンバーのスケジュールデータ取得
		SELECT
			S.SNO,
			S.TITLECOLOR,
			M.MID AS CID,
			NULL AS BBSID,
			S.TITLE,
			S.DETAIL,
			S.STARTYEAR,
			S.STARTMONTH,
			S.STARTDAY,
			S.STARTTIME AS TIME_S,
			S.STARTMINUTE AS MINUTE_S,
			S.ENDTIME AS TIME_E,
			S.ENDMINUTE AS MINUTE_E,
			CASE
				WHEN S.STARTTIME = '--' AND S.STARTMINUTE = '--' THEN NULL
				ELSE S.STARTTIME||':'||S.STARTMINUTE||' ～'
			END AS STARTTIME,
			CASE
				WHEN S.ENDTIME = '--' AND S.ENDMINUTE = '--' THEN NULL
				ELSE ' '||S.ENDTIME||':'||S.ENDMINUTE
			END AS ENDTIME,
			CASE
				WHEN S.MID = /*mid*/ THEN '0'
				WHEN S.MID IN /*idlist*/(1,2) THEN '1'
				ELSE '2'
			END AS FRIENDSTATUS,
			M.NICKNAME,
			'2' AS ENTRY,
			S.ENTDATE
		FROM
			SCHEDULE S,
			MEMBERS M
		WHERE
			(
				    S.STARTYEAR  >  /*year*/
				OR  S.STARTYEAR  >= /*year*/
				AND S.STARTMONTH >  /*month*/
				OR  S.STARTYEAR  >= /*year*/
				AND S.STARTMONTH >= /*month*/
				AND S.STARTDAY   >= /*day*/
			)
			AND
			(
				S.MID = /*mid*/ OR
				(
					(
						-- グループメンバーの「全体に公開」「グループに公開」データの取得
						(
							    S.MID IN /*idlist*/(1,2)
							AND S.PUBLEVEL IN ('1','2')
						)
						-- 他メンバーの「全体に公開」データの取得
						OR S.PUBLEVEL = '1'
					)
					AND EXISTS
						(
							SELECT
								SI.MID,
								SI.SNO
							FROM
								SCHEDULE_JOIN_INFO SI
							WHERE
								SI.SNO = S.SNO
								AND SI.MID = S.MID
								AND SI.JOINMID = /*mid*/
								AND SI.DELFLG ='0'
						)
				)
			)
			AND S.MID = M.MID
			AND M.STATUS ='1'
	UNION

		-- 自分のスケジュールデータ取得
		SELECT
			NULL AS SNO,
			NULL AS TITLECOLOR,
			CE.CID,
			CB.BBSID,
			CB.TITLE,
			CB.COMMENT AS DETAIL,
			CI.EVENT_YEAR AS STARTYEAR,
			CI.EVENT_MONTH AS STARTMONTH,
			CI.EVENT_DAY AS STARTDAY,
			NULL AS TIME_S,
			NULL AS MINUTE_S,
			NULL AS TIME_E,
			NULL AS MINUTE_E,
			NULL AS STARTTIME,
			NULL AS ENDTIME,
			NULL AS FRIENDSTATUS,
			M.NICKNAME AS NICKNAME,
			'1' AS ENTRY,
			CB.ENTDATE
		FROM
			COMMUNITIES C,
			COMMUNITY_ENTERANT CE,
			COMMUNITY_BBS CB,
			COMMUNITY_EVENT_INFO CI,
			COMMUNITY_EVENTENTRY_INFO CEEI,
			MEMBERS M
		WHERE
			    CE.MID        = /*mid*/
			AND CE.JOINSTATUS = '1'
			AND C.CID         = CE.CID
			AND CE.CID        = CI.CID
			AND CI.CID        = CEEI.CID
			AND CI.BBSID      = CEEI.BBSID
			AND CI.CID        = CB.CID
			AND CI.BBSID      = CB.BBSID
			AND CEEI.MID      = CE.MID
			AND CB.MID        = M.MID
			AND CB.COMNO      = '0'
			AND CB.ENTRYTYPE  = '2'
			AND CB.DELFLG     = '0'
			AND M.STATUS      = '1'
			AND (
					    CI.EVENT_YEAR  >  /*year*/
					OR  CI.EVENT_YEAR  >= /*year*/
					AND CI.EVENT_MONTH >  /*month*/
					OR  CI.EVENT_YEAR  >= /*year*/
					AND CI.EVENT_MONTH >= /*month*/
					AND CI.EVENT_DAY   >= /*day*/
				)
	) A
ORDER BY
	A.STARTYEAR,
	A.STARTMONTH,
	A.STARTDAY,
	A.ENTRY,
	A.TIME_S,
	A.MINUTE_S,
	A.TIME_E,
	A.MINUTE_E,
	A.ENTDATE ASC