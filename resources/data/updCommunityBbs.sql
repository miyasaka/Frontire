-- トピック情報の更新
update community_bbs
set
	title        = /*title*/,
	comment      = /*comment*/,
	pic1         = /*pic1*/,
	pic2         = /*pic2*/,
	pic3         = /*pic3*/,
	picnote1     = /*picnote1*/,
	picnote2     = /*picnote2*/,
	picnote3     = /*picnote3*/,
	upddate      = /*upddate*/,
	updid        = /*updid*/
where
	    cid       = /*cid*/
	and bbsid     = /*bbsid*/
	and entrytype = /*entrytype*/
	and comno     = /*comno*/