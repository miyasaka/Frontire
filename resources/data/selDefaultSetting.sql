SELECT
	MID,
	DISPNUM_MEM,
	DISPNUM_COM,
	DISPNUM_MEMDIARY,
	DISPNUM_MEMDIARYCMNT,
	DISPNUM_COMBBS,
	DISPNUM_COMBBSCMNT,
	DISPNUM_MEMUPDATE,
	SORTITEM_MEMDIARY,
	SORTITEM_MEMDIARYCMNT,
	SORTITEM_COMBBS,
	SORTITEM_COMBBSCMNT,
	SORTITEM_MEMUPDATE,
	DISPNUM_MY,
	SORTITEM_MY,
	DISPPOS_MYPHOTO,
	DISPPOS_MY,
	DISPPOS_MEM,
	DISPPOS_COM,
	DISPPOS_COMUPDATE,
	DISPPOS_DIARYUPDATE,
	DISPPOS_MEMUPDATE,
	DISPNUM_CALENDAR,
	DISPPOS_CALENDAR,
	DISPPOS_MEMDIARYCMNT,
	DISPPOS_COMBBSCMNT,
	DISPNUM_MYPHOTO,
	DISPTYPE_CALENDAR,
	DISPNUM_FSHOUT,
	DISPPOS_FSHOUT,
	DISPNUM_FOLLOWME,
	DISPNUM_FOLLOWYOU,
	DISPNUM_GROUP,
	DISPPOS_FOLLOWME,
	DISPPOS_FOLLOWYOU,
	DISPPOS_GROUP
FROM
	MEMBERSETUP_INFO
WHERE
	MID = /*mid*/