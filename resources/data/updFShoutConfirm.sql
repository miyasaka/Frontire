UPDATE FRONTIERSHOUT
SET
    UPDID   = /*updid*/,
    UPDDATE = NOW(),
    CONFIRMFLG  = '1'
WHERE
    MID = /*mid*/
AND NO  = /*no*/