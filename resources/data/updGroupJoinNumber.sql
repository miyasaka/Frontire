-- グループ参加人数更新ＳＱＬ
UPDATE FRONTIER_GROUP
SET
  JOINNUMBER  = (SELECT MAX(JOINNUMBER)+1 FROM FRONTIER_GROUP WHERE FRONTIERDOMAIN = /*frontierdomain*/ AND GID = /*gid*/)
 ,UPDID    = /*updid*/
 ,UPDDATE  = now()
WHERE
 FRONTIERDOMAIN = /*frontierdomain*/
AND GID = /*gid*/

