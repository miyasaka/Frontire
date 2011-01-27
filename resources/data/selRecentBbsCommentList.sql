SELECT
    CB1.CID AS CMID,
    CB1.BBSID,
    CB3.TITLE,
    C.TITLE AS COMMUNITY,
    CB2.COMMENTS,
	TO_CHAR(CB2.UPD,'YYYY年MM月DD日 hh24:mi') AS UPDATEDATE,
	TO_CHAR(CB3.ENT,'YYYY年MM月DD日 hh24:mi') AS ENTDATE,
	CB1.ENTRYTYPE
FROM
    (SELECT
        CB.CID,
        CB.BBSID,
		CB.ENTRYTYPE
    FROM
        COMMUNITY_BBS CB,COMMUNITY_ENTERANT CE
    WHERE
        CB.CID = CE.CID AND
        CB.MID =/*mid*/ AND
        CB.DELFLG ='0' AND
       -- CB.COMNO !='0' AND 
        CE.JOINSTATUS = '1' AND
        to_char(CB.ENTDATE,'yyyymmdd') > /*day*/
    GROUP BY
        CB.CID,
        CB.BBSID,
		CB.ENTRYTYPE
    ) CB1,
    (SELECT
        CID,
        BBSID,
        COUNT(COMNO)-1 AS COMMENTS,
        MAX(UPDDATE) AS UPD
    FROM
        COMMUNITY_BBS
	WHERE
		DELFLG ='0'
    GROUP BY
        CID,
        BBSID
    ) CB2,
    (SELECT
        CID,
        BBSID,
        TITLE,
		ENTDATE AS ENT
    FROM
        COMMUNITY_BBS
    WHERE
        COMNO ='0'
    ) CB3,
    COMMUNITIES C
WHERE
    CB1.CID = CB2.CID AND
    CB1.BBSID = CB2.BBSID AND
    CB1.CID = CB3.CID AND
    CB1.BBSID = CB3.BBSID AND
    CB1.CID = C.CID
ORDER BY CB2.UPD DESC