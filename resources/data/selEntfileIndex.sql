SELECT
  A.FILEID
 ,A.TITLE
 ,A.CATEGORYID
 ,B.CATEGORYNAME
 ,A.EXPLANATION
 ,A.VERSION
 ,A.FILENAME
 ,A.PUBFILE
 ,A.ENTID
FROM
  FILEINFO A
  LEFT OUTER JOIN MST_CATEGORY B ON A.CATEGORYID = B.CATEGORYID
WHERE
     A.FILEID = /*fileid*/
 AND A.HISTORYNO = 1