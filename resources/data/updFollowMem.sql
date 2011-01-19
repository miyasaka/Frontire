-- フォローした数更新ＳＱＬ
UPDATE MEMBERS
SET
  FOLLOWNUMBER  = /*follownumber*/
 ,UPDID    = /*updid*/
 ,UPDDATE  = now()
WHERE
     MID = /*mid*/

