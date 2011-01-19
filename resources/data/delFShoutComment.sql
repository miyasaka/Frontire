UPDATE FRONTIERSHOUT
SET
    UPDID   = /*updid*/,
    UPDDATE = NOW(),
    DELFLG  = '1'
WHERE
    MID = /*mid*/
AND NO  = /*no*/