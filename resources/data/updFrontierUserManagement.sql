UPDATE
    FRONTIER_USER_MANAGEMENT
SET
    NICKNAME =/*nickname*/
    ,PIC =/*pic*/
    ,UPDID=/*updid*/
    ,UPDDATE=current_timestamp
WHERE
    FID =/*fid*/ AND
    FRONTIERDOMAIN =/*frontierdomain*/
