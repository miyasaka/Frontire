-- Frontier Net公開日記一覧のデータ取得用ＳＱＬ
SELECT
	D.DIARYID,
	D.TITLE,
	D.COMMENT,
	TO_CHAR(D.ENTDATE,'YYYYMMDDHH24MI') AS ENTDATE,
	COALESCE(
		(
			SELECT COUNT(*)
			FROM DIARY
			WHERE
				MID        = D.MID AND
				DIARYID    = D.DIARYID AND
				COMNO     != 0 AND
				DELFLG     = '0'
		)
	, 0) AS CNT,
	D.PIC1,
	D.PIC2,
	D.PIC3,
	COALESCE(D.PICNOTE1,'') AS PICNOTE1,
	COALESCE(D.PICNOTE2,'') AS PICNOTE2,
	COALESCE(D.PICNOTE3,'') AS PICNOTE3,
	D.MID,
	COALESCE(M.NICKNAME,'')  AS NICKNAME,
	CASE COALESCE(MP.MAINPICNO,'')
		WHEN '1' THEN COALESCE(MP.PIC1,'')
		WHEN '2' THEN COALESCE(MP.PIC2,'')
		WHEN '3' THEN COALESCE(MP.PIC3,'')
		ELSE ''
	END AS PIC
FROM
	DIARY AS D,
	MEMBERS AS M,
	MEMBERPHOTO_INFO AS MP
WHERE
/*IF type == "1"*/
	-- グループ選択時に条件追加
	D.MID IN (
		SELECT GJ.MID
		FROM
			FRONTIER_GROUP AS G,
			FRONTIER_GROUP_JOIN AS GJ
		WHERE
			G.FRONTIERDOMAIN = /*domain*/ AND
			G.GID            = /*gid*/ AND
			G.FRONTIERDOMAIN = GJ.FRONTIERDOMAIN AND
			G.GID            = GJ.GID
	) AND
/*END*/
	D.MID       = M.MID AND
	M.STATUS    = '1' AND
	MP.MID      = M.MID AND
	D.COMNO     = 0 AND
	D.DELFLG    = '0' AND
	D.PUB_DIARY = '1' AND
		(
			-- 外部公開日記の条件
			(D.PUB_LEVEL = '0' AND D.APP_STATUS = '4') OR
			-- Frontier Net公開日記の条件
			(D.PUB_LEVEL = '1' AND D.APP_STATUS = '2')
		)
ORDER BY D.ENTDATE DESC