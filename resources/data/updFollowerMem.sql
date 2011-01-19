-- フォローされた数更新ＳＱＬ
UPDATE MEMBERS
SET
  FOLLOWERNUMBER  = /*followernumber*/
 ,UPDID    = /*updid*/
 ,UPDDATE  = now()
WHERE
     MID = /*mid*/
