UPDATE FILEINFO
SET
  CATEGORYID  = /*categoryid*/
 ,TITLE       = /*title*/
 ,EXPLANATION = /*explanation*/
 ,VERSION     = /*version*/
 ,CONTENT     = /*content*/
 ,FILENAME    = /*filename*/
 ,E_EXTENSION = /*EExtension*/
 ,DOWNLOAD    = 0
 ,FILESIZE    = /*filesize*/
 ,PUBFILE     = /*pubfile*/
 ,UPDID       = /*updid*/
 ,UPDDATE     = /*upddate*/
WHERE
     FILEID = /*fileid*/
 AND HISTORYNO = 1