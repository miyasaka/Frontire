SELECT
    CASE MAINPICNO WHEN '1' THEN PIC1 WHEN '2' THEN PIC2 WHEN '3' THEN PIC3 END AS PHOTO
FROM
    MEMBERPHOTO_INFO
WHERE
    mid =/*mid*/
