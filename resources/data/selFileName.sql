SELECT
  FILENAME
FROM
  FILEINFO
WHERE
     FILEID = /*fileid*/
 AND HISTORYNO = /*historyno*/
 /*IF fileAuthOpen != null*/
 AND (UPDID = /*updid*/ OR PUBFILE = /*fileAuthOpen*/)
 /*END*/